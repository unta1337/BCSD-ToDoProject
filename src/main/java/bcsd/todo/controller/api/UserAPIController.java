package bcsd.todo.controller.api;

import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * API를 위한 사용자 컨트롤러 클래스.
 */
@Controller
@RequestMapping(value = "/api/user", produces = "application/json; charset=utf-8")
@ResponseBody
public class UserAPIController {
    /**
     * 사용자 서비스 객체.
     */
    @Autowired
    private DefaultUserService userService;
    /**
     * 현재 접속한 세션.
     */
    @Autowired
    private HttpSession session;

    /**
     * 현재 세션에 로그인한 사용자 정보 조회.
     *
     * @return 사용자 정보 또는 null
     */
    @RequestMapping(method = RequestMethod.GET)
    public User getSessionUser() {
        User user = (User) session.getAttribute("sessionUser");
        return user != null ? userService.getUserInfo(user.getId()) : null;
    }
}
