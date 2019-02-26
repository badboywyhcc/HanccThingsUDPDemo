import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

public class UDPClient {

    public static void main(String[] args) {
        try{
            /*
             * UDP基本的流程：
             *  1 创建Socket
             *  2 准备要发送的数据
             *  3 准备远端计算机地址信息
             *  4 打包（设置数据 设置地址）
             *  5 通过Socket将包发送至远端
             *  若学要再次发送数据 重复2-5
             */
//1
            DatagramSocket socket=new DatagramSocket();
//2
            String message="你好服务端";
            byte[] data=message.getBytes("UTF-8");
//3
            InetAddress address=InetAddress.getByName("localhost");
            /*
             * TCP协议的端口与UDO协议的端口是不冲突的
             */
            int port=8899;
//4
            /*数据所在 数组
             * 数组长度
             * 远端地址
             * 远端端口
             *
             */
            DatagramPacket packet=new  DatagramPacket(data,data.length,address,port);
//发送
            socket.send(packet);
            /*
             * 接收服务端发送回来的信息
             */
            data=new byte[1000];
            packet=new DatagramPacket(data, data.length);
//3
            socket.receive(packet);
//4
            message=new String(data,0,packet.getLength(),"Utf-8");
            System.out.println("服务端说："+message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
