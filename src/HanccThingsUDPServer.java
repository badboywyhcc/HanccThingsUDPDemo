import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class HanccThingsUDPServer extends Thread{
    private static final String TAG = HanccThingsUDPServer.class.getSimpleName();
    public interface UDPServerCallBack{
        /**
         * 接收数据回调
         * @param packet
         */
        public void receivePacket(DatagramPacket packet);

        /**
         * 接收终止回调
         * @param UDPStatus
         */
        public void receiveTerminate(HanccThingsUDPStatusType UDPStatus);
    }

    private  boolean isStop;
    private  DatagramSocket server;
    private  int bufferSize;
    UDPServerCallBack callBack;

    /**
     * UDP服务端初始化
     * @param server
     * @param bufferSize
     * @param callBack
     */
    public HanccThingsUDPServer(DatagramSocket server,int bufferSize,UDPServerCallBack callBack) {
        this.server = server;
        this.bufferSize = bufferSize;
        this.callBack = callBack;
        this.isStop = false;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[bufferSize];
        // 构建需要收发的DatagramPacket报文--构造 DatagramPacket，用来接收长度为 length 的数据包。
        DatagramPacket packet = new DatagramPacket(receiveBuffer,receiveBuffer.length);
        while (!isStop) {
            try {
                // 收报文---从此套接字接收数据报包，此方法在接收到数据报前一直阻塞
                server.receive(packet);
                this.callBack.receivePacket(packet);

                String receiveStr = bytesToHexString(packet);
                HanccThingsLogger.Debug(TAG,"来自IP:"+ packet.getAddress().getHostAddress()+"   PORT:"+packet.getPort()+"   数据:"+receiveStr+"    数据长度:"+packet.getLength());
            } catch (IOException e) {
                e.printStackTrace();
                this.callBack.receiveTerminate(HanccThingsUDPStatusType.RECEIVEEXCEPTION);
            }
        }
    }

    /**
     * 重新开始方法
     */
    @Override
    public synchronized void start() {
        this.isStop = false;
        super.start();
    }

    /**
     * 停止业务
     */
    public void stopBusiness(){
        this.isStop = true;
        this.callBack.receiveTerminate(HanccThingsUDPStatusType.RECEIVETERMINATE);
    }

    /**
     * 数组转换成十六进制字符串
     */
    public static final String bytesToHexString(DatagramPacket packet) {
        byte[] bArray = packet.getData();
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        String resultStr = sb.toString().substring(0,packet.getLength()*2);
        return resultStr;
    }
}
