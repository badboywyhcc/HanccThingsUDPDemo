import HanccThingsDataConvertPackage.HanccThingsDataChannelProtocolFrameBean;
import HanccThingsDataConvertPackage.HanccThingsDataChannelProtocolFrameParse;

import java.net.DatagramPacket;

public class HanccThingsUDPManager {

    private HanccThingsUDPServer UDPServer;
    private static final String TAG = HanccThingsUDPManager.class.getSimpleName();
    private static final int    SERVERPORT = 8899;
    private static final int    BUFFERSIZE = 1024;

    private static HanccThingsUDPManager UDPManager = null;

    /**
     * 单例
     * @return
     */
    public static HanccThingsUDPManager shareInstance() {
        if (UDPManager == null) {
            synchronized (HanccThingsUDPServer.class) {
                if (UDPManager == null) {
                    UDPManager = new HanccThingsUDPManager();
                }
            }
        }
        return UDPManager;
    }

    /**
     * 构造函数
     */
    private HanccThingsUDPManager() {
        initBusiness();
    }

    /**
     * 初始化业务
     */
    public void initBusiness(){

            // 初始化 UDP 服务端
            this.UDPServer = new HanccThingsUDPServer(SERVERPORT,BUFFERSIZE,new HanccThingsUDPServer.UDPServerCallBack(){
                // 返回数据回调
                @Override
                public void receivePacket(DatagramPacket packet){
                    if (true == parseFrameFlag(packet)){
                        parseFrameData(packet);
                    }else{
                        System.out.println("非标协议格式");
                    }
                }
                // 接收数据终止回调
                @Override
                public void receiveTerminate(HanccThingsUDPStatusType UDPStatusType) {
                    System.out.println(UDPStatusType.getCode()+UDPStatusType.msg);
                }
            });
    }

    /**
     * 判断前导码
     * @param packet
     * @return
     */
    public  boolean parseFrameFlag(DatagramPacket packet){
        byte[] buf = packet.getData();
        if (buf[0] == 0x05) {
            return true;
        }else{
            return false;
        }
    }
    /**
     * 处理协议数据
     * @param packet
     */
    public void parseFrameData(DatagramPacket packet){
        HanccThingsDataChannelProtocolFrameBean deviceFrameBean = HanccThingsDataChannelProtocolFrameParse.parseFrameData(packet.getData());
        // System.out.println("要回数据了");
        // 发报文
        // String info =  "您好! 已经收到数据了";
        // this.UDPTestPackage.UDPServer.sendData(info.getBytes(), packet.getAddress(), packet.getPort());

        HanccThingsDataChannelProtocolFrameBean deviceBean = new HanccThingsDataChannelProtocolFrameBean();
        deviceBean.Frame_flag         = 0x05;
        deviceBean.Frame_version      = 0x02;
        deviceBean.Frame_type         = 0xff;

        deviceBean.Frame_deviceEncode = "0102030405060708";
        deviceBean.Frame_deviceMAC    = "010203040506";
        deviceBean.Frame_packetNum    = 33554434;
        deviceBean.Frame_reserveField = new byte[8];
        deviceBean.Frame_command      = 0x9100;
        deviceBean.Frame_dataBody     = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] desData = HanccThingsDataChannelProtocolFrameParse.parseFrameBean(deviceBean);

        this.UDPServer.sendData(desData, packet.getAddress(), packet.getPort());
    }

    /**
     * 开始业务
     */
    public void startBusiness(){
        System.out.println("开始业务");
        this.UDPServer.start();
    }

    /**
     * 停止业务
     */
    public void stopBusiness(){
        System.out.println("停止业务");
        this.UDPServer.stopBusiness();
    }

























    /*
     *  运行HanccThingsUDPManager 中的main方法  启动HanccThingsUDPManager业务(该业务主要是开启服务端接收并解析协议数据并返回指定协议数据)。然后 运行HanccThingsUDPClient 中的main方法启动单纯的客户端测试业务.
     *
     * */
    public static void main(String[] args) {
        HanccThingsUDPManager.shareInstance().startBusiness();
    }
}
