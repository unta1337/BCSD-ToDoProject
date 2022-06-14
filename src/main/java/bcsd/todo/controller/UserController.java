package bcsd.todo.controller;

import bcsd.todo.annotation.Authenticate;
import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import bcsd.todo.utility.LogUtility;
import bcsd.todo.utility.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 사용자 컨트롤러 클래스.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    /**
     * 사용자 서비스 객체.
     */
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
    public String getUserInfoPage(@PathVariable("id") String id, HttpSession session) {
        User targetUser = userService.getUserById(id);
        session.setAttribute("targetUser", targetUser);

        if (targetUser == null) {
            return "error/noSuchUser";
        }

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null || !id.equals(sessionUser.getId())) {
            return "userInfoOther";
        }

        if (userService.getAuthorize()) {
            return "userInfo";
        } else {
            return "userInfoOther";
        }
    }

    /**
     * 사용자 로그인 처리 및 관련 페이지 출력.
     *
     * @param id 사용자 아이디
     * @param body POST 요청 바디 (사용자 정보)
     * @param session 브라우저 세션
     * @param response 브라우저 응답
     * @return 사용자 정보 페이지 또는 No such user 페이지
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @Authenticate
    public String getUserInfoPage(@PathVariable("id") String id, @ModelAttribute User body, HttpSession session, HttpServletResponse response) throws IOException {
        if (!userService.isValidUser(id)) {
            LogUtility.browserAlert("아이디 또는 비밀번호가 유효하지 않습니다.", response);
            return "login";
        }

        Boolean authenticationResult = (Boolean) session.getAttribute("authenticationResult");

        if (authenticationResult == null) {
            return "error/500";
        }

        if (authenticationResult) {
            TokenUtility.sendTokenViaCookie(id, response);
            return "userInfo";
        } else {
            return "error/418";
        }
    }

    /**
     * 사용자 회원가입 처리 관련 및 페이지 출력.
     *
     * @param body POST 요청 바디 (사용자 정보)
     * @param session 브라우저 세션
     * @param response 브라우저 응답
     * @return 사용자 정보 페이지 또는 로그인 페이지
     * @throws IOException 리다이렉트 시 발생할 수 있는 IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    public String createUser(@ModelAttribute User body, HttpSession session, HttpServletResponse response) throws IOException {
        String id = body.getId();
        String password = body.getPassword();

        Boolean succeed = userService.createUser(id, password);

        if (!succeed) {
            LogUtility.browserAlert("아이디 또는 비밀번호가 유효하지 않습니다.", response);
            return "signUp";
        }

        session.setAttribute("sessionUser", userService.getUserById(id));
        session.setAttribute("authenticationResult", true);

        return getUserInfoPage(id, body, session, response);
    }

    /**
     * 로그아웃.
     *
     * @param session 브라우저 세션
     * @return 로그인 페이지
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        return "redirect:/login";
    }

    /**
     * 사용자 계정 삭제. (Soft Delete)
     *
     * @param body POST 요청 바디 (사용자 정보)
     * @return 삭제에 성공한 경우 로그인 페이지 아니면 500.
     */
    @RequestMapping(value = "leave", method = RequestMethod.POST)
    public String leave(@ModelAttribute User body) {
        boolean succeed = userService.deleteUserByIdUniq(body.getIdUniq(), false);

        if (!succeed) {
            return "error/500";
        }

        return "redirect:/login";
    }
}
