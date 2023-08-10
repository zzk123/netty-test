package javanoio.bio;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName TimeServer
 * @Description TODO
 * @Author zzk
 * @Date 2023/7/28 21:57
 **/
public class TimeServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (Exception exception){
                
            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
            while (true) {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        }finally {
            if(server != null){
                server.close();
                server = null;
            }
        }
    }
}
