package bcsd.todo.controller;

import bcsd.todo.annotation.Authenticate;
import bcsd.todo.annotation.Authorize;
import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import bcsd.todo.utility.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 사용자 컨트롤러 클래스.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private DefaultUserService userService;

    /**
     * 타 사용자의 사용자 조회 요청에 대해 사용자 정보 페이지 출력.
     *
     * @param id 사용자 아이디
     * @param session 브라우저 세션
     * @return 사용자 정보 페이지 또는 No such user 페이지
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Authorize
    public String getUserInfoPage(@PathVariable("id") String id, HttpSession session) {
        if (!userService.isValidUser(id)) {
            return "error/noSuchUser";
        }

        session.setAttribute("targetUserId", id);

        Boolean authorizationResult = (Boolean) session.getAttribute("authorizationResult");

        if (authorizationResult == null) {
            return "error/500";
        }

        if (authorizationResult) {
            return "userInfo";
        } else {
            return "userInfoOther";
        }
    }

    /**
     * 사용자 로그인 처리 및 관련 페이지 출력.
     *
     * @param id 사용자 아이디
     * @param body POST 요청 바디
     * @param session 브라우저 세션
     * @return 사용자 정보 페이지 또는 No such user 페이지
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @Authenticate
    public String getUserInfoPage(@PathVariable("id") String id, @ModelAttribute User body, HttpSession session, HttpServletResponse response) {
        if (!userService.isValidUser(id)) {
            return "error/noSuchUser";
        }

        Boolean authenticationResult = (Boolean) session.getAttribute("authenticationResult");

        if (authenticationResult == null) {
            return "error/500";
        }

        if (authenticationResult) {
            TokenUtil.sendTokenViaCookie(id, response);
            return "userInfo";
        } else {
            return "error/418";
        }
    }

    /**
     * 로그아웃.
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
