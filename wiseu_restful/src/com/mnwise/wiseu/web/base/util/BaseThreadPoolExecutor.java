package com.mnwise.wiseu.web.base.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 기본 스레드 풀 Executor Java Concurrency 참고
 * 
 * @author Wee
 * @since 2009-07-18
 */
public class BaseThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * 기본 스레드 풀 Executor
     * 
     * @param corePoolSize idle 상태에서의 스레드 풀 유지 갯수
     * @param maximumPoolSize 최대 사용가능한 스레드 풀 갯수
     * @param keepAliveTime thread의 수가 core 스레드 수 보다 많을 경우 새로운 테스크를 만들기전 대기하는 최대 시간
     * @param unit keepAliveTime 인수의 시간 단위
     * @param workQueue 테스크를 실행하기 전에 테스크 보관 유지에 사용하는 큐
     * @param threadFactory
     */
    public BaseThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    /**
     * 스레드가 실행되기전에 호출된다.
     */
    @Override
    protected void beforeExecute(Thread r, Runnable t) {
        super.beforeExecute(r, t);
    }

    // private void status() {
    // if (log.isDebugEnabled()) {
    // log.debug("---------------------------------------------------");
    // log.debug("- getActiveCount()); : {}" + getActiveCount());
    // log.debug("- getTaskCount()); : {}" + getTaskCount());
    // log.debug("- getLargestPoolSize()); : {}" + getLargestPoolSize());
    // log.debug("- getMaximumPoolSize()); : {}" + getMaximumPoolSize());
    // log.debug("- getPoolSize()); : {}" + getPoolSize());
    // log.debug("- getCompletedTaskCount());: {}" + getCompletedTaskCount());
    // }
    // }

    /**
     * 스레드가 종료될때 호출된다.
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

}
