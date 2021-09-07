package com.mnwise.wiseu.web.base.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class xCryptionAria {
    private static final Logger log = LoggerFactory.getLogger(xCryptionAria.class);

    private static final int[][] KRK = {
        {
            0x517cc1b7, 0x27220a94, 0xfe13abe8, 0xfa9a6ee0
        }, {
            0x6db14acc, 0x9e21c820, 0xff28b1d5, 0xef5de2b0
        }, {
            0xdb92371d, 0x2126e970, 0x03249775, 0x04e8c90e
        }
    };

    private static final byte[] S1 = new byte[256];
    private static final byte[] S2 = new byte[256];
    private static final byte[] X1 = new byte[256];
    private static final byte[] X2 = new byte[256];

    private static final int[] TS1 = new int[256];
    private static final int[] TS2 = new int[256];
    private static final int[] TX1 = new int[256];
    private static final int[] TX2 = new int[256];

    // Static initializer. For setting up the tables
    static {
        int[] exp = new int[256];
        int[] log = new int[256];
        exp[0] = 1;
        for(int i = 1; i < 256; i++) {
            int j = (exp[i - 1] << 1) ^ exp[i - 1];
            if((j & 0x100) != 0)
                j ^= 0x11b;
            exp[i] = j;
        }
        for(int i = 1; i < 255; i++)
            log[exp[i]] = i;

        int[][] A = {
            {
                1, 0, 0, 0, 1, 1, 1, 1
            }, {
                1, 1, 0, 0, 0, 1, 1, 1
            }, {
                1, 1, 1, 0, 0, 0, 1, 1
            }, {
                1, 1, 1, 1, 0, 0, 0, 1
            }, {
                1, 1, 1, 1, 1, 0, 0, 0
            }, {
                0, 1, 1, 1, 1, 1, 0, 0
            }, {
                0, 0, 1, 1, 1, 1, 1, 0
            }, {
                0, 0, 0, 1, 1, 1, 1, 1
            }
        };
        int[][] B = {
            {
                0, 1, 0, 1, 1, 1, 1, 0
            }, {
                0, 0, 1, 1, 1, 1, 0, 1
            }, {
                1, 1, 0, 1, 0, 1, 1, 1
            }, {
                1, 0, 0, 1, 1, 1, 0, 1
            }, {
                0, 0, 1, 0, 1, 1, 0, 0
            }, {
                1, 0, 0, 0, 0, 0, 0, 1
            }, {
                0, 1, 0, 1, 1, 1, 0, 1
            }, {
                1, 1, 0, 1, 0, 0, 1, 1
            }
        };

        for(int i = 0; i < 256; i++) {
            int t = 0, p;
            if(i == 0)
                p = 0;
            else
                p = exp[255 - log[i]];
            for(int j = 0; j < 8; j++) {
                int s = 0;
                for(int k = 0; k < 8; k++) {
                    if(((p >>> (7 - k)) & 0x01) != 0)
                        s ^= A[k][j];
                }
                t = (t << 1) ^ s;
            }
            t ^= 0x63;
            S1[i] = (byte) t;
            X1[t] = (byte) i;
        }
        for(int i = 0; i < 256; i++) {
            int t = 0, p;
            if(i == 0)
                p = 0;
            else
                p = exp[(247 * log[i]) % 255];
            for(int j = 0; j < 8; j++) {
                int s = 0;
                for(int k = 0; k < 8; k++) {
                    if(((p >>> k) & 0x01) != 0)
                        s ^= B[7 - j][k];
                }
                t = (t << 1) ^ s;
            }
            t ^= 0xe2;
            S2[i] = (byte) t;
            X2[t] = (byte) i;
        }

        for(int i = 0; i < 256; i++) {
            TS1[i] = 0x00010101 * (S1[i] & 0xff);
            TS2[i] = 0x01000101 * (S2[i] & 0xff);
            TX1[i] = 0x01010001 * (X1[i] & 0xff);
            TX2[i] = 0x01010100 * (X2[i] & 0xff);
        }
    }

    private int keySize = 0;
    private int numberOfRounds = 0;
    private byte[] masterKey = null;
    private int[] encRoundKeys = null;
    private int[] decRoundKeys = null;
    private static String secretKey = "123321123";
    private static String charset = "euc-kr";

    public xCryptionAria(int keySize) throws InvalidKeyException {
        setKeySize(keySize);
    }

    /**
     * Resets the class so that it can be reused for another master key.
     */
    void reset() {
        this.keySize = 0;
        this.numberOfRounds = 0;
        this.masterKey = null;
        this.encRoundKeys = null;
        this.decRoundKeys = null;
    }

    int getKeySize() {
        return this.keySize;
    }

    void setKeySize(int keySize) throws InvalidKeyException {
        this.reset();
        if(keySize != 128 && keySize != 192 && keySize != 256)
            throw new InvalidKeyException("keySize=" + keySize);
        this.keySize = keySize;
        switch(keySize) {
            case 128:
                this.numberOfRounds = 12;
                break;
            case 192:
                this.numberOfRounds = 14;
                break;
            case 256:
                this.numberOfRounds = 16;
        }
    }

    void setKey(byte[] masterKey) throws InvalidKeyException {
        if(masterKey.length * 8 < keySize)
            throw new InvalidKeyException("masterKey size=" + masterKey.length);
        this.decRoundKeys = null;
        this.encRoundKeys = null;
        this.masterKey = (byte[]) masterKey.clone();
    }

    void setupEncRoundKeys() throws InvalidKeyException {
        if(this.keySize == 0)
            throw new InvalidKeyException("keySize");
        if(this.masterKey == null)
            throw new InvalidKeyException("masterKey");
        if(this.encRoundKeys == null)
            this.encRoundKeys = new int[4 * (this.numberOfRounds + 1)];
        this.decRoundKeys = null;
        doEncKeySetup(this.masterKey, this.encRoundKeys, this.keySize);
    }

    void setupDecRoundKeys() throws InvalidKeyException {
        if(this.keySize == 0)
            throw new InvalidKeyException("keySize");
        if(this.encRoundKeys == null)
            if(this.masterKey == null)
                throw new InvalidKeyException("masterKey");
            else
                setupEncRoundKeys();
        this.decRoundKeys = (int[]) encRoundKeys.clone();
        doDecKeySetup(this.masterKey, this.decRoundKeys, this.keySize);
    }

    void setupRoundKeys() throws InvalidKeyException {
        setupDecRoundKeys();
    }

    private static void doCrypt(byte[] i, int ioffset, int[] rk, int nr, byte[] o, int ooffset) {
        int t0, t1, t2, t3, j = 0;

        byte[] blocks = new byte[16];

        // i가 16바이트가 되지 않으면 나머지는 null로 채운다.
        for(int index = 0; index < 16; index++) {
            if(index >= (i.length - ioffset))
                blocks[index] = 0x00;
            else
                blocks[index] = i[index + ioffset];
        }
        t0 = toInt(blocks[0], blocks[1], blocks[2], blocks[3]);
        t1 = toInt(blocks[4], blocks[5], blocks[6], blocks[7]);
        t2 = toInt(blocks[8], blocks[9], blocks[10], blocks[11]);
        t3 = toInt(blocks[12], blocks[13], blocks[14], blocks[15]);

        for(int r = 1; r < nr / 2; r++) {
            t0 ^= rk[j++];
            t1 ^= rk[j++];
            t2 ^= rk[j++];
            t3 ^= rk[j++];
            t0 = TS1[(t0 >>> 24) & 0xff] ^ TS2[(t0 >>> 16) & 0xff] ^ TX1[(t0 >>> 8) & 0xff] ^ TX2[t0 & 0xff];
            t1 = TS1[(t1 >>> 24) & 0xff] ^ TS2[(t1 >>> 16) & 0xff] ^ TX1[(t1 >>> 8) & 0xff] ^ TX2[t1 & 0xff];
            t2 = TS1[(t2 >>> 24) & 0xff] ^ TS2[(t2 >>> 16) & 0xff] ^ TX1[(t2 >>> 8) & 0xff] ^ TX2[t2 & 0xff];
            t3 = TS1[(t3 >>> 24) & 0xff] ^ TS2[(t3 >>> 16) & 0xff] ^ TX1[(t3 >>> 8) & 0xff] ^ TX2[t3 & 0xff];
            t1 ^= t2;
            t2 ^= t3;
            t0 ^= t1;
            t3 ^= t1;
            t2 ^= t0;
            t1 ^= t2;
            t1 = badc(t1);
            t2 = cdab(t2);
            t3 = dcba(t3);
            t1 ^= t2;
            t2 ^= t3;
            t0 ^= t1;
            t3 ^= t1;
            t2 ^= t0;
            t1 ^= t2;

            t0 ^= rk[j++];
            t1 ^= rk[j++];
            t2 ^= rk[j++];
            t3 ^= rk[j++];
            t0 = TX1[(t0 >>> 24) & 0xff] ^ TX2[(t0 >>> 16) & 0xff] ^ TS1[(t0 >>> 8) & 0xff] ^ TS2[t0 & 0xff];
            t1 = TX1[(t1 >>> 24) & 0xff] ^ TX2[(t1 >>> 16) & 0xff] ^ TS1[(t1 >>> 8) & 0xff] ^ TS2[t1 & 0xff];
            t2 = TX1[(t2 >>> 24) & 0xff] ^ TX2[(t2 >>> 16) & 0xff] ^ TS1[(t2 >>> 8) & 0xff] ^ TS2[t2 & 0xff];
            t3 = TX1[(t3 >>> 24) & 0xff] ^ TX2[(t3 >>> 16) & 0xff] ^ TS1[(t3 >>> 8) & 0xff] ^ TS2[t3 & 0xff];
            t1 ^= t2;
            t2 ^= t3;
            t0 ^= t1;
            t3 ^= t1;
            t2 ^= t0;
            t1 ^= t2;
            t3 = badc(t3);
            t0 = cdab(t0);
            t1 = dcba(t1);
            t1 ^= t2;
            t2 ^= t3;
            t0 ^= t1;
            t3 ^= t1;
            t2 ^= t0;
            t1 ^= t2;
        }
        t0 ^= rk[j++];
        t1 ^= rk[j++];
        t2 ^= rk[j++];
        t3 ^= rk[j++];
        t0 = TS1[(t0 >>> 24) & 0xff] ^ TS2[(t0 >>> 16) & 0xff] ^ TX1[(t0 >>> 8) & 0xff] ^ TX2[t0 & 0xff];
        t1 = TS1[(t1 >>> 24) & 0xff] ^ TS2[(t1 >>> 16) & 0xff] ^ TX1[(t1 >>> 8) & 0xff] ^ TX2[t1 & 0xff];
        t2 = TS1[(t2 >>> 24) & 0xff] ^ TS2[(t2 >>> 16) & 0xff] ^ TX1[(t2 >>> 8) & 0xff] ^ TX2[t2 & 0xff];
        t3 = TS1[(t3 >>> 24) & 0xff] ^ TS2[(t3 >>> 16) & 0xff] ^ TX1[(t3 >>> 8) & 0xff] ^ TX2[t3 & 0xff];
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        t1 = badc(t1);
        t2 = cdab(t2);
        t3 = dcba(t3);
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;

        t0 ^= rk[j++];
        t1 ^= rk[j++];
        t2 ^= rk[j++];
        t3 ^= rk[j++];
        o[0 + ooffset] = (byte) (X1[0xff & (t0 >>> 24)] ^ (rk[j] >>> 24));
        o[1 + ooffset] = (byte) (X2[0xff & (t0 >>> 16)] ^ (rk[j] >>> 16));
        o[2 + ooffset] = (byte) (S1[0xff & (t0 >>> 8)] ^ (rk[j] >>> 8));
        o[3 + ooffset] = (byte) (S2[0xff & (t0)] ^ (rk[j]));
        o[4 + ooffset] = (byte) (X1[0xff & (t1 >>> 24)] ^ (rk[j + 1] >>> 24));
        o[5 + ooffset] = (byte) (X2[0xff & (t1 >>> 16)] ^ (rk[j + 1] >>> 16));
        o[6 + ooffset] = (byte) (S1[0xff & (t1 >>> 8)] ^ (rk[j + 1] >>> 8));
        o[7 + ooffset] = (byte) (S2[0xff & (t1)] ^ (rk[j + 1]));
        o[8 + ooffset] = (byte) (X1[0xff & (t2 >>> 24)] ^ (rk[j + 2] >>> 24));
        o[9 + ooffset] = (byte) (X2[0xff & (t2 >>> 16)] ^ (rk[j + 2] >>> 16));
        o[10 + ooffset] = (byte) (S1[0xff & (t2 >>> 8)] ^ (rk[j + 2] >>> 8));
        o[11 + ooffset] = (byte) (S2[0xff & (t2)] ^ (rk[j + 2]));
        o[12 + ooffset] = (byte) (X1[0xff & (t3 >>> 24)] ^ (rk[j + 3] >>> 24));
        o[13 + ooffset] = (byte) (X2[0xff & (t3 >>> 16)] ^ (rk[j + 3] >>> 16));
        o[14 + ooffset] = (byte) (S1[0xff & (t3 >>> 8)] ^ (rk[j + 3] >>> 8));
        o[15 + ooffset] = (byte) (S2[0xff & (t3)] ^ (rk[j + 3]));
    }

    void encrypt(byte[] i, int ioffset, byte[] o, int ooffset) throws InvalidKeyException {
        if(this.keySize == 0)
            throw new InvalidKeyException("keySize");
        if(this.encRoundKeys == null)
            if(this.masterKey == null)
                throw new InvalidKeyException("masterKey");
            else
                setupEncRoundKeys();
        doCrypt(i, ioffset, this.encRoundKeys, this.numberOfRounds, o, ooffset);
    }

    byte[] encrypt(byte[] i, int ioffset) throws InvalidKeyException {
        byte[] o = new byte[16];
        this.encrypt(i, ioffset, o, 0);
        return o;
    }

    void decrypt(byte[] i, int ioffset, byte[] o, int ooffset) throws InvalidKeyException {
        if(this.keySize == 0)
            throw new InvalidKeyException("keySize");
        if(this.decRoundKeys == null)
            if(this.masterKey == null)
                throw new InvalidKeyException("masterKey");
            else
                setupDecRoundKeys();
        doCrypt(i, ioffset, this.decRoundKeys, this.numberOfRounds, o, ooffset);
    }

    byte[] decrypt(byte[] i, int ioffset) throws InvalidKeyException {
        byte[] o = new byte[16];
        this.decrypt(i, ioffset, o, 0);
        return o;
    }

    private static void doEncKeySetup(byte[] mk, int[] rk, int keyBits) {
        int t0, t1, t2, t3, q, j = 0;
        int[] w0 = new int[4];
        int[] w1 = new int[4];
        int[] w2 = new int[4];
        int[] w3 = new int[4];

        w0[0] = toInt(mk[0], mk[1], mk[2], mk[3]);
        w0[1] = toInt(mk[4], mk[5], mk[6], mk[7]);
        w0[2] = toInt(mk[8], mk[9], mk[10], mk[11]);
        w0[3] = toInt(mk[12], mk[13], mk[14], mk[15]);

        q = (keyBits - 128) / 64;
        t0 = w0[0] ^ KRK[q][0];
        t1 = w0[1] ^ KRK[q][1];
        t2 = w0[2] ^ KRK[q][2];
        t3 = w0[3] ^ KRK[q][3];
        t0 = TS1[(t0 >>> 24) & 0xff] ^ TS2[(t0 >>> 16) & 0xff] ^ TX1[(t0 >>> 8) & 0xff] ^ TX2[t0 & 0xff];
        t1 = TS1[(t1 >>> 24) & 0xff] ^ TS2[(t1 >>> 16) & 0xff] ^ TX1[(t1 >>> 8) & 0xff] ^ TX2[t1 & 0xff];
        t2 = TS1[(t2 >>> 24) & 0xff] ^ TS2[(t2 >>> 16) & 0xff] ^ TX1[(t2 >>> 8) & 0xff] ^ TX2[t2 & 0xff];
        t3 = TS1[(t3 >>> 24) & 0xff] ^ TS2[(t3 >>> 16) & 0xff] ^ TX1[(t3 >>> 8) & 0xff] ^ TX2[t3 & 0xff];
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        t1 = badc(t1);
        t2 = cdab(t2);
        t3 = dcba(t3);
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;

        if(keyBits > 128) {
            w1[0] = toInt(mk[16], mk[17], mk[18], mk[19]);
            w1[1] = toInt(mk[20], mk[21], mk[22], mk[23]);
            if(keyBits > 192) {
                w1[2] = toInt(mk[24], mk[25], mk[26], mk[27]);
                w1[3] = toInt(mk[28], mk[29], mk[30], mk[31]);
            } else {
                w1[2] = w1[3] = 0;
            }
        } else {
            w1[0] = w1[1] = w1[2] = w1[3] = 0;
        }
        w1[0] ^= t0;
        w1[1] ^= t1;
        w1[2] ^= t2;
        w1[3] ^= t3;
        t0 = w1[0];
        t1 = w1[1];
        t2 = w1[2];
        t3 = w1[3];

        q = (q == 2) ? 0 : (q + 1);
        t0 ^= KRK[q][0];
        t1 ^= KRK[q][1];
        t2 ^= KRK[q][2];
        t3 ^= KRK[q][3];
        t0 = TX1[(t0 >>> 24) & 0xff] ^ TX2[(t0 >>> 16) & 0xff] ^ TS1[(t0 >>> 8) & 0xff] ^ TS2[t0 & 0xff];
        t1 = TX1[(t1 >>> 24) & 0xff] ^ TX2[(t1 >>> 16) & 0xff] ^ TS1[(t1 >>> 8) & 0xff] ^ TS2[t1 & 0xff];
        t2 = TX1[(t2 >>> 24) & 0xff] ^ TX2[(t2 >>> 16) & 0xff] ^ TS1[(t2 >>> 8) & 0xff] ^ TS2[t2 & 0xff];
        t3 = TX1[(t3 >>> 24) & 0xff] ^ TX2[(t3 >>> 16) & 0xff] ^ TS1[(t3 >>> 8) & 0xff] ^ TS2[t3 & 0xff];
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        t3 = badc(t3);
        t0 = cdab(t0);
        t1 = dcba(t1);
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        t0 ^= w0[0];
        t1 ^= w0[1];
        t2 ^= w0[2];
        t3 ^= w0[3];
        w2[0] = t0;
        w2[1] = t1;
        w2[2] = t2;
        w2[3] = t3;

        q = (q == 2) ? 0 : (q + 1);
        t0 ^= KRK[q][0];
        t1 ^= KRK[q][1];
        t2 ^= KRK[q][2];
        t3 ^= KRK[q][3];
        t0 = TS1[(t0 >>> 24) & 0xff] ^ TS2[(t0 >>> 16) & 0xff] ^ TX1[(t0 >>> 8) & 0xff] ^ TX2[t0 & 0xff];
        t1 = TS1[(t1 >>> 24) & 0xff] ^ TS2[(t1 >>> 16) & 0xff] ^ TX1[(t1 >>> 8) & 0xff] ^ TX2[t1 & 0xff];
        t2 = TS1[(t2 >>> 24) & 0xff] ^ TS2[(t2 >>> 16) & 0xff] ^ TX1[(t2 >>> 8) & 0xff] ^ TX2[t2 & 0xff];
        t3 = TS1[(t3 >>> 24) & 0xff] ^ TS2[(t3 >>> 16) & 0xff] ^ TX1[(t3 >>> 8) & 0xff] ^ TX2[t3 & 0xff];
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        t1 = badc(t1);
        t2 = cdab(t2);
        t3 = dcba(t3);
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        w3[0] = t0 ^ w1[0];
        w3[1] = t1 ^ w1[1];
        w3[2] = t2 ^ w1[2];
        w3[3] = t3 ^ w1[3];

        gsrk(w0, w1, 19, rk, j);
        j += 4;
        gsrk(w1, w2, 19, rk, j);
        j += 4;
        gsrk(w2, w3, 19, rk, j);
        j += 4;
        gsrk(w3, w0, 19, rk, j);
        j += 4;
        gsrk(w0, w1, 31, rk, j);
        j += 4;
        gsrk(w1, w2, 31, rk, j);
        j += 4;
        gsrk(w2, w3, 31, rk, j);
        j += 4;
        gsrk(w3, w0, 31, rk, j);
        j += 4;
        gsrk(w0, w1, 67, rk, j);
        j += 4;
        gsrk(w1, w2, 67, rk, j);
        j += 4;
        gsrk(w2, w3, 67, rk, j);
        j += 4;
        gsrk(w3, w0, 67, rk, j);
        j += 4;
        gsrk(w0, w1, 97, rk, j);
        j += 4;
        if(keyBits > 128) {
            gsrk(w1, w2, 97, rk, j);
            j += 4;
            gsrk(w2, w3, 97, rk, j);
            j += 4;
        }
        if(keyBits > 192) {
            gsrk(w3, w0, 97, rk, j);
            j += 4;
            gsrk(w0, w1, 109, rk, j);
        }
    }

    /**
     * Main bulk of the decryption key setup method. Here we assume that the int array rk already contains the encryption round keys.
     *
     * @param mk the master key
     * @param rk the array which contains the encryption round keys at the beginning of the method execution. At the end of method execution this will hold the decryption round keys.
     * @param keyBits the length of the master key
     * @return
     */
    private static void doDecKeySetup(byte[] mk, int[] rk, int keyBits) {
        int a = 0, z;
        int[] t = new int[4];

        z = 32 + keyBits / 8;
        swapBlocks(rk, 0, z);
        a += 4;
        z -= 4;

        for(; a < z; a += 4, z -= 4)
            swapAndDiffuse(rk, a, z, t);
        diff(rk, a, t, 0);
        rk[a] = t[0];
        rk[a + 1] = t[1];
        rk[a + 2] = t[2];
        rk[a + 3] = t[3];
    }

    private static int toInt(byte b0, byte b1, byte b2, byte b3) {
        return (b0 & 0xff) << 24 ^ (b1 & 0xff) << 16 ^ (b2 & 0xff) << 8 ^ b3 & 0xff;
    }

    private static int m(int t) {
        return 0x00010101 * ((t >>> 24) & 0xff) ^ 0x01000101 * ((t >>> 16) & 0xff) ^ 0x01010001 * ((t >>> 8) & 0xff) ^ 0x01010100 * (t & 0xff);
    }

    private static final int badc(int t) {
        return ((t << 8) & 0xff00ff00) ^ ((t >>> 8) & 0x00ff00ff);
    }

    private static final int cdab(int t) {
        return ((t << 16) & 0xffff0000) ^ ((t >>> 16) & 0x0000ffff);
    }

    private static final int dcba(int t) {
        return (t & 0x000000ff) << 24 ^ (t & 0x0000ff00) << 8 ^ (t & 0x00ff0000) >>> 8 ^ (t & 0xff000000) >>> 24;
    }

    private static final void gsrk(int[] x, int[] y, int rot, int[] rk, int offset) {
        int q = 4 - (rot / 32), r = rot % 32, s = 32 - r;

        rk[offset] = x[0] ^ y[(q) % 4] >>> r ^ y[(q + 3) % 4] << s;
        rk[offset + 1] = x[1] ^ y[(q + 1) % 4] >>> r ^ y[(q) % 4] << s;
        rk[offset + 2] = x[2] ^ y[(q + 2) % 4] >>> r ^ y[(q + 1) % 4] << s;
        rk[offset + 3] = x[3] ^ y[(q + 3) % 4] >>> r ^ y[(q + 2) % 4] << s;
    }

    private static final void diff(int[] i, int offset1, int[] o, int offset2) {
        int t0, t1, t2, t3;

        t0 = m(i[offset1]);
        t1 = m(i[offset1 + 1]);
        t2 = m(i[offset1 + 2]);
        t3 = m(i[offset1 + 3]);
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        t1 = badc(t1);
        t2 = cdab(t2);
        t3 = dcba(t3);
        t1 ^= t2;
        t2 ^= t3;
        t0 ^= t1;
        t3 ^= t1;
        t2 ^= t0;
        t1 ^= t2;
        o[offset2] = t0;
        o[offset2 + 1] = t1;
        o[offset2 + 2] = t2;
        o[offset2 + 3] = t3;
    }

    private static final void swapBlocks(int[] arr, int offset1, int offset2) {
        int t;

        for(int i = 0; i < 4; i++) {
            t = arr[offset1 + i];
            arr[offset1 + i] = arr[offset2 + i];
            arr[offset2 + i] = t;
        }
    }

    private static final void swapAndDiffuse(int[] arr, int offset1, int offset2, int[] tmp) {
        diff(arr, offset1, tmp, 0);
        diff(arr, offset2, arr, offset1);
        arr[offset2] = tmp[0];
        arr[offset2 + 1] = tmp[1];
        arr[offset2 + 2] = tmp[2];
        arr[offset2 + 3] = tmp[3];
    }

    public static String encrypt(String plainText, String secretKey) {
        return encrypt(plainText, secretKey, charset);
    }

    public static String encrypt(String plainText, String secretKey, String charset) {
        byte[] plainTextBytes;
        try {
            plainTextBytes = plainText.getBytes(charset);

            byte[] secretKeyBytes = secretKey.getBytes();
            byte[] roundKeyBytes = new byte[32];
            xCryptionAria cipher = new xCryptionAria(256);

            // 키로 들어온 문자열에서 256비트만 사용하기 위한 처리
            for(int i = 0; i < 32; i++)
                roundKeyBytes[i] = 0;
            for(int i = 0; i < 32 & i < secretKeyBytes.length; i++)
                roundKeyBytes[i] = secretKeyBytes[i];

            // 마지막 블록이 128비트(16바이트)가 아니면 부족한 비트를 채워 128비트로 만든다.
            int padding = plainTextBytes.length % 16;
            padding = padding == 0 ? 0 : 16 - padding;
            int plainTextBlockLength = plainTextBytes.length < 16 ? 16 : plainTextBytes.length + padding;
            byte cipherTextBytes[] = new byte[0 + plainTextBlockLength];
            int iTotalBlocks = plainTextBlockLength / 16;

            cipher.setKey(roundKeyBytes);
            cipher.setupRoundKeys();

            for(int i = 0; i < iTotalBlocks; i++) {
                cipher.encrypt(plainTextBytes, i * 16, cipherTextBytes, i * 16);
            }

            return hexToString(cipherTextBytes);
        } catch(Exception e) {
            log.error(null, e);
        }
        return null;
    }

    public static String decrypt(String cipherTextBase64, String secretKey) throws Exception {
        return decrypt(cipherTextBase64, secretKey, charset);
    }

    public static String decrypt(String cipherTextBase64, String secretKey, String charset) throws Exception {
        if(!Cipher.isCipherShinhanCard(cipherTextBase64))
            return cipherTextBase64;

        byte cipherTextBytes[] = stringToHex(cipherTextBase64);

        byte[] secretKeyBytes = secretKey.getBytes();
        byte[] roundKeyBytes = new byte[32];
        byte[] plainTextBytes = new byte[cipherTextBytes.length];
        xCryptionAria cipher = new xCryptionAria(256);

        // 키로 들어온 문자열에서 256비트만 사용하기 위한 처리
        for(int i = 0; i < 32; i++)
            roundKeyBytes[i] = 0;
        for(int i = 0; i < 32 & i < secretKeyBytes.length; i++)
            roundKeyBytes[i] = secretKeyBytes[i];

        cipher.setKey(roundKeyBytes);
        cipher.setupRoundKeys();

        int blocks = cipherTextBytes.length / 16;
        for(int i = 0; i < blocks; i++) {
            cipher.decrypt(cipherTextBytes, i * 16, plainTextBytes, i * 16);
        }

        // 복호화된 바이트 배열의 null을 제외한 실제 길이를 구함
        int i = 0;
        for(i = plainTextBytes.length - 1; i >= 0; i--) {
            if(0x00 != plainTextBytes[i])
                break;
        }

        return new String(plainTextBytes, 0, i + 1, charset);
    }

    public static String hexToString(byte[] source) {
        StringBuffer hex = new StringBuffer();
        int value = 0;

        for(int i = 0; i < source.length; i++) {
            value = 0xFF & source[i];
            if(value <= 0xf)
                hex.append("0");
            hex.append(Integer.toHexString(value).toUpperCase());
        }
        return hex.toString();
    }

    public static byte[] stringToHex(String source) {
        byte[] encodeBytes = null;
        try {
            encodeBytes = source.getBytes("8859_1");
        } catch(UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        ;
        byte[] decode = new byte[encodeBytes.length / 2];
        int pos = 0;
        int lValue = 0;
        int rValue = 0;
        int value = 0;

        for(int i = 0; i < source.length(); i += 2) {
            lValue = (0xFF & encodeBytes[i + 0]);
            // 'A'-'F'(0x41-0x46)을 0x0A-0x0F로 변환,
            // 'A' 이상이면 0x37을 빼고 '0'~'9'이면 0x30을 뺌
            lValue = lValue >= 0x41 ? lValue - 0x37 : lValue - 0x30;
            rValue = (0xFF & encodeBytes[i + 1]);
            rValue = rValue >= 0x41 ? rValue - 0x37 : rValue - 0x30;
            value = (lValue << 4) + rValue;
            decode[pos++] = (byte) value;
        }
        return decode;
    }

    public static String encrypt(String plainText) {
        return encrypt(plainText, secretKey);
    }

    public static String decrypt(String cipherTextHex) throws Exception {
        return decrypt(cipherTextHex, secretKey);
    }

    public static void setSecretKey(String secretKey) {
        secretKey = xCryptionAria.secretKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }
}
