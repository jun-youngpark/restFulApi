package com.mnwise.wiseu.web.csvclean.batch;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.mnwise.wiseu.web.base.util.BaseThreadFactory;
import com.mnwise.wiseu.web.base.util.BaseThreadPoolExecutor;

/**
 * 대량의 대상자 등록시 웹에서 올린 후 남겨진 대상자 파일을 삭제하기 위한 배치
 */
public class CsvCleanManager extends Thread implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(CsvCleanManager.class);

    private static ThreadPoolExecutor executor = null;
    private LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>(10);
    private ApplicationContext ctx;

    public CsvCleanManager(int corePoolSize, int maximumPoolSize) {
        log.debug("CSVCleanManager Start.. ThreadPool[" + corePoolSize + ", " + maximumPoolSize + "]");

        setDaemon(true);
        executor = new BaseThreadPoolExecutor(corePoolSize, maximumPoolSize, 100L, TimeUnit.MILLISECONDS, blockingQueue, new BaseThreadFactory("SfdcBatchSend"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void run() {
        try {
            CsvCleanWorker worker = (CsvCleanWorker) ctx.getBean("csvCleanWorker");
            executor.execute(worker);

            log.debug("CSVClean Status..Active : " + executor.getActiveCount() + " / Maximum : " + executor.getMaximumPoolSize());
        } catch(Exception e) {
            log.error(null, e);
        }
    }
}
