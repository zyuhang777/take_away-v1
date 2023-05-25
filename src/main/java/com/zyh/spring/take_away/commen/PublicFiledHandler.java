package com.zyh.spring.take_away.commen;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zyh.spring.take_away.util.ThreadLocalContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Component
public class PublicFiledHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("createUser", ThreadLocalContext.get());
        metaObject.setValue("updateUser",ThreadLocalContext.get());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",1l);
    }
}
