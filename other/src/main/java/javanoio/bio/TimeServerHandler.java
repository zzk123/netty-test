package javanoio.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @ClassName TimeServerHandler
 * @Description TODO
 * @Author zzk
 * @Date 2023/7/28 22:05
 **/
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while(true){
                body = in.readLine();
                if(body == null){
                    break;
                }
                System.out.println("body：" + body);
            }
        }catch (Exception e){
            if(in != null){
                try{
                    in.close();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                if(out != null){
                    out.close();
                    out = null;
                }
                if(this.socket != null){
                    try {
                        this.socket.close();
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                    this.socket = null;
                }
            }
        }
    }
}
