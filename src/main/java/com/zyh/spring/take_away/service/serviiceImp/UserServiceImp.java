package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.mapper.UserMapper;
import com.zyh.spring.take_away.pojo.User;
import com.zyh.spring.take_away.service.UserService;
import com.zyh.spring.take_away.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
@Slf4j
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public String sendMsg(User user) {
        String phone = user.getPhone();
        log.info("{}",phone);
        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("{}",code);
//            SMSUtils.sendMessage("上饭下菜",
//                    "SMS_460870227",phone,code);
            redisTemplate.opsForValue().set("code",code,3, TimeUnit.MINUTES);
            return code;
        }
        return phone;
    }
}
