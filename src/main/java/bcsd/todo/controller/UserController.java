package bcsd.todo.controller;

import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 컨트롤러 클래스.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private DefaultUserService userService;

    /*
     * TODO: 로그인 시 JWT 토큰 발급
     *       JWT 사용한 인증 및 인가 구현
     *       사용자 비밀번호 인증 로직 추가
     *       사용자 제거 메소드 추가
     */

    /**
     * User 컨트롤러 테스트용 메소드.
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        String id = "test";
        String password = "1234";

        Boolean trial = userService.createUser(id, password);

        return Boolean.toString(trial);
    }

    /**
     * 사용자로부터 아이디와 비밀번호를 입력 받아 새로운 사용자 계정 생성.
     *
     * @param user 생성할 사용자 정보
     * @return 사용자 계정 생성 여부
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Boolean createUser(@RequestBody User user) {
        String id = user.getId();
        String password = user.getPassword();

        // 아이디 또는 비밀번호가 없으면 사용자 생성 실패.
        if (id == null || password == null) {
            return false;
        }
        if (id.length() == 0 || password.length() == 0) {
            return false;
        }

        return userService.createUser(id, password);
    }

    /**
     * 사용자의 고유 번호를 이용해 사용자 정보 불러오기.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 정보 또는 null
     */
    @RequestMapping(value = "/uniq/{idUniq}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserByIdUniq(@PathVariable("idUniq") Integer idUniq) {
        return userService.getUserByIdUniq(idUniq);
    }

    /**
     * 사용자의 아이디를 이용해 사용자 정보 불러오기.
     *
     * @param id 사용자 아이디
     * @return 사용자 정보 리스트 또는 빈 리스트
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    /**
     * 사용자 계정 삭제하기.
     *
     * @param idUniq 사용자 고유 번호
     * @param hard Hard Delete 여부
     */
    @RequestMapping(value = "uniq/{idUniq}", method = RequestMethod.DELETE)
    @ResponseBody
    public Boolean deleteUser(@PathVariable("idUniq") Integer idUniq, @RequestParam(value = "hard", defaultValue = "false") Boolean hard) {
        return userService.deleteUserByIdUniq(idUniq, hard);
    }

    /**
     * 사용자 계정 복구하기.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 계정 복구 여부
     */
    @RequestMapping(value = "/uniq/{idUniq}", method = RequestMethod.PATCH)
    @ResponseBody
    public Boolean restoreUserByIdUniq(@PathVariable("idUniq") Integer idUniq) {
        return userService.restoreUserByIdUniq(idUniq);
    }
}
