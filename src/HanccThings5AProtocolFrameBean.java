import sun.jvm.hotspot.runtime.Bytes;

public class HanccThings5AProtocolFrameBean {
    public byte    Frame_flag;          // 协议头
    public byte[]  Frame_dataLen        = new byte[1];  // 数据长度
    public byte    Frame_version;                       // 协议版本号
    public byte    Frame_type;                          // 协议类型
    public byte[]  Frame_deviceEncode   = new byte[1];  // 设备编码
    public byte[]  Frame_deviceMAC      = new byte[1];  // 设备mac
    public byte[]  Frame_packetNum      = new byte[1];  // 帧序
    public byte[]  Frame_reserveField   = new byte[1];  // 保留字
    public byte[]  Frame_command        = new byte[1];  // 命令
    public byte[]  Frame_dataBody;                      // 数据区
    public byte[]  Frame_CRC16          = new byte[1];  // CRC校验

    public HanccThings5AProtocolFrameBean(byte[] frameData) {
//        Frame_flag          = (byte) desBytes(frameData,0,1)[0];
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
}
