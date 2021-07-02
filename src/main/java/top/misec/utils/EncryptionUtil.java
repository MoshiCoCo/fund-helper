package top.misec.utils;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * @author JunzhouLiu
 */
public class EncryptionUtil {

    /**
     * 生成盐，即随机生成一个字符串
     *
     * @return
     */
    public static String generateSalt() {
        // UUID
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    /**
     * md5 加密（本质是哈希运算，不可逆）
     *
     * @param data 待加密数
     * @param salt 盐（随机串，这里也相当于秘钥）
     * @return
     */
    public static String md5Hex(String data, String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        // Apache DigestUtils
        return DigestUtils.md5Hex(salt + DigestUtils.md5Hex(data));
    }

    /**
     * SHA512 加密
     */
    public static String shaHex(String data, String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.sha512Hex(salt + DigestUtils.sha512Hex(data));
    }
}
