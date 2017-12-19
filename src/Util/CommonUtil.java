package Util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class CommonUtil {

    private static CommonUtil commonUtil;

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
     * */
    public void startThread(Runnable runnable) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("mdns-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MICROSECONDS,
                new LinkedBlockingDeque<>(1024),
                threadFactory, new ThreadPoolExecutor.AbortPolicy()
        );
        singleThreadPool.execute(runnable);
        singleThreadPool.shutdown();
    }
}
