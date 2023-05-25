package com.zyh.spring.take_away.commen.Exception;

import lombok.Data;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Data
public class TakeAwayException extends RuntimeException{

    public TakeAwayException(String message) {
        super(message);
        System.out.println("eeee");
    }
}
