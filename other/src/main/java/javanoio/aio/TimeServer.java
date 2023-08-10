package javanoio.aio;

/**
 * @ClassName TimeServer
 * @Description TODO
 * @Author zzk
 * @Date 2023/8/3 22:14
 **/
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (Exception e){

            }
        }

    }
}
