package bcsd.todo.controller;

import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
