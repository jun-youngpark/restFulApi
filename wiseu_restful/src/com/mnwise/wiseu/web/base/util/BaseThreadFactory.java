package com.mnwise.wiseu.web.base.util;

import java.util.concurrent.ThreadFactory;

public class BaseThreadFactory implements ThreadFactory {

    private final String poolName;

    public BaseThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    public Thread newThread(Runnable r) {
        return new BaseThread(r, poolName);
    }

}
