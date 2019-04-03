import HanccThingsDataFrameUtilPackage.HanccThingsStringByteArrayUtil;

import java.io.IOException;
import java.net.*;

/**
 * 接收状态
 */
enum HanccThingsUDPStatusType {

    RECEIVEEXCEPTION(10000,"接收数据异常"),
    RECEIVETERMINATE(10002,"接收数据终止");

    public int code;
    public String msg;
    HanccThingsUDPStatusType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "HanccThingsUDPStatusType{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }


    public static void main(String[] args) {
        System.out.println(HanccThingsUDPStatusType.RECEIVETERMINATE.getMsg());
    }
}





















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
     * @param port
     * @param bufferSize
     * @param callBack
     */
    public HanccThingsUDPServer(int port,int bufferSize,UDPServerCallBack callBack) {
        try {
            this.server = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

                String receiveStr = HanccThingsStringByteArrayUtil.ByteToHexString(packet);
                System.out.println("来自IP:"+ packet.getAddress().getHostAddress()+"   PORT:"+packet.getPort()+"   数据:"+receiveStr+"    数据长度:"+packet.getLength());
            } catch (IOException e) {
                e.printStackTrace();
                this.callBack.receiveTerminate(HanccThingsUDPStatusType.RECEIVEEXCEPTION);
            }
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
            this.server.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
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
}
