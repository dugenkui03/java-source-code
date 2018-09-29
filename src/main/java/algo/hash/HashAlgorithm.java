package algo.hash;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;

/**
 * @Description 各种哈希算法的实现
 * @Date 2018/9/29 下午2:57
 * -
 * @Author dugenkui
 **/

public class HashAlgorithm {

    /**
     * 字符串简单哈希求值
     *
     * @return
     */
    public static String simpleHash(String key) {
        return key.hashCode() + "";
    }

    /**
     * 字符串注意高位影响的哈希(参考HashMap)
     *
     * @return
     */
    public static double bitHash(String key) {
        return key.hashCode() ^ (key.hashCode() >>> 16);
    }

    /**
     * 字符串求MD5值取模
     *
     * @return
     */
    public static String md5Hash(String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 字符串求MD5值后在hash
     *
     * @return
     */
    public static String md5ThenHash(String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes()).hashCode() + "";
    }

    /**
     * 使用murmruHash算法进行哈希：随机性分布特征好、速度快、非加密；
     *
     * @return
     */
    public static String murmurHash(String key) {
        HashFunction murmur3_128 = Hashing.murmur3_128();
        HashCode hashCode = murmur3_128.hashString(key, Charset.forName("utf-8"));
        return hashCode.toString();
    }
}
