package top.misec.utils;


import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;

/**
 * @author junzhou
 */
@Log4j2
public class JwtUtils {
    private static final String SECRET = "ajkbnjkalsh983y1280jkac";

    private static final String EXP = "exp";

    private static final String PAYLOAD = "payload";

    private static final JWTSigner JWT_SIGNER = new JWTSigner(SECRET);
    private static final JWTVerifier JWT_VERIFIER = new JWTVerifier(SECRET);
    private static final long DEFAULT_MAX_AGE = 1000 * 60 * 60 * 100;


    public static String sign(Integer id) {
        return sign(id, DEFAULT_MAX_AGE);
    }


    /**
     * 加密，传入一个id和有效期
     *
     * @param id
     * @param maxAge
     * @return
     */
    @Synchronized
    public static String sign(Integer id, long maxAge) {
        try {
            Map<String, Object> claims = new HashMap<>(16);
            claims.put(PAYLOAD, id);
            claims.put(EXP, System.currentTimeMillis() + maxAge);
            return JWT_SIGNER.sign(claims);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密，传入一个加密后的token字符串和解密后的类型
     *
     * @param jwt
     * @return
     */
    @Synchronized
    public static Integer verifySign(String jwt) {
        try {
            Map<String, Object> claims = JWT_VERIFIER.verify(jwt);
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                long exp = (Long) claims.get(EXP);
                long currentTimeMillis = System.currentTimeMillis();
                if (exp > currentTimeMillis) {
                    log.warn(claims.get(PAYLOAD));
                    return (Integer) claims.get(PAYLOAD);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
