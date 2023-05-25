package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.mapper.AddressBookMapper;
import com.zyh.spring.take_away.pojo.AddressBook;
import com.zyh.spring.take_away.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
@Slf4j
public class AddressBookImp
        extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {
}
