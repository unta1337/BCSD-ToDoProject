package bcsd.todo.controller;

import bcsd.todo.domain.User;
import bcsd.todo.service.user.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{idUniq}", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserByIdUniq(@PathVariable("idUniq") Integer idUniq) {
        return userService.getUserByIdUniq(idUniq);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Boolean createUser(HttpServletRequest request) {
        String id = request.getParameter("id");
        String password = request.getParameter("password");

        return userService.createUser(id, password);
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public List<User> test(@Param("idUniq") Integer idUniq) {
        return userService.getUserByIdUniq(idUniq);
    }
}
