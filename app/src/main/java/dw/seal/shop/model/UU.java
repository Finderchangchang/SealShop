package dw.seal.shop.model;

/**
 * Created by Administrator on 2017/9/28.
 */

public class UU {
    /*将十六进制转换为字节数组*/
    public static byte[] HexStringToBinary(String hexString) {
        String hexStr = "0123456789ABCDEF";
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位

        for (int i = 0; i < len; i++) {
            //右移四位得到高位
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);//高地位做或运算
        }
        return bytes;
    }
}
