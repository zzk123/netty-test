import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-24
 */
public class PlainOioServer {

    /**
     * 未使用Netty的阻塞网络编程
     * @param port
     * @throws IOException
     */
    public void serve(int port) throws IOException{
        final ServerSocket socket = new ServerSocket(port);
        try{
            for(;;){
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try{
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            clientSocket.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }finally{
                            try{
                                clientSocket.close();
                            }catch (IOException ex){

                            }
                        }
                    }
                }).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
