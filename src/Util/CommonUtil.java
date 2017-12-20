package Util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class CommonUtil {

    private static final int ProcessorNum = Runtime.getRuntime().availableProcessors();
    private static CommonUtil commonUtil;
    private static ExecutorService fixThreadPool = newFixedThreadPool(ProcessorNum * 4 + 1);
    private static ExecutorService cacheThreadPool;
    public static final int SLEEPTIME = 30;

    private CommonUtil() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("mdns-pool-%d")
                .build();
        cacheThreadPool = newCachedThreadPool(threadFactory);
    }

    public static CommonUtil getInstance() {
        if (commonUtil == null) {
            synchronized (CommonUtil.class) {
                if (commonUtil == null) {
                    commonUtil = new CommonUtil();
                }
            }
        }
        return commonUtil;
    }

    /**
     * 单个线程
     */
    public void startSingleThread(Runnable runnable) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("mdns-pool-%d")
                .build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MICROSECONDS,
                new LinkedBlockingDeque<>(1024),
                threadFactory, new ThreadPoolExecutor.AbortPolicy()
        );
        singleThreadPool.execute(runnable);
        singleThreadPool.shutdown();
    }

    /**
     * Fixed线程
     */
    public void startFixedThread(Runnable runnable) {
        fixThreadPool.execute(runnable);
    }

    /**
     * Cached线程
     */
    public void startCachedThread(Runnable runnable) {
        cacheThreadPool.execute(runnable);
    }

    public void closeFixedThreadPool() {
        fixThreadPool.shutdown();
    }

    public void closeCachedThreadPool() {
        cacheThreadPool.shutdown();
    }
}
