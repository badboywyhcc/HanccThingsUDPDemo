import sun.jvm.hotspot.runtime.Bytes;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class HanccThings5AProtocolFrameBean {
    public short   Frame_flag;            // 1 协议头
    public int     Frame_dataLen;         // 2 数据长度
    public short   Frame_version;         // 1 协议版本号
    public short   Frame_type;            // 1 协议类型
    public String  Frame_deviceEncode;    // 8 设备编码
    public String  Frame_deviceMAC;       // 6 设备mac
    public long    Frame_packetNum;       // 4 帧序
    public byte[]  Frame_reserveField;    // 8 保留字
    public int     Frame_command;         // 2 命令
    public byte[]  Frame_dataBody;        // n 数据区
    public int     Frame_CRC16;           // 2 CRC校验


    public HanccThings5AProtocolFrameBean(byte[] frameData) {
        short   flag           = desByteResult(frameData,0);
        byte[] dataLen        = desByteResult(frameData,1,2);
        System.out.println("数据总长度:"+(dataLen[0]<< 8|(short)dataLen[1]));
        short datalength = (short)((short) ((dataLen[0] << 8 | dataLen[1])&0xffff) -34);
        System.out.println("有效数据长度:"+datalength);
        byte   version        = desByteResult(frameData,3);
        byte   type           = desByteResult(frameData,4);
        byte[] deviceEncode   = desByteResult(frameData,5,8);
        byte[] deviceMAC      = desByteResult(frameData,13,6);
        byte[] packetNum      = desByteResult(frameData,19,4);
        byte[] reserveField   = desByteResult(frameData,23,8);
        byte[] command        = desByteResult(frameData,31,2);
        byte[] dataBody       = desByteResult(frameData,33,datalength);
        byte[] CRC16          = desByteResult(frameData,33+datalength,2);

        this.Frame_flag = (short) (flag&0xff);
        this.Frame_dataLen = datalength;
        this.Frame_version = (short) (version&0xff);
        this.Frame_type = (short) (type&0xff);

        this.Frame_deviceEncode = Arrays.toString(deviceEncode);
        this.Frame_deviceMAC = Arrays.toString(deviceMAC);
        this.Frame_packetNum = (long)((packetNum[0]<<24|packetNum[1]<<16|packetNum[2]<<8|packetNum[3])&0xffffffff);
        this.Frame_reserveField = reserveField;
        this.Frame_command = (int)(((command[0]<<8|command[1])&0xffff));
        this.Frame_dataBody = dataBody;
        this.Frame_CRC16 = (int)(((CRC16[0]<<8|CRC16[1])&0xffff));
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
                0x00, 0x26,                                     // 2
                0x02,                                           // 1
                (byte)0xff,                                     // 1
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // 8
                0x01, 0x00, 0x00, 0x00, 0x00, 0x06,             // 6
                0x02, 0x00, 0x00, 0x02,                         // 4
                0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, // 8
                (byte)0x92, 0x00,                               // 2
                0x01, 0x02, 0x03, 0x04,                         // n
                (byte)0x92, 0x00                                // 2
        };

        HanccThings5AProtocolFrameBean deviceBean = new HanccThings5AProtocolFrameBean(cmd8200);

        System.out.println("🍉:"+deviceBean.Frame_flag);            // 协议头
        System.out.println("🍉:"+deviceBean.Frame_dataLen);         // 数据长度
        System.out.println("🍉:"+deviceBean.Frame_version);         // 协议版本号
        System.out.println("🍉:"+deviceBean.Frame_type);            // 协议类型
        System.out.println("🍉:"+deviceBean.Frame_deviceEncode);    // 设备编码
        System.out.println("🍉:"+deviceBean.Frame_deviceMAC);       // 设备mac
        System.out.println("🍉:"+deviceBean.Frame_packetNum);       // 帧序
        System.out.println("🍉:"+Arrays.toString(deviceBean.Frame_reserveField));                // 保留字
        System.out.println("🍉命令:"+deviceBean.Frame_command);   // 命令
        System.out.println("🍉:"+Arrays.toString(deviceBean.Frame_dataBody));                    // 数据区
        System.out.println("🍉:"+deviceBean.Frame_CRC16);                                        // CRC校验

    }

}
