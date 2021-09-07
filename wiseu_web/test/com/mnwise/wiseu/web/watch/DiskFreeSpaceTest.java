package com.mnwise.wiseu.web.watch;

import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;

public class DiskFreeSpaceTest extends TestCase {

    @Test
    public void testDiskFreeSpace() throws Exception {
        File f = new File("c:/");
        // System.out.println("c: total space : " + f.getTotalSpace());
        // System.out.println("c: free space : " + f.getFreeSpace());
        // System.out.println("c: useable space : " + f.getUsableSpace());
    }
}
