import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-30
 */
public class ScheduleExamples {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * jdk
     * 使用ScheduledExecutorService调度任务
     */
    public static void schedule(){
        ScheduledExecutorService executorService =
                Executors.newScheduledThreadPool(10);

        ScheduledFuture<?> future = executorService.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Now it is 60 seconds later");
                    }
                }, 60, TimeUnit.SECONDS);
        executorService.shutdown();
    }


    /**
     * 使用EventLoop调度任务
     */
    public static void scheduleViaEventLoop(){
        Channel ch = CHANNEL_FROM_SOMEWHERE;
        ScheduledFuture<?> future = ch.eventLoop().schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("60 seconds later");
                    }
                }, 60, TimeUnit.SECONDS);
    }

    /**
     * 取消任务
     */
    public static void cancelingTaskUsingScheduledFuture(){
        Channel ch = CHANNEL_FROM_SOMEWHERE;
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Run every 60 seconds");
                    }
                }, 60, 60, TimeUnit.SECONDS);
        // 其他步骤
        boolean mayInterruptIfRunning = false;
        future.cancel(mayInterruptIfRunning);
    }

}
