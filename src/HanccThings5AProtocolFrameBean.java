import sun.jvm.hotspot.runtime.Bytes;

import java.util.Arrays;

public class HanccThings5AProtocolFrameBean {
    public short   Frame_flag;            // 协议头
    public short   Frame_dataLen;         // 数据长度
    public short   Frame_version;         // 协议版本号
    public short   Frame_type;            // 协议类型
    public String  Frame_deviceEncode;    // 设备编码
    public String  Frame_deviceMAC;       // 设备mac
    public int     Frame_packetNum;       // 帧序
    public byte[]  Frame_reserveField;    // 保留字
    public int     Frame_command;         // 命令
    public byte[]  Frame_dataBody;        // 数据区
    public int     Frame_CRC16;           // CRC校验



    public HanccThings5AProtocolFrameBean(byte[] frameData) {
        short   flag           = desByteResult(frameData,0);
        byte[] dataLen        = desByteResult(frameData,1,2);
        short datalength = (short) ((short) ((short)dataLen[0]<< 8|(short)dataLen[1]) - 34 -1);
        byte   version        = desByteResult(frameData,3);
        byte   type           = desByteResult(frameData,4);
        byte[] deviceEncode   = desByteResult(frameData,5,8);
        byte[] deviceMAC      = desByteResult(frameData,13,6);
        byte[] packetNum      = desByteResult(frameData,19,4);
        byte[] reserveField   = desByteResult(frameData,23,8);
        byte[] command        = desByteResult(frameData,31,2);
        byte[] dataBody       = desByteResult(frameData,33,datalength);
        byte[] CRC16          = desByteResult(frameData,35,2);

        System.out.println("🏖:"+Arrays.toString(command));
        this.Frame_flag = flag;
        this.Frame_dataLen = datalength;
        this.Frame_version = version;
        this.Frame_type = type;

        this.Frame_deviceEncode = Arrays.toString(deviceEncode);
        this.Frame_deviceMAC = Arrays.toString(deviceMAC);
        this.Frame_packetNum = packetNum[0]<<24|packetNum[1]<<16|packetNum[2]<<8|packetNum[3];
        this.Frame_reserveField = reserveField;
        this.Frame_command = command[0]<<8|command[1];
        this.Frame_dataBody = dataBody;
        this.Frame_CRC16 = CRC16[0]<<8|CRC16[1];
    }

    private byte[] desByteResult(byte[] data,int start,int offset){
        byte[] resultBytes = new byte[offset];
        for (int i = 0;i < offset;i++) {
            resultBytes[i] = data[start + i];
        }
        return resultBytes;
    }
    private byte desByteResult(byte[] data,int index){
        byte resultByte = data[index];
        return resultByte;
    }

    public static void main(String[] args) {
        byte[] cmd8200 = new byte[]{
                0x05,                                           // 1
                0x00, 0x25,                                     // 2
                0x02,                                           // 1
                0x01,                                           // 1
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // 8
                0x01, 0x00, 0x00, 0x00, 0x00, 0x06,             // 6
                0x01, 0x00, 0x00, 0x02,                         // 4
                0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, // 8
                0x09, 0x00,                                     // 2
                0x01, 0x03,                                     // n
                0x00, 0x01                                      // 2
        };
        HanccThings5AProtocolFrameBean deviceBean = new HanccThings5AProtocolFrameBean(cmd8200);

        System.out.println("🍉:"+deviceBean.Frame_flag);            // 协议头
        System.out.println("🍉:"+deviceBean.Frame_dataLen);         // 数据长度
        System.out.println("🍉:"+deviceBean.Frame_version);         // 协议版本号
        System.out.println("🍉:"+deviceBean.Frame_type);            // 协议类型
        System.out.println("🍉:"+deviceBean.Frame_deviceEncode);    // 设备编码
        System.out.println("🍉:"+deviceBean.Frame_deviceMAC);       // 设备mac
        System.out.println("🍉:"+deviceBean.Frame_packetNum);       // 帧序
        System.out.println("🍉:"+Arrays.toString(deviceBean.Frame_reserveField));    // 保留字
        System.out.println("🍉:"+deviceBean.Frame_command);         // 命令
        System.out.println("🍉:"+Arrays.toString(deviceBean.Frame_dataBody));        // 数据区
        System.out.println("🍉:"+deviceBean.Frame_CRC16);           // CRC校验
    }
}
