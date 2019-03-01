import java.net.DatagramPacket;

public class HanccThingsDataFrameParse {
    private static final String TAG = HanccThingsUDPManager.class.getSimpleName();

//    private static HanccThingsDataFrameParse HanccThingsDataFrameParseManager = null;
//    public static HanccThingsDataFrameParse shareInstance(){
//        if (HanccThingsDataFrameParseManager == null){
//            synchronized (HanccThingsDataFrameParse.class){
//                if (HanccThingsDataFrameParseManager == null) {
//                    HanccThingsDataFrameParseManager = new HanccThingsDataFrameParse();
//                }
//            }
//        }
//        return HanccThingsDataFrameParseManager;
//    }
//
//    public HanccThingsDataFrameParse() {
//
//    }

    public static boolean parseFrameFlag(DatagramPacket packet){
        byte[] buf = packet.getData();
        if (buf[0] == 0x05) {
            return true;
        }else{
            return false;
        }
    }
    public static void parseFrameData(DatagramPacket packet){
        String receiveStr = bytesToHexString(packet);
        HanccThingsLogger.Debug(TAG,"来自IP:"+ packet.getAddress().getHostAddress()+"   PORT:"+packet.getPort()+"   数据:"+receiveStr+"    数据长度:"+packet.getLength());

        HanccThings5AProtocolFrameBean deviceFrameBean = new HanccThings5AProtocolFrameBean(packet.getData());
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
