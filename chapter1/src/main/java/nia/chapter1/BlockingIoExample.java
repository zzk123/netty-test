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

    /**
     * 服务端代码
     * @param portNumber
     * @throws IOException
     */
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
            //客户端返回 done 关闭连接
            response = processRequest(request);
            out.println(response);
        }
    }

    /**
     * 响应字符
     * @param request
     * @return
     */
    private String processRequest(String request){
        return "Processed";
    }

    public static void main(String[] args) throws Exception {
        BlockingIoExample example = new BlockingIoExample();
        example.serve(25 );

    }
}
