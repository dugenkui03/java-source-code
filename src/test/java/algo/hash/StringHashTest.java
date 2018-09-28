package algo.hash;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 相近的字符串用JDK原生的
 * @Date 2018/9/27 下午9:49
 * -
 * @Author dugenkui
 **/

public class StringHashTest {
    private static final String ALGORITHM_TYPE="MD5";

    /**
     * 获取UUID集合
     */
    static List<String> uuidList=new ArrayList<>();
    static{
        long t1=System.currentTimeMillis();
        try{
            File file=new File("/Users/moriushitorasakigake/Desktop/uuid2.txt");
            BufferedReader fileReader=new BufferedReader(new FileReader(file));
            String tmp;
            while((tmp=fileReader.readLine())!=null){
                uuidList.add(tmp);
            }
        }catch (IOException e){
            System.out.println("请在指定目录添加样本数据");
        }
        System.out.println("加载数据耗时："+(System.currentTimeMillis()-t1));
    }


    static int simpleHash(String key){
        return Math.abs(key.hashCode())%100+1;
    }

    static int md5(String key){
        MessageDigest md5=null;
        try{
            md5=MessageDigest.getInstance(ALGORITHM_TYPE);
        }catch (Exception e){ }
        md5.update(key.getBytes());
        byte[] md5ByteArr=md5.digest();
        String md5Str=byteArrayToHex(md5ByteArr);

        return Math.abs(md5Str.hashCode())%100+1;
    }

    private static String byteArrayToHex(byte[] byteArray){
        //首先初始化一个数组，保存16进制的每个字符
        char[] hexDigits={'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
        //一个byte是8位，需要用两个十六进制的数存放
        char[] resultCharArray=new char[byteArray.length*2];
        //遍历字节数组，使用位运算将byte转换成字符放到数组中
        int index=0;
        for (byte b:byteArray) {
            //分别取高四位和低四位存放到结果charArray中
            resultCharArray[index++]=hexDigits[b>>>4&0xf];//f=1111
            resultCharArray[index++]=hexDigits[b&0xf];
        }

        return new String(resultCharArray);
    }

    static int murmurHash(String key){
        HashFunction murmur3_128=Hashing.murmur3_128();
        HashCode hashCode=murmur3_128.hashString(key, Charset.forName("utf-8"));

        return Math.abs(hashCode.asInt())%100+1;
    }

    public static void main(String[] args) {
        List<Integer> simHashRes=new ArrayList<>();
        long t1=System.currentTimeMillis();
        for (String uuid:uuidList) {
            simHashRes.add(simpleHash(uuid));
        }

        long t2=System.currentTimeMillis();
        List<Integer> md5Res=new ArrayList<>();
        for (String uuid:uuidList) {
            simHashRes.add(md5(uuid));
        }

        long t3=System.currentTimeMillis();
        List<Integer> murmurHashRes=new ArrayList<>();
        for (String uuid:uuidList) {
            simHashRes.add(murmurHash(uuid));
        }

        long t4=System.currentTimeMillis();

        System.out.println((t2-t1)+":"+(t3-t2)+":"+(t4-t3));
    }
}
