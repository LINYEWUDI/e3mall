package xyz.linye.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.linye.domain.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController {


    @RequestMapping("test")
    public void test(String username,String password,HttpServletResponse response) {
        try {
            response.getWriter().write(username+"====>"+password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @RequestMapping("ajax")
    public void ajaxTest(@RequestBody String username,@RequestBody String password ,HttpServletResponse response) {
//    public User ajaxTest(@RequestBody User user, HttpServletResponse response) {
        String user = username + "====>"+password;
        System.out.println(user);
        try {
            response.getWriter().write(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
