package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.pojo.AddressBook;
import com.zyh.spring.take_away.service.AddressBookService;
import com.zyh.spring.take_away.util.ThreadLocalContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "移动端地址本控制层")
@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @ApiOperation("获取用户的所有地址信息")
    @GetMapping("/list")
    public R<List<AddressBook>> addressList(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object id = session.getAttribute("user");
         id = id.toString();
        LambdaQueryWrapper<AddressBook> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(AddressBook::getUserId,id);
        List<AddressBook> addressBookList = addressBookService.list(userLambdaQueryWrapper);
        return R.success(addressBookList);

    }
    @ApiOperation("添加用户地址信息")
    @PostMapping
    public R<String> addAddress(@RequestBody AddressBook addressBook,HttpServletRequest request){
        HttpSession session = request.getSession();
        addressBook.setUserId(Long.valueOf(session.getAttribute("user").toString()));
        addressBookService.save(addressBook);
        return R.success("地址添加成功");
    }
    @ApiOperation("用户默认地址的设置")
    @PutMapping("/default")
    @Transactional
    public R<String> defaultAddress(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getUserId,ThreadLocalContext.get());
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(addressBookLambdaUpdateWrapper);
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper1.eq(AddressBook::getId,addressBook.getId());
        addressBookLambdaUpdateWrapper1.set(AddressBook::getIsDefault,1);
        addressBookService.update(addressBookLambdaUpdateWrapper1);
        return R.success("默认地址修改成功");
    }
    @ApiOperation("通过id得到AddressBook")
    @GetMapping("{id}")
    public R<AddressBook> getAddressBook(@PathVariable String id){
        return R.success(addressBookService.getById(id));
    }
    @ApiOperation("根据id修改地址")
    @PutMapping()
    public R<String> updateAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("地址信息修改成功");
    }
    @ApiOperation("删除地址信息")
    @DeleteMapping
    public R<String> deleteAddressBook(@RequestParam("ids") String id){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getId,id);
        addressBookService.remove(addressBookLambdaQueryWrapper);
        return R.success("地址信息删除成功");
    }
    @ApiOperation("获取用户默认地址")
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        Long userId = ThreadLocalContext.get();
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId,userId).eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);
        return R.success(addressBook);
    }
}
