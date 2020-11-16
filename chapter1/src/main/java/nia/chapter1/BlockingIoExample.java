package nia.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: netty-test
 * @description: 阻塞I/O示例，只能处理一个连接
 * @author: zzk
 * @create: 2020-09-22
 */
public class BlockingIoExample {

    public void serve(int portNumber) throws IOException{
        //创建一个新的ServerSocket用以监听指定端口上的连接请求
        ServerSocket serverSocket = new ServerSocket(portNumber);
        //accept()方法的调用将被阻塞，直到一个连接建立
        Socket clientSocket = serverSocket.accept();
        //流对象源于套接字的流对象
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        String request, response;
        while((request = in.readLine()) != null){
            if("Done".equals(request)){
                break;
            }
            response = processRequest(request);
            out.println(response);
        }
    }

    private String processRequest(String request){
        return "Processed";
    }
}
