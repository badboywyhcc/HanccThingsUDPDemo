import HanccThingsDataConvertPackage.HanccThingsDataChannelProtocolFrameBean;
import HanccThingsDataConvertPackage.HanccThingsDataChannelProtocolFrameParse;
import HanccThingsDataFrameUtilPackage.HanccThingsStringByteArrayUtil;

import java.io.IOException;
import java.net.*;

public class HanccThingsUDPClient {
    private static final int RECEIVEBUFFERSIZE = 1024;
    private static final int SERVERPORT = 8899;

    public interface UDPClientCallBack{
        /**
         * 接收数据回调
         * @param packet
         */
        public void receivePacket(DatagramPacket packet);
    }

    public DatagramSocket client;

    public HanccThingsUDPClient() {
        try {
            // 创建发送端对象
            this.client = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 停止业务
     */
    public void stopBusiness(){
        this.client.close();
    }

    /**
     * 发送数据
     * @param buf
     * @param address
     * @param port
     */
    public void sendData(byte buf[], InetAddress address, int port,UDPClientCallBack callBack){
        try {
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
            this.client.send(sendPacket);

            /*** 接收数据***/
            byte[] receBuf = new byte[RECEIVEBUFFERSIZE];
            DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
            this.client.receive(recePacket);
            callBack.receivePacket(recePacket);

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



















    /*
     *  运行HanccThingsUDPManager 中的main方法  启动HanccThingsUDPManager业务(该业务主要是开启服务端接收并解析协议数据并返回指定协议数据)。然后 运行HanccThingsUDPClient 中的main方法启动单纯的客户端测试业务.
     *
     * */
    public static void main(String[] args) {

        HanccThingsDataChannelProtocolFrameBean deviceBean = new HanccThingsDataChannelProtocolFrameBean();
        deviceBean.Frame_flag         = 0x05;
        deviceBean.Frame_version      = 0x02;
        deviceBean.Frame_type         = 0xff;

        deviceBean.Frame_deviceEncode = "0102030405060708";
        deviceBean.Frame_deviceMAC    = "010203040506";
        deviceBean.Frame_packetNum    = 33554434;
        deviceBean.Frame_reserveField = new byte[8];
        deviceBean.Frame_command      = 0x9200;
        deviceBean.Frame_dataBody     = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] desData = HanccThingsDataChannelProtocolFrameParse.parseFrameBean(deviceBean);


        /**
         * 初始化UDPClient
         */
        HanccThingsUDPClient UDPClient = new HanccThingsUDPClient();
        try {
            UDPClient.sendData(desData, InetAddress.getLocalHost(), SERVERPORT,new HanccThingsUDPClient.UDPClientCallBack(){
                @Override
                public void receivePacket(DatagramPacket packet) {
                    String receStr = new String(packet.getData(), 0 , packet.getLength());
                    System.out.println("来自服务端IP:"+packet.getAddress()+"    端口:"+packet.getPort()+"    回应长度:"+packet.getLength()+"       数据:"+ HanccThingsStringByteArrayUtil.ByteToHexString(packet));

                    /* 在此接收完毕关闭监听 */
                    UDPClient.stopBusiness();
                }
            });

        } catch (UnknownHostException e){
            e.printStackTrace();
        }
    }
}
