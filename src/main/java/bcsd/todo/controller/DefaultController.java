package bcsd.todo.controller;

import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// TODO: 필요한 곳에 주석 작성.

// TODO: 회원탈퇴 구현 필요. (별도의 페이지를 작성하여 Soft, Hard 여부 확인)
// TODO: 사용자 아이디 변경 구현 필요.
// TODO: 사용자 비밀번호 변경 구현 필요.
// TODO: 상기한 기능에 대한 컨트롤러(페이지) 구현 필요.

/**
 * 메인 페이지 컨트롤러.
 */
@Controller
public class DefaultController {
    /**
     * 사용자 서비스 객체.
     */
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

    /**
     * 회원가입 페이지로 연결.
     *
     * @return 회원가입 페이지
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public String signUp() {
        return "signUp";
    }

    /**
     * 회원가입 페이지로 연결.
     *
     * @return 회원가입 페이지
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String signUpPost() {
        return signUp();
    }
}
