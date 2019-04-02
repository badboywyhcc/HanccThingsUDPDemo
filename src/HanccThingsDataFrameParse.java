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
    public static HanccThings5AProtocolFrameBean parseFrameData(DatagramPacket packet){

        HanccThings5AProtocolFrameBean deviceFrameBean = new HanccThings5AProtocolFrameBean(packet.getData());
        return deviceFrameBean;
    }

}
