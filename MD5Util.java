import java.io.UnsupportedEncodingException;

/**
 * user:hao.song
 * date:15/6/27
 * time:下午10:37
 */
public class MD5Util {
    public static final int GROUP_LEN = 512 / 8;


    public static String encrypt(String toEncrypt) {
        byte[] md5Bytes = getMd5PreBytes(toEncrypt);
        int groupNums = md5Bytes.length / GROUP_LEN;
        int abcdArray[] = {0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476};
        for (int i = 0; i < groupNums; ++i) {
            int m[] = getOneGroup(i, md5Bytes);
            abcdArray = oneLoop(abcdArray[0], abcdArray[1], abcdArray[2], abcdArray[3], m);
        }
        return toViewString(abcdArray[0], abcdArray[1], abcdArray[2], abcdArray[3]);
    }

    private static String toViewString(int A, int B, int C, int D) {
        byte a[] = ByteUtil.changeIntToBytes(A);
        byte b[] = ByteUtil.changeIntToBytes(B);
        byte c[] = ByteUtil.changeIntToBytes(C);
        byte d[] = ByteUtil.changeIntToBytes(D);
        int x1 = d.length;
        int x2 = x1 + c.length;
        int x3 = x2 + b.length;
        byte result[] = new byte[a.length + b.length + c.length + d.length];
        for (int i = 0; i < result.length; ++i) {
            if (i < x1) {
                result[i] = d[i];
            }
            if (i >= x1 && i < x2) {
                result[i] = c[i - x1];
            }
            if (i >= x2 && i < x3) {
                result[i] = b[i - x2];
            }
            if (i >= x3) {
                result[i] = a[i - x3];
            }
        }
        String s = "";
        return new String(result);
    }

    private static int[] getOneGroup(int i, byte[] md5Bytes) {
        int end = (i + 1) * GROUP_LEN;
        int m[] = new int[16];
        int idx = 0;
        for (int j = i * GROUP_LEN; j < end; j += 4) {
            byte b[] = new byte[]{
                    md5Bytes[j],
                    md5Bytes[j + 1],
                    md5Bytes[j + 2],
                    md5Bytes[j + 3]
            };
            m[idx++] = ByteUtil.byteArrayToInt(b);
        }
        return m;
    }

    public static byte[] getMd5PreBytes(String str) {
        int len = str.length();
        int groupCount = len / GROUP_LEN + 1;
        int leftLen = len % GROUP_LEN;
        int toAdd = 0;
        if (leftLen <= 56) {
            toAdd = 56 - leftLen;
        } else {
            toAdd = GROUP_LEN - leftLen + 56;
            groupCount += 1;
        }
        return getFillingBytes(str, groupCount, toAdd);
    }

    public static byte[] getFillingBytes(String str, int groupCount, int toAddCount) {
        byte[] result = new byte[GROUP_LEN * groupCount];
        int oriLen = 0;
        try {
            byte[] toBeFilled = str.getBytes("UTF-8");
            oriLen = toBeFilled.length;
            for (int i = 0; i < oriLen; ++i) {
                result[i] = toBeFilled[i];
            }
            result[oriLen] = -0x80;
            for (int i = oriLen + 1; i < oriLen + toAddCount; ++i) {
                result[i] = 0x00;
            }
            byte[] lenBytes = ByteUtil.changeLongToBytes(oriLen * 8);
            for (int i = 0; i < lenBytes.length; ++i) {
                result[oriLen + toAddCount + i] = lenBytes[i];
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported encoding");
        }

        return result;
    }

    private static int[] oneLoop(int A, int B, int C, int D, int[] m) {
        int a = A;
        int b = B;
        int c = C;
        int d = D;
        //first loop
        a = FF(a, b, c, d, m[0], 7, 0xd76aa478);
        d = FF(d, a, b, c, m[1], 12, 0xe8c7b756);
        c = FF(c, d, a, b, m[2], 17, 0x242070db);
        b = FF(b, c, d, a, m[3], 22, 0xc1bdceee);
        a = FF(a, b, c, d, m[4], 7, 0xf57c0faf);
        d = FF(d, a, b, c, m[5], 12, 0x4787c62a);
        c = FF(c, d, a, b, m[6], 17, 0xa8304613);
        b = FF(b, c, d, a, m[7], 22, 0xfd469501);
        a = FF(a, b, c, d, m[8], 7, 0x698098d8);
        d = FF(d, a, b, c, m[9], 12, 0x8b44f7af);
        c = FF(c, d, a, b, m[10], 17, 0xffff5bb1);
        b = FF(b, c, d, a, m[11], 22, 0x895cd7be);
        a = FF(a, b, c, d, m[12], 7, 0x6b901122);
        d = FF(d, a, b, c, m[13], 12, 0xfd987193);
        c = FF(c, d, a, b, m[14], 17, 0xa679438e);
        b = FF(b, c, d, a, m[15], 22, 0x49b40821);

        //second loop
        a = GG(a, b, c, d, m[1], 5, 0xf61e2562);
        d = GG(d, a, b, c, m[6], 9, 0xc040b340);
        c = GG(c, d, a, b, m[11], 14, 0x265e5a51);
        b = GG(b, c, d, a, m[0], 20, 0xe9b6c7aa);
        a = GG(a, b, c, d, m[5], 5, 0xd62f105d);
        d = GG(d, a, b, c, m[10], 9, 0x02441453);
        c = GG(c, d, a, b, m[15], 14, 0xd8a1e681);
        b = GG(b, c, d, a, m[4], 20, 0xe7d3fbc8);
        a = GG(a, b, c, d, m[9], 5, 0x21e1cde6);
        d = GG(d, a, b, c, m[14], 9, 0xc33707d6);
        c = GG(c, d, a, b, m[3], 14, 0xf4d50d87);
        b = GG(b, c, d, a, m[8], 20, 0x455a14ed);
        a = GG(a, b, c, d, m[13], 5, 0xa9e3e905);
        d = GG(d, a, b, c, m[2], 9, 0xfcefa3f8);
        c = GG(c, d, a, b, m[7], 14, 0x676f02d9);
        b = GG(b, c, d, a, m[12], 20, 0x8d2a4c8a);

        //third loop
        a = HH(a, b, c, d, m[5], 4, 0xfffa3942);
        d = HH(d, a, b, c, m[8], 11, 0x8771f681);
        c = HH(c, d, a, b, m[11], 16, 0x6d9d6122);
        b = HH(b, c, d, a, m[14], 23, 0xfde5380c);
        a = HH(a, b, c, d, m[1], 4, 0xa4beea44);
        d = HH(d, a, b, c, m[4], 11, 0x4bdecfa9);
        c = HH(c, d, a, b, m[7], 16, 0xf6bb4b60);
        b = HH(b, c, d, a, m[10], 23, 0xbebfbc70);
        a = HH(a, b, c, d, m[13], 4, 0x289b7ec6);
        d = HH(d, a, b, c, m[0], 11, 0xeaa127fa);
        c = HH(c, d, a, b, m[3], 16, 0xd4ef3085);
        b = HH(b, c, d, a, m[6], 23, 0x04881d05);
        a = HH(a, b, c, d, m[9], 4, 0xd9d4d039);
        d = HH(d, a, b, c, m[12], 11, 0xe6db99e5);
        c = HH(c, d, a, b, m[15], 16, 0x1fa27cf8);
        b = HH(b, c, d, a, m[2], 23, 0xc4ac5665);

        //forth loop
        a = II(a, b, c, d, m[0], 6, 0xf4292244);
        d = II(d, a, b, c, m[7], 10, 0x432aff97);
        c = II(c, d, a, b, m[14], 15, 0xab9423a7);
        b = II(b, c, d, a, m[5], 21, 0xfc93a039);
        a = II(a, b, c, d, m[12], 6, 0x655b59c3);
        d = II(d, a, b, c, m[3], 10, 0x8f0ccc92);
        c = II(c, d, a, b, m[10], 15, 0xffeff47d);
        b = II(b, c, d, a, m[1], 21, 0x85845dd1);
        a = II(a, b, c, d, m[8], 6, 0x6fa87e4f);
        d = II(d, a, b, c, m[15], 10, 0xfe2ce6e0);
        c = II(c, d, a, b, m[6], 15, 0xa3014314);
        b = II(b, c, d, a, m[13], 21, 0x4e0811a1);
        a = II(a, b, c, d, m[4], 6, 0xf7537e82);
        d = II(d, a, b, c, m[11], 10, 0xbd3af235);
        c = II(c, d, a, b, m[2], 15, 0x2ad7d2bb);
        b = II(b, c, d, a, m[9], 21, 0xeb86d391);

        A += a;
        B += b;
        C += c;
        D += d;
        int[] result = {A, B, C, D};
        return result;
    }

    private static int FF(int a, int b, int c, int d, int mi, int s, int ti) {
        return b + ((a + F(b, c, d) + mi + ti) << s);
    }

    private static int GG(int a, int b, int c, int d, int mi, int s, int ti) {
        return b + ((a + G(b, c, d) + mi + ti) << s);
    }

    private static int HH(int a, int b, int c, int d, int mi, int s, int ti) {
        return b + ((a + H(b, c, d) + mi + ti) << s);
    }

    private static int II(int a, int b, int c, int d, int mi, int s, int ti) {
        return b + ((a + I(b, c, d) + mi + ti) << s);
    }

    private static int F(int x, int y, int z) {
        return (x & y) | ((~x) & z);
    }

    private static int G(int x, int y, int z) {
        return (x & z) | (y & (~z));
    }

    private static int H(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private static int I(int x, int y, int z) {
        return y ^ (x | (~z));
    }


}
