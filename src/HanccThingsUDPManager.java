import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class HanccThingsUDPManager {

    private HanccThingsUDPServer UDPServer;
    private static final String TAG = HanccThingsUDPManager.class.getSimpleName();
    private static final int    SERVERPORT = 8899;
    private static final int    BUFFERSIZE = 256;

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
        try {
            DatagramSocket serverSocket = new DatagramSocket(SERVERPORT);
            // 初始化 UDP 服务端
            this.UDPServer = new HanccThingsUDPServer(serverSocket,BUFFERSIZE,new HanccThingsUDPServer.UDPServerCallBack(){
                // 返回数据回调
                @Override
                public void receivePacket(DatagramPacket packet){
                    if (true == HanccThingsDataFrameParse.parseFrameFlag(packet)){
                        parseFrameData(packet);
                    }else{
                        System.out.println("非标协议格式");
                    }
                }
                // 接收数据终止回调
                @Override
                public void receiveTerminate(HanccThingsUDPStatusType UDPStatusType) {
                    HanccThingsLogger.Debug(TAG,UDPStatusType.getCode()+UDPStatusType.msg);
                }
            });
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理协议数据
     * @param packet
     */
    public void parseFrameData(DatagramPacket packet){
        HanccThings5AProtocolFrameBean deviceFrameBean = HanccThingsDataFrameParse.parseFrameData(packet);
        System.out.println("🍉:"+deviceFrameBean.Frame_flag);            // 协议头
        System.out.println("🍉:"+deviceFrameBean.Frame_dataLen);         // 数据长度
        System.out.println("🍉:"+deviceFrameBean.Frame_version);         // 协议版本号
        System.out.println("🌞:"+deviceFrameBean.Frame_type);            // 协议类型
        System.out.println("🍉:"+deviceFrameBean.Frame_deviceEncode);    // 设备编码
        System.out.println("🍉:"+deviceFrameBean.Frame_deviceMAC);       // 设备mac
        System.out.println("🍉:"+deviceFrameBean.Frame_packetNum);       // 帧序
        System.out.println("🍉:"+ Arrays.toString(deviceFrameBean.Frame_reserveField));                // 保留字
        System.out.println("🍉命令:"+deviceFrameBean.Frame_command);     // 命令
        System.out.println("🍉:"+Arrays.toString(deviceFrameBean.Frame_dataBody));                    // 数据区
        System.out.println("🍉:"+deviceFrameBean.Frame_CRC16);
    }

    /**
     * 开始业务
     */
    public void startBusiness(){
        HanccThingsLogger.Debug(TAG,"开始业务");
        this.UDPServer.start();
    }

    /**
     * 停止业务
     */
    public void stopBusiness(){
        HanccThingsLogger.Debug(TAG,"停止业务");
        this.UDPServer.stopBusiness();
    }
}
