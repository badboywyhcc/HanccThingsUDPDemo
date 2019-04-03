package HanccThingsDataConvertPackage;

import HanccThingsDataFrameUtilPackage.HanccThingsCRCUtil;
import HanccThingsDataFrameUtilPackage.HanccThingsStringByteArrayUtil;

import java.util.Arrays;

public class HanccThingsDataChannelProtocolFrameParse {
    /**
     * 从源数据指定位置开始取出指定长度字节
     * @param data
     * @param start
     * @param offset
     * @return
     */
    public static byte[] desByteResult(byte[] data,int start,int offset){
        byte[] resultBytes = new byte[offset];
        for (int i = 0;i < offset;i++) {
            resultBytes[i] = data[start + i];
        }
        return resultBytes;
    }

    /**
     * 从源数据指定位置取开始出指定1字节
     * @param data
     * @param index
     * @return
     */
    public static byte desByteResult(byte[] data,int index){
        byte resultByte = data[index];
        return resultByte;
    }

    /**
     * 数据通道原始数据转模型
     * @param packetData 数据通道原始数据
     * @return
     */
    public static HanccThingsDataChannelProtocolFrameBean parseFrameData(byte[] packetData){

        HanccThingsDataChannelProtocolFrameBean deviceFrameBean = new HanccThingsDataChannelProtocolFrameBean();
        byte   flag           = desByteResult(packetData,0);
        byte[] dataLen        = desByteResult(packetData,1,2);
        int packetTotalLen    = Byte.toUnsignedInt(dataLen[0])<< 8|Byte.toUnsignedInt(dataLen[1]);
        int datalength        = packetTotalLen -34;
//        System.out.println("数据总长度:"+packetTotalLen+"   有效数据长度:"+datalength);
        byte   version        = desByteResult(packetData,3);
        byte   type           = desByteResult(packetData,4);
        byte[] deviceEncode   = desByteResult(packetData,5,8);
        byte[] deviceMAC      = desByteResult(packetData,13,6);
        byte[] packetNum      = desByteResult(packetData,19,4);
        byte[] reserveField   = desByteResult(packetData,23,8);
        byte[] command        = desByteResult(packetData,31,2);
        byte[] dataBody       = desByteResult(packetData,33,datalength);
        byte[] CRC16          = desByteResult(packetData,33+datalength,2);

        deviceFrameBean.Frame_flag         = Byte.toUnsignedInt(flag);
        deviceFrameBean.Frame_version      = Byte.toUnsignedInt(version);
        deviceFrameBean.Frame_type         = Byte.toUnsignedInt(type);

        deviceFrameBean.Frame_deviceEncode = HanccThingsStringByteArrayUtil.ByteToHexString(deviceEncode);
        deviceFrameBean.Frame_deviceMAC    = HanccThingsStringByteArrayUtil.ByteToHexString(deviceMAC);
        deviceFrameBean.Frame_packetNum    = (long)((Byte.toUnsignedInt(packetNum[0])<<24|Byte.toUnsignedInt(packetNum[1])<<16|Byte.toUnsignedInt(packetNum[2])<<8|Byte.toUnsignedInt(packetNum[3])));
        deviceFrameBean.Frame_reserveField = reserveField;
        deviceFrameBean.Frame_command      = Byte.toUnsignedInt(command[0])<<8|Byte.toUnsignedInt(command[1]);
        deviceFrameBean.Frame_dataBody     = dataBody;
        deviceFrameBean.Frame_CRC16        = Byte.toUnsignedInt(CRC16[0])<<8|Byte.toUnsignedInt(CRC16[1]);

        return deviceFrameBean;
    }

    /**
     * 模型转数据通道原始数据
     * @param frameBean 模型
     * @return
     */
    public static byte[] parseFrameBean(HanccThingsDataChannelProtocolFrameBean frameBean){

        // 协议头
        byte[]   flag                = new byte[1];
        flag[0]                      = (byte) frameBean.Frame_flag;

        // 协议版本号
        byte[]   version             = new byte[1];
        version[0]                   = (byte)frameBean.Frame_version;

        // 协议类型
        byte[]   type                = new byte[1];
        type[0]                      = (byte)frameBean.Frame_type;

        // 设备编码
        byte[] deviceEncode          = HanccThingsStringByteArrayUtil.HexStringToByte(frameBean.Frame_deviceEncode);

        // mac地址
        byte[] deviceMAC             = HanccThingsStringByteArrayUtil.HexStringToByte(frameBean.Frame_deviceMAC);

        // 帧序
        byte[]   packetNum           = new byte[4];
        packetNum[0]                      = (byte)((frameBean.Frame_packetNum & 0xff000000) >> 24);
        packetNum[1]                      = (byte)((frameBean.Frame_packetNum & 0x00ff0000) >> 16);
        packetNum[2]                      = (byte)((frameBean.Frame_packetNum & 0x0000ff00) >> 8);
        packetNum[3]                      = (byte)(frameBean.Frame_packetNum  & 0x000000ff);

        // 保留字
        byte[] reserveField          = frameBean.Frame_reserveField;

        // 命令
        byte[] command               = new byte[2];
        command[0]                   = (byte) ((frameBean.Frame_command&0XFF00) >> 8);
        command[1]                   = (byte) (frameBean.Frame_command&0X00FF);

        // 总数据长度
        byte[] packetTotalLen        = new byte[2];
        packetTotalLen[0]            = (byte) (((frameBean.Frame_dataBody.length +34)&0XFF00) >> 8);
        packetTotalLen[1]            = (byte) ((frameBean.Frame_dataBody.length +34)&0X00FF);

        byte[] dataBody              = frameBean.Frame_dataBody;


        byte[] src_CRCData = byteMergerAll(packetTotalLen,version,type,deviceEncode,deviceMAC,packetNum,reserveField,command,dataBody);
        byte[] CRC16       = HanccThingsCRCUtil.CRC16_Type1(src_CRCData,src_CRCData.length);
        int crcValue       = (int)((Byte.toUnsignedInt(CRC16[0]) << 8)| (Byte.toUnsignedInt(CRC16[1]) &0xff));
        // System.out.println("CRC数组总包:"+HanccThingsDataFrameUtilPackage.HanccThingsStringByteArrayUtil.ByteToHexString(src_CRCData)+"    校验数据长度:"+src_CRCData.length+"   CRC:"+Integer.toHexString(crcValue));

        byte[] packetData = byteMergerAll(flag,packetTotalLen,version,type,deviceEncode,deviceMAC,packetNum,reserveField,command,dataBody,CRC16);
        // System.out.println("数据总包:"+HanccThingsDataFrameUtilPackage.HanccThingsStringByteArrayUtil.ByteToHexString(packetData)+"    数据总包长度:"+packetData.length);

        return packetData;
    }

    /**
     * 把所有的byte数组都拼接起来
     * @param values
     * @return
     */
    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }


    public static void main(String[] args) {

        byte[] cmd8200 = new byte[]{
                0x05,                                           // 1
                0x00, 0x26,                                     // 2
                0x02,                                           // 1
                (byte)0xff,                                     // 1
                0x01, 0x02, 0x03, (byte)0x04, 0x05, 0x06, 0x07, 0x08, // 8
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06,             // 6
                0x02, 0x00, 0x00, 0x02,                         // 4
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 8
                (byte)0x92, 0x00,                               // 2
                0x01, 0x02, 0x03, 0x04,                         // n
                (byte)0x15, 0x62                                // 2
        };

        System.out.println("✴️️️数据包:"+ HanccThingsStringByteArrayUtil.ByteToHexString(cmd8200)+"   数据包长度:"+cmd8200.length);



        // 原始数据转模型
        HanccThingsDataChannelProtocolFrameBean deviceBean = HanccThingsDataChannelProtocolFrameParse.parseFrameData(cmd8200);
        System.out.println("❇️协议头:"+Integer.toHexString(deviceBean.Frame_flag));
        System.out.println("❇️有效数据长度:"+Integer.toHexString(deviceBean.Frame_dataLen));
        System.out.println("❇️协议版本号:"+Integer.toHexString(deviceBean.Frame_version));
        System.out.println("❇️协议类型:"+Integer.toHexString(deviceBean.Frame_type));
        System.out.println("❇️设备编码:"+deviceBean.Frame_deviceEncode);
        System.out.println("❇️设备mac:"+deviceBean.Frame_deviceMAC);
        System.out.println("❇️帧序:"+Long.toHexString(deviceBean.Frame_packetNum));
        System.out.println("❇️保留字:"+Arrays.toString(deviceBean.Frame_reserveField));
        System.out.println("❇️命令:"+Integer.toHexString(deviceBean.Frame_command));
        System.out.println("❇️数据区:"+Arrays.toString(deviceBean.Frame_dataBody));
        System.out.println("❇️CRC校验:"+Integer.toHexString(deviceBean.Frame_CRC16));






//        HanccThingsDataConvertPackage.HanccThingsDataChannelProtocolFrameBean deviceBean = new HanccThingsDataConvertPackage.HanccThingsDataChannelProtocolFrameBean();
        deviceBean.Frame_flag         = 0x05;
        deviceBean.Frame_version      = 0x02;
        deviceBean.Frame_type         = 0xff;

        deviceBean.Frame_deviceEncode = "0102030405060708";
        deviceBean.Frame_deviceMAC    = "010203040506";
        deviceBean.Frame_packetNum    = 33554434;
        deviceBean.Frame_reserveField = new byte[8];
        deviceBean.Frame_command      = 0x9200;
        deviceBean.Frame_dataBody     = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] desData = HanccThingsDataChannelProtocolFrameParse.parseFrameBean(deviceBean);
        System.out.println("数据总包:"+ HanccThingsStringByteArrayUtil.ByteToHexString(desData)+"    数据总包长度:"+desData.length);
    }
}
