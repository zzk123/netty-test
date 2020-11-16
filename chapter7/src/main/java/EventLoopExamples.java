import java.util.Collections;
import java.util.List;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-30
 */
public class EventLoopExamples {

    public static void executeTaskInEventLoop(){
        boolean terminated = true;

        while(!terminated){
            List<Runnable> readyEvents = blockUntilEventsReady();
            for(Runnable ev : readyEvents){
                ev.run();
            }
        }
    }

    private static final List<Runnable> blockUntilEventsReady(){
        return Collections.<Runnable>singletonList(
                new Runnable(){
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
