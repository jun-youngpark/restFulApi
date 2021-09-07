package com.mnwise.wiseu.web.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.io.IOUtil;

import net.sf.jazzlib.ZipEntry;
import net.sf.jazzlib.ZipInputStream;
import net.sf.jazzlib.ZipOutputStream;

public class ZipUtil {
    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

    private static final int COMPRESSION_LEVEL = 8;

    /**
     * 여러 파일을 묶어서 zip 형식으로 만듭니다.
     *
     * @param files 파일 목록
     * @param names 파일 이름
     * @param zipFile 생성될 zip 파일
     * @throws Exception
     */
    public synchronized static void compressZip(Collection<File> fileList, String zipPath) throws Exception {
        int listSize = (fileList == null) ? 0 : fileList.size();

        String[] files = new String[listSize];
        String[] names = new String[listSize];

        Iterator<File> iterator = fileList.iterator();

        for(int i = 0; iterator.hasNext(); i++) {
            File file = iterator.next();
            files[i] = file.getAbsolutePath();
            names[i] = FilenameUtils.getName(file.getName());
        }
        compressZip(files, names, zipPath);
    }

    /**
     * 여러 파일을 묶어서 zip 형식으로 만듭니다.
     *
     * @param files 파일 목록
     * @param names 파일 이름
     * @param zipFile 생성될 zip 파일
     * @throws Exception
     */
    public synchronized static void compressZip(String[] files, String[] names, String zipPath) throws Exception {
        ZipOutputStream out = null;

        long startTime = System.currentTimeMillis();

        try {
            File zipFile = new File(zipPath);
            // if(!zipFile.exists()) zipFile.delete(); zipFile이 존재하면 삭제하는 것으로 수정
            if(zipFile.exists())
                zipFile.delete();

            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipPath)));

            for(int i = 0; i < files.length; i++) {
                File existFile = new File(files[i]);

                if(existFile.canRead()) {
                    BufferedInputStream in = null;
                    try {
                        in = new BufferedInputStream(new FileInputStream(files[i]));
                        out.putNextEntry(new ZipEntry(names[i]));
                        out.setLevel(COMPRESSION_LEVEL);

                        int read = -1;
                        byte[] b = new byte[8192];
                        while((read = in.read(b, 0, b.length)) != -1) {
                            out.write(b, 0, read);
                        }

                        out.closeEntry();
                        out.flush();
                        log.info("압축 한 파일 => " + names[i]);
                    } catch(Exception e) {
                        throw e;
                    } finally {
                        IOUtil.closeQuietly(in);
                    }
                } else {
                    throw new FileNotFoundException(names[i] + " 파일이 존재하지 않거나 읽을 수 없습니다.");
                }
            }

            out.finish();
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
        }

        long endTime = System.currentTimeMillis();

        log.info(" * 압축이 완료되었습니다.");
        log.info(" * 압축 된 파일: " + zipPath);
        log.info(" * 압축 하는 데 걸린시간: " + (endTime - startTime) + " ms");
    }

    /**
     * 압축파일을 해제합니다. 에디터의 ZIP 파일 올리기에서만 사용된다.
     *
     * @param decompressPath 압축 해제할 경로
     * @param zipFile 압축파일
     * @throws Exception
     */
    public synchronized static String decompressZip(String decompressPath, File zipFile) throws Exception {
        log.info(" * ZIP 파일 압축풀기 시작.");

        long start = System.currentTimeMillis();

        boolean existImage = false;
        boolean existHtml = false;
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
        BufferedOutputStream out = null;
        String imagePath = null;

        File decompressFile = new File(decompressPath);
        if(!decompressFile.exists()) {
            decompressFile.mkdirs();
            log.info(" * 압축 풀 경로 생성: " + decompressFile.getPath());
        }

        File entryFile = null;
        File entryParentFile = null;
        ZipEntry ze = null;
        while((ze = in.getNextEntry()) != null) {
            String entryName = ze.getName();

            /* image */
            if(entryName.toLowerCase().startsWith("image")) {
                existImage = true;

                if(entryName.endsWith("/")) {
                    entryParentFile = new File(decompressPath + "/" + entryName);

                    if(!entryParentFile.exists())
                        entryParentFile.exists();
                    imagePath = entryParentFile.getPath();
                    continue;

                } else {
                    entryFile = new File(decompressPath + "/" + entryName);
                    entryParentFile = entryFile.getParentFile();

                    if(!entryParentFile.exists())
                        entryParentFile.mkdirs();
                    imagePath = entryParentFile.getPath();
                }
            }

            /* HTML */
            if(entryName.toLowerCase().endsWith(".htm") || entryName.toLowerCase().endsWith(".html")) {
                existHtml = true;
                entryFile = new File(decompressPath + "/" + entryName);
            }

            /* 압축풀기 */
            out = new BufferedOutputStream(new FileOutputStream(entryFile));
            byte[] b = new byte[8192];
            int i = -1;
            while((i = in.read(b, 0, b.length)) > -1) {
                out.write(b, 0, i);
            }
            IOUtil.closeQuietly(out);

            log.info(" * 압축 푼 경로: " + entryFile.getPath());
        }

        IOUtil.closeQuietly(in);

        long end = System.currentTimeMillis();

        if(!existImage) {
            log.info("image(s) 폴더가 없습니다.");

        } else if(!existHtml) {
            log.info("html 파일이 없습니다.");

        } else {
            log.info(" * 압축 푼 파일: " + zipFile.getPath());
            log.info(" * 압축 푸는데 걸린시간: " + (end - start) + " ms");
            log.info(" * ZIP 파일 압축풀기 완료.");
        }

        return imagePath;
    }

}
