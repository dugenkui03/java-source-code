package base.bit;

/**
 * todo java11里边的工具类，尤其是murmurhash的实现。
 * @author 杜艮魁
 * @date 2018/10/11
 */
public class DemoTest {
    public static void main(String[] args) {
        /**
         * 获取二进制串为1111111111111111000000000000000的数字
         */
        int value=Integer.parseInt("1111111111111111000000000000001",2);
        System.out.println("1111111111111111000000000000000");
        /**
         * 左移3位
         */
        value=Integer.rotateLeft(value,7);
//        System.out.println(Integer.toBinaryString(value));

        /**
         * 右移2位
         */
        value=Integer.rotateRight(value,7);
        System.out.println(Integer.toBinaryString(value));

        // fixme 移动相同位数后不回复原


        System.out.println(Integer.parseInt("1b873593",16));
        System.out.println(Integer.toBinaryString(461845907));

        System.out.println(3050743062l+1244224234l-0xFFFFFFFFL);


//        System.out.println(Integer.parseInt("FFFFFFFF",16));

//        System.out.println(0xFFFFFFFFL-0x7fffffff-0x7fffffff-1);

    }

    static{
        int value=111,distance=1,n,o,t;

        value &= 0xFFFFFFFFL;
        int newValue = (value << distance) | (value >>> -distance);

    }

}
