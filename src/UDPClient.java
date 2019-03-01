import java.io.IOException;
import java.net.*;

public class UDPClient {

    public static void main(String[] args) {
        try {
            byte[] transDataByte = {0x05,0x04,0x03,0x02,0x01,0x00,0x05,0x04,0x03,0x02,0x01,0x00};
            String transData = "你好龟仙人";
            // 创建发送端对象
            DatagramSocket client = new DatagramSocket();
            // 发送
//            DatagramPacket dp = new DatagramPacket(transData.getBytes(), transData.getBytes().length, InetAddress.getLocalHost(), 8899);
            DatagramPacket dp = new DatagramPacket(transDataByte, transDataByte.length, InetAddress.getLocalHost(), 8899);
            client.send(dp);

//            /*** 接收数据***/
//            byte[] receBuf = new byte[1024];
//            DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
//            client.receive(recePacket);
//
//            String receStr = new String(recePacket.getData(), 0 , recePacket.getLength());
//            System.out.println("客户端收到回应:" + receStr);
//            System.out.println(recePacket.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//			// 关闭socket
//			if(send != null){
//				send.close();
//			}
        }
    }
}
