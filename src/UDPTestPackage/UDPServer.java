package UDPTestPackage;

import java.io.IOException;
import java.net.*;

public class UDPServer {
    //  UDP服务端测试
    public static void main(String[] args) {
        new Thread() {
            public void run() {
                try {
                    // 1.构建DatagramSocket实例，指定本地端口
                    DatagramSocket server = new DatagramSocket(8899);
                    byte[] buf = new byte[1024];
                    // 2.构建需要收发的DatagramPacket报文--构造 DatagramPacket，用来接收长度为 length 的数据包。
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    while (true) {
                        // 收报文---从此套接字接收数据报包，此方法在接收到数据报前一直阻塞
                        server.receive(packet);
                        // 接收信息
                        String receive = new String(packet.getData(),0,packet.getLength());
                        System.out.println("来自IP:"+ packet.getAddress().getHostAddress()+"   PORT:"+packet.getPort()+"   数据:"+receive+"    数据长度:"+packet.getLength());

                        // 发报文
                        String info =  "您好! 已经收到数据了";
                        // 发送
                        DatagramPacket dp = new DatagramPacket(info.getBytes(), info.getBytes().length, packet.getAddress(), packet.getPort());
                        server.send(dp);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

