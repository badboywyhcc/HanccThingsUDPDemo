package HanccThingsDataFrameUtilPackage;

import java.net.DatagramPacket;

public class HanccThingsStringByteArrayUtil {
    /**
     * 字符串转换成十六进制字符串
     */
    public static String StringToHexString(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }
    /**
     * 十六进制字符串转换字符串
     * @param hexStr
     * @return String
     */
    public static String HexStringToString(String hexStr) {

        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }



    /**
     * 把16进制字符串转换成byte[]字节数组
     * @param hexStr
     * @return byte[]
     */
    public static byte[] HexStringToByte(String hexStr) {
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    /**
     * byte[]数组转成十六进制字符串
     * @param b
     * @return HexString
     */
    public static String ByteToHexString(byte[] b){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i){
            buffer.append(ByteToHexString(b[i]));
        }
        return buffer.toString();
    }
    public static String ByteToHexString(byte b){
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
    }

    /**
     * 数组转换成十六进制字符串
     */
    public static final String ByteToHexString(DatagramPacket packet) {
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
