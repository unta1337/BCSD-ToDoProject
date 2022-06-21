package bcsd.todo.controller.api;

import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * API를 위한 기본 컨트롤러 클래스.
 */
@Controller
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
@ResponseBody
public class DefaultAPIController {
    /**
     * 사용자 서비스 객체.
     */
    @Autowired
    DefaultUserService userService;

    /**
     * 로그인.
     *
     * @param user 로그인을 시도하려는 사용자의 정보
     * @return 로그인 성공 여부
     */
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public Boolean signIn(@RequestBody User user) {
        return userService.signIn(user);
    }

    /**
     * 로그아웃.
     *
     * @return 로그아웃 성공 여부
     */
    @RequestMapping(value = "/sign-out", method = RequestMethod.POST)
    public Boolean signOut() {
        return userService.signOut();
    }
}
