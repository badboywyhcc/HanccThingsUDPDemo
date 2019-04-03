package HanccThingsDataConvertPackage;

public class HanccThingsDataChannelProtocolFrameBean {
    public int     Frame_flag;            //  协议头
    public int     Frame_dataLen;         //  数据长度,数据长度N+34
    public int     Frame_version;         //  协议版本号,协议类型0x00-0x03
    public int     Frame_type;            //  协议类型,整机品牌【4】+ 设备分类【2】+ 设备子分类【1】+ 产品序号【1】
    public String  Frame_deviceEncode;    //  设备编码,设备发出的序列号：0x00000000--0x0fffffff，服务器发出的序列号：0x10000000-0x1fffffff，APP 发出的序列号：0x20000000-0x2fffffff,此序列号要滚动+1
    public String  Frame_deviceMAC;       //  设备mac
    public long    Frame_packetNum;       //  帧序
    public byte[]  Frame_reserveField;    //  保留字
    public int     Frame_command;         //  命令
    public byte[]  Frame_dataBody;        //  数据区
    public int     Frame_CRC16;           //  CRC校验,数据crc16检验码CRC-16/X25 X16+X12+X5+1
}
