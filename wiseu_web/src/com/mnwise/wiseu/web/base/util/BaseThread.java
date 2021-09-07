package com.mnwise.wiseu.web.base.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 기본 스레드 - Java Concurrency 참고
 */
public class BaseThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(BaseThread.class);

    private static final String DEFAULT_NAME = "BaseWorkerThread";
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();

    public BaseThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public BaseThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                log.warn("Uncaught in thread " + t.getName(), e);
            }
        });
    }

    public void run() {
        if(log.isDebugEnabled()) {
            log.debug("Created " + getName());
        }

        try {
            alive.incrementAndGet();
            super.run();
        } catch(Exception e) {
            alive.decrementAndGet();
            if(log.isDebugEnabled()) {
                log.debug("Exiting " + getName());
            }
        }
    }

    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

}
