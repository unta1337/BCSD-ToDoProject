package bcsd.todo.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenUtil {
    private static final String signatureString = "Internal-Secret-String";
    private static final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(signatureString)).build();

    public static String generateToken(String id) {
        return JWT.create().withClaim("id", id).sign(Algorithm.HMAC256(signatureString));
    }

    public static Boolean verifyToken(String token) {
        try {
            verifier.verify(token);
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public static Boolean sendTokenViaCookie(String id, HttpServletResponse response) {
        String token = generateToken(id);

        response.addCookie(new Cookie("token", token));

        return true;
    }
}
