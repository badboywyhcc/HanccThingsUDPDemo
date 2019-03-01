import sun.jvm.hotspot.runtime.Bytes;

public class HanccThings5AProtocolFrameBean {
    public short    Frame_flag;          // 协议头
    public short[]  Frame_dataLen        = new short[2];  // 数据长度
    public short    Frame_version;                        // 协议版本号
    public short    Frame_type;                           // 协议类型
    public short[]  Frame_deviceEncode   = new short[8];  // 设备编码
    public short[]  Frame_deviceMAC      = new short[6];  // 设备mac
    public short[]  Frame_packetNum      = new short[4];  // 帧序
    public short[]  Frame_reserveField   = new short[8];  // 保留字
    public short[]  Frame_command        = new short[2];  // 命令
    public short[]  Frame_dataBody;                       // 数据区
    public short[]  Frame_CRC16          = new short[2];  // CRC校验

    public HanccThings5AProtocolFrameBean(byte[] frameData) {
        Frame_flag          = (byte) desBytes(frameData,0,1)[0];
//        Frame_dataLen        = ;
//        Frame_version;
//        Frame_type;
//        Frame_deviceEncode   = new byte[1];
//        Frame_deviceMAC      = new byte[1];
//        Frame_packetNum      = new byte[1];
//        Frame_reserveField   = new byte[1];
//        Frame_command        = new byte[1];
//        Frame_dataBody;
//        Frame_CRC16          = new byte[1];
    }

    public byte[] desBytes(byte[] data,int start,int offset){
        byte[] resultBytes = new byte[offset];
        for (int i = 0;i < offset;i++){
            resultBytes[i] = data[start +offset];
        }
        return resultBytes;
    }

    public static void main(String[] args) {
        short[] cmd8200 = new short[]{
                0x05,                                           // 1
                0x04, 0x03,                                     // 2
                0x02,                                           // 1
                0x01,                                           // 1
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 8
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00,             // 6
                0x00, 0x00, 0x00, 0x00,                         // 4
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 8
                0x10, 0x00,                                     // 2
                0x01,                                           // 1
                0x00, 0x01                                      // 2
        };
    }
}
