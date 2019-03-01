import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class HanccThingsUDPServer extends Thread{
    private static final String TAG = HanccThingsUDPServer.class.getSimpleName();
    public interface UDPServerCallBack{
        public void receivePacket(DatagramPacket packet);
        public void receiveTerminate(HanccThingsUDPStatusType UDPStatus);
    }

    private  boolean isStop;
    private  DatagramSocket server;
    private  int bufferSize;
    UDPServerCallBack callBack;
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
            } catch (IOException e) {
                e.printStackTrace();
                this.callBack.receiveTerminate(HanccThingsUDPStatusType.RECEIVEEXCEPTION);
            }
        }
    }

    @Override
    public synchronized void start() {
        this.isStop = false;
        super.start();
    }

    public void stopBusiness(){
        this.isStop = true;
        this.callBack.receiveTerminate(HanccThingsUDPStatusType.RECEIVETERMINATE);
    }
}
