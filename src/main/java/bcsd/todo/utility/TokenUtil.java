package bcsd.todo.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 토큰 유틸리티 클래스.
 */
@Component
public class TokenUtil {
    /**
     * 내부적으로 사용하는 Signature 문자열.
     */
    private static final String signatureString = "Internal-Secret-String";
    /**
     * 토큰 유효성 검사기.
     */
    private static final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(signatureString)).build();

    /**
     * 토큰 생성.
     *
     * @param id 사용자 아이디
     * @return 토큰 문자열
     */
    public static String generateToken(String id) {
        return JWT.create().withClaim("id", id).sign(Algorithm.HMAC256(signatureString));
    }

    /**
     * 토큰 유효성 검사.
     *
     * @param token 토큰 문자열
     * @return
     */
    public static Boolean verifyToken(String token) {
        try {
            verifier.verify(token);
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 쿠기에 토큰 전송.
     *
     * @param id 사용자 아이디
     * @param response HTTP Response
     * @return 토큰 전송 여부
     */
    public static Boolean sendTokenViaCookie(String id, HttpServletResponse response) {
        String token = generateToken(id);

        response.addCookie(new Cookie("token", token));

        return true;
    }
}
