package bcsd.todo.controller;

import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * 메인 페이지 컨트롤러.
 */
@Controller
public class DefaultController {
    @Autowired
    private DefaultUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index(HttpSession session) {
        return "login";
    }
}
