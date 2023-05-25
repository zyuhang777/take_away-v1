package com.zyh.spring.take_away.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.spring.take_away.pojo.User;

public interface UserService extends IService<User> {
    String sendMsg(User user);
}
