package Util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class CommonUtil {

    // 定义显示屏幕的宽度和高度
    public static final int screenWidth = 800;
    public static final int screenHeight = 700;
    // 定义坦克的宽度和长度
    public final static int size = 35;
    public static final int SLEEPTIME = 30;
    // 定义坦克运动范围
    public static final int rangX = screenWidth - size;
    public static final int rangY = screenHeight - size;
    private static final int ProcessorNum = Runtime.getRuntime().availableProcessors();
    private static CommonUtil commonUtil;
    private static ExecutorService fixThreadPool = newFixedThreadPool(ProcessorNum * 4 + 1);
    private static ExecutorService cacheThreadPool;

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
