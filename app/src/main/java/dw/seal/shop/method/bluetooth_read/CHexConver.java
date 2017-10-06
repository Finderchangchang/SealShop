// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CHexConver.java

package dw.seal.shop.method.bluetooth_read;

public class CHexConver {
    static String Key = "FFFFFFFFFFFF";//秘钥
    static String StartCard = "3C";//初始块
    static String CardCount = "01";//块数量
    private static final char mChars[] = "0123456789ABCDEF".toCharArray();
    private static final String mHexStr = "0123456789ABCDEF";
    public CHexConver() {
    }
    public static String getButtonReadVal(){
        return (CardCount + StartCard + Key);
    }
    public static String byte2HexStr(byte abyte0[], int i) {
        StringBuilder stringbuilder = new StringBuilder("");
        int j = 0;
        do {
            if (j >= i)
                return stringbuilder.toString().toUpperCase().trim();
            String s = Integer.toHexString(abyte0[j] & 0xff);
            String s1;
            if (s.length() == 1)
                s1 = (new StringBuilder("0")).append(s).toString();
            else
                s1 = s;
            stringbuilder.append(s1);
            stringbuilder.append(" ");
            j++;
        } while (true);
    }

    public static boolean checkHexStr(String s) {
        String s1;
        int i = 0;
        int j = 0;
        boolean flag = true;
        s1 = s.toString().trim().replace(" ", "").toUpperCase();
        i = s1.length();
        if (i <= 1 || i % 2 != 0) {

        }

        if (j >= i) {
            flag = true;
        } else {
            int k = j + 1;
            String s2 = s1.substring(j, k);
            if ("0123456789ABCDEF".contains(s2))
                flag = false;
        }
        j++;
        return flag;
    }

    public static byte[] hexStr2Bytes(String s) {
        s = s.toString().trim().replace(" ", "");
        int i = s.length() / 2;
        System.out.println(i);
        byte abyte0[] = new byte[i];
        int j = 0;
        do {
            if (j >= i)
                return abyte0;
            int k = j * 2 + 1;
            int l = k + 1;
            StringBuilder stringbuilder = new StringBuilder("0x");
            int i1 = j * 2;
            String s1 = s.substring(i1, k);
            StringBuilder stringbuilder1 = stringbuilder.append(s1);
            String s2 = s.substring(k, l);
            byte byte0 = (byte) (Integer.decode(stringbuilder1.append(s2).toString()).intValue() & 0xff);
            abyte0[j] = byte0;
            j++;
        } while (true);
    }

    public static String hexStr2Str(String s) {
        char ac[] = s.toCharArray();
        byte abyte0[] = new byte[s.length() / 2];
        int i = 0;
        do {
            int j = abyte0.length;
            if (i >= j)
                return new String(abyte0);
            int k = i * 2;
            char c = ac[k];
            int l = "0123456789ABCDEF".indexOf(c) * 16;
            int i1 = i * 2 + 1;
            char c1 = ac[i1];
            int j1 = "0123456789ABCDEF".indexOf(c1);
            byte byte0 = (byte) (l + j1 & 0xff);
            abyte0[i] = byte0;
            i++;
        } while (true);
    }

    public static String str2HexStr(String s) {
        StringBuilder stringbuilder = new StringBuilder("");
        byte abyte0[] = s.getBytes();
        int i = 0;
        do {
            int j = abyte0.length;
            if (i >= j)
                return stringbuilder.toString().trim();
            int k = (abyte0[i] & 0xf0) >> 4;
            char c = mChars[k];
            stringbuilder.append(c);
            int l = abyte0[i] & 0xf;
            char c1 = mChars[l];
            stringbuilder.append(c1);
            stringbuilder.append(' ');
            i++;
        } while (true);
    }

    public static String strToUnicode(String s)
            throws Exception {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        do {
            int j = s.length();
            if (i >= j)
                return stringbuilder.toString();
            char c = s.charAt(i);
            String s1 = Integer.toHexString(c);
            if (c > '\200') {
                String s2 = (new StringBuilder("\\u")).append(s1).toString();
                stringbuilder.append(s2);
            } else {
                String s3 = (new StringBuilder("\\u00")).append(s1).toString();
                stringbuilder.append(s3);
            }
            i++;
        } while (true);
    }

    public static String unicodeToString(String s) {
        int i = s.length() / 6;
        StringBuilder stringbuilder = new StringBuilder();
        int j = 0;
        do {
            if (j >= i)
                return stringbuilder.toString();
            int k = j * 6;
            int l = (j + 1) * 6;
            String s1 = s.substring(k, l);
            String s2 = String.valueOf(s1.substring(2, 4));
            String s3 = (new StringBuilder(s2)).append("00").toString();
            String s4 = s1.substring(4);
            int i1 = Integer.valueOf(s3, 16).intValue();
            int j1 = Integer.valueOf(s4, 16).intValue();
            char ac[] = Character.toChars(i1 + j1);
            String s5 = new String(ac);
            stringbuilder.append(s5);
            j++;
        } while (true);
    }

    /**
     * @param hexString
     * @return 将十六进制转换为字节数组
     */
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



    public static String calcCheckSum(String msg) {
        String resStr = "";
        int xor = 0;
        int j = 0;
        String str = "";
        String shex = "";
        shex = msg.toString().trim().replace(" ", "").toUpperCase();
        int len = shex.length();
        str = shex.substring(0, 2);
        xor = Integer.parseInt(str, 16);
        for (int i = 2; i < len - 1; i += 2) {
            str = shex.substring(i, i + 2);
            j = Integer.parseInt(str, 16);
            xor = xor ^ j;
        }
        resStr = Integer.toHexString(xor);
        return resStr;
    }

}
