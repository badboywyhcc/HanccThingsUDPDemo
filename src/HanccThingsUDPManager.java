import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class HanccThingsUDPManager {

    private HanccThingsUDPServer UDPServer;
    private static final String TAG = HanccThingsUDPManager.class.getSimpleName();
    private static final int    SERVERPORT = 8899;
    private static final int    BUFFERSIZE = 256;


    private static HanccThingsUDPManager UDPManager = null;
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

    private HanccThingsUDPManager() {
        initBusiness();
    }

    public void initBusiness(){
        try {
            DatagramSocket serverSocket = new DatagramSocket(SERVERPORT);
            UDPServer = new HanccThingsUDPServer(serverSocket,BUFFERSIZE,new HanccThingsUDPServer.UDPServerCallBack(){
                @Override
                public void receivePacket(DatagramPacket packet){
                    if (true == HanccThingsDataFrameParse.parseFrameFlag(packet)){
                        parseFrameData(packet);
                    }
                }
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
    public void parseFrameData(DatagramPacket packet){
        HanccThingsDataFrameParse.parseFrameData(packet);
    }

    public void startBusiness(){
        HanccThingsLogger.Debug(TAG,"开始业务");
        UDPServer.start();
    }
    public void stopBusiness(){
        HanccThingsLogger.Debug(TAG,"停止业务");
        UDPServer.stopBusiness();
    }
}
