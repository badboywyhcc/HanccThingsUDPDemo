import java.io.IOException;
import java.net.*;

public class HanccThingsUDPClient {
    private final int RECEIVEBUFFERSIZE = 2048;

    public interface UDPClientCallBack{
        /**
         * 接收数据回调
         * @param packet
         */
        public void receivePacket(DatagramPacket packet);
    }

    private DatagramSocket client;
    private UDPClientCallBack callBack;

    public HanccThingsUDPClient() {
        try {
            // 创建发送端对象
            this.client = new DatagramSocket();
            // 发送
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HanccThingsUDPClient(UDPClientCallBack callBack) {
        try {
            // 创建发送端对象
            this.client = new DatagramSocket();
            this.callBack = callBack;
            byte[] receBuf = new byte[RECEIVEBUFFERSIZE];
            DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
            this.callBack.receivePacket(recePacket);
            // 发送
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送数据
     * @param buf
     * @param address
     * @param port
     */
    public void sendData(byte buf[], InetAddress address, int port){
        try {
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
            client.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        byte[] cmd8200 = new byte[]{
                0x05,                                           // 1
                0x00, 0x26,                                     // 2
                0x02,                                           // 1
                (byte)0xff,                                     // 1
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // 8
                0x01, 0x00, 0x00, 0x00, 0x00, 0x06,             // 6
                0x02, 0x00, 0x00, 0x02,                         // 4
                0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, // 8
                (byte)0x92, 0x00,                               // 2
                0x01, 0x02, 0x03, 0x04,                         // n
                (byte)0x92, 0x00                                // 2
        };

        HanccThingsUDPClient UDPClient = new HanccThingsUDPClient();
        try {
            UDPClient.sendData(cmd8200, InetAddress.getLocalHost(), 8899);
        } catch (UnknownHostException e){
        }
    }
}
