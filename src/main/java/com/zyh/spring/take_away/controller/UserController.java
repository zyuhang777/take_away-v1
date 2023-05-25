package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.pojo.User;
import com.zyh.spring.take_away.service.UserService;
import com.zyh.spring.take_away.util.UsernameGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "用户控制层")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("用户端发送验证码")
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user,
                             HttpServletRequest request) {
        String code = userService.sendMsg(user);
        HttpSession session = request.getSession();
        session.setAttribute(user.getPhone(), code);
        return R.success("手机验证码发送成功");
    }

    @ApiOperation("用户端登录")
    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpServletRequest request) {
        String phone = user.getPhone();
        HttpSession session = request.getSession();
        String codeSrc = (String) session.getAttribute(phone);
        if (codeSrc.equals(user.getCode())) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User userDb = userService.getOne(userLambdaQueryWrapper);
            if (userDb == null) {
                user.setName(UsernameGenerator.getRandomJianHan(7));
                userService.save(user);
            }
            session.setAttribute("user", userDb == null ? user.getId().toString() : userDb.getId().toString());

            return R.success(user);
        }
        return R.error("验证码错误，登录失败");
    }
}
