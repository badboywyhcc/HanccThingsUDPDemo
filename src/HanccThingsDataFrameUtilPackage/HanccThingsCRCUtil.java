package HanccThingsDataFrameUtilPackage;

public class HanccThingsCRCUtil {

    /**
     * CRC16 校验,检验码CRC-16/X25 X16+X12+X5+1
     * @param data
     * @param length
     * @return
     */

    public static byte[] CRC16_Type1(byte[] data, int length) {
        int j     = 0;
        int crc16 = 0x0000FFFF;
        for (int i = 0; i < length; i++) {
            crc16 ^= data[i] & 0x000000FF;
            for (j = 0; j < 8; j++) {
                int flags = crc16 & 0x00000001;
                if (flags != 0) {
                    crc16 = (crc16 >> 1) ^ 0x8408;
                } else {
                    crc16 >>= 0x01;
                }
            }
        }
        int    ret = ~crc16 & 0x0000FFFF;
        byte[] crc = new byte[2];
        crc[1] = (byte) (ret & 0x000000FF);
        crc[0] = (byte) ((ret >> 8) & 0x000000FF);
        return crc;
    }
}
