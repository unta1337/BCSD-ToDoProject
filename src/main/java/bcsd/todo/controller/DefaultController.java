package bcsd.todo.controller;

import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// TODO: 회원가입 구현 필요.
// TODO: 회원탈퇴 구현 필요.
// TODO: 사용자 아이디 변경 구현 필요.
// TODO: 사용자 비밀번호 변경 구현 필요.
// TODO: 상기한 기능에 대한 컨트롤러(페이지) 구현 필요.

// TODO: 사용자 아이디 유효성 검사 로직 필요.   (20자 이내 영문자와 숫자만 가능)
// TODO: 사용자 비밀번호 유효성 검사 로직 필요. (기존 비밀번호와 달라야 하고 8자 이상)

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
}
