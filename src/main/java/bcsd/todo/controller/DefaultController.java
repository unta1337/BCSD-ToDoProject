package bcsd.todo.controller;

import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.*;
import java.util.List;

// TODO: 주석 수정.

/**
 * 메인 페이지 컨트롤러.
 */
@Controller
public class DefaultController {
    @Autowired
    DefaultUserService userService;

    /**
     * 로그인 페이지로 연결.
     *
     * @return 로그인 페이지
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/getJWT", method = RequestMethod.GET)
    @ResponseBody
    public String getJWT(@RequestParam("id") String id) {
        User user = userService.getUserById(id).get(0);

        String token = JWT.create().withClaim("id", id).sign(Algorithm.HMAC256("Internal-Secret-String"));

        return token;
    }

    @RequestMapping(value = "/authJWT", method = RequestMethod.GET)
    @ResponseBody
    public String authJWT(@RequestParam("token") String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("Internal-Secret-String")).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaims().toString();
    }
}
