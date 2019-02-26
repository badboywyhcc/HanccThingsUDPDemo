import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.xml.crypto.Data;

public class UDPSend {
	public static void main(String[] args) throws Exception{
		String transData = "你好啊，龟仙人";
		// 创建发送端对象
		DatagramSocket send = new DatagramSocket(10010);
		// 发送
		DatagramPacket dp = new DatagramPacket(transData.getBytes(), transData.getBytes().length, InetAddress.getLocalHost(), 10086);
		send.send(dp);
		send.close();
	}
}
