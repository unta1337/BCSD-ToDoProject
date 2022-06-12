package bcsd.todo.controller;

import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 사용자 컨트롤러 클래스.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private DefaultUserService userService;

    /**
     * 타 사용자의 사용자 조회 요청에 대해 사용자 정보 페이지 출력하기.
     *
     * @param id 사용자 아이디
     * @param session 브라우저 세션
     * @return 사용자 정보 페이지 또는 No such user 페이지
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getUserInfoPage(@PathVariable("id") String id, HttpSession session) {
        List<User> targetUsers = userService.getUserById(id);

        if (targetUsers.size() == 0)
            return "error/noSuchUser";

        User targetUser = null;
        for (User tu : targetUsers) {
            if (tu.getValid()) {
                targetUser = tu;
                break;
            }
        }

        if (targetUser == null)
            return "error/noSuchUser";

        if (!id.equals(session.getAttribute("id"))) {
            return "userInfoOther";
        }

        return "userInfo";
    }

    /**
     * 로그인 성공 시 사용자 정보 페이지 출력하기.
     *
     * @param id 사용자 아이디
     * @param body POST 요청 바디
     * @param session 브라우저 세션
     * @return 사용자 정보 페이지 또는 No such user 페이지
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String getUserInfoPage(@PathVariable("id") String id, @ModelAttribute User body, HttpSession session) {
        List<User> targetUsers = userService.getUserById(id);

        if (targetUsers.size() == 0)
            return "error/noSuchUser";

        User targetUser = null;
        for (User tu : targetUsers) {
            if (tu.getValid()) {
                targetUser = tu;
                break;
            }
        }

        if (targetUser == null)
            return "error/noSuchUser";

        if (!BCrypt.checkpw(body.getPassword(), targetUser.getPassword()))
            return "error/418";

        session.setAttribute("id", id);
        session.setAttribute("password", targetUser.getPassword());

        return "userInfo";
    }

    /**
     * 로그아웃하기.
     *
     * @param session 브라우저 세션
     * @return 로그인 페이지
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/login";
    }
}
