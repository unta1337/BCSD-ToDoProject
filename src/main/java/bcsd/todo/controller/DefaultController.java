package bcsd.todo.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DefaultController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/genHash/{password}", method = RequestMethod.GET)
    @ResponseBody
    public String genHash(@PathVariable("password") String password) {
        String encrypted = BCrypt.hashpw(password, BCrypt.gensalt());

        return String.format("%s | %d", encrypted, encrypted.length());
    }

    @RequestMapping(value = "/checkHash/{password}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean checkHash(@PathVariable("password") String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public String postTest(HttpServletRequest request) {
        return request.getParameter("id");
    }
}
