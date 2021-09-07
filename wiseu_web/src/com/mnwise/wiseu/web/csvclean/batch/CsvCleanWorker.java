package com.mnwise.wiseu.web.csvclean.batch;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * CSV File Clean용 배치 스레드
 */
public class CsvCleanWorker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CsvCleanWorker.class);
    @Value("${import.upload.dir}")
    private String uploadDir;
    @Value("${import.clean.time}")
    private String cleanTime;
    @Value("${import.execute.Period}")
    private int executePeriod;

    public void run() {
        try {
            while(true) {
                TimeUnit.SECONDS.sleep(this.executePeriod);
                checkFileWhenTimeIsDelete(new File(this.uploadDir));
            }
        } catch(Exception e) {
            log.error("[CsvCleanWorker] Exception occurred. Message: " + e.getMessage());
        }
    }

    private void checkFileWhenTimeIsDelete(File filePath) {
        long nowDt = System.currentTimeMillis();

        if(filePath.isDirectory()) {
            File[] fileArray = filePath.listFiles();
            for(File file : fileArray) {
                if(file.isDirectory()) {
                    checkFileWhenTimeIsDelete(file);
                } else {
                    long lastModifyDt = file.lastModified();
                    long preservePeriod = (Integer.parseInt(cleanTime))  * 60 * 60 * 1000;

                    String tmpFileNm = file.getName();
                    if(nowDt - lastModifyDt > preservePeriod) {
                        if(tmpFileNm.endsWith(".csv") || tmpFileNm.endsWith(".xls") || tmpFileNm.endsWith(".xlsx")) {
                            if(file.delete()) {
                                log.info("[CsvCleanWorker] " + file.getAbsolutePath() + " deleted");
                            }
                        }
                    }
                }
            }
        }
    }
}