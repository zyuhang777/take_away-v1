package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.pojo.Employee;
import com.zyh.spring.take_away.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Api(tags = "Employee控制层")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @ApiOperation("employee用户登录")
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //密码加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        //查询数据库

        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        /**
         * QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
         * //        employeeQueryWrapper.eq("username", employee.getUsername());
         */
        Employee emp = employeeService.getOne(employeeLambdaQueryWrapper);
        if (emp == null) {
            return R.error("用户名不存在");
        }
//        assert emp != null;
        if (!password.equals(emp.getPassword())) {
            return R.error("密码不正确");
        }
        if (emp.getStatus() == 0) {
            return R.error("该账户已经被禁用");
        }
        HttpSession session = request.getSession();
        session.setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    @ApiOperation("用户退出")
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("用户已退出");
    }

    @ApiOperation("添加员工")
    @PostMapping("")
    public R<String> saveEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        HttpSession session = request.getSession();
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        boolean save = employeeService.save(employee);
        return save ? R.success("添加成功") : R.error("添加失败");

    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public R<Page<Employee>> page(Long page, Long pageSize, String name) {
        Page<Employee> employeePage = new Page<>(page, pageSize);

//        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
//        employeeQueryWrapper.like(!(name==null||name.equals("")),"name",name);
//        employeeService.page(employeePage,employeeQueryWrapper);

        if (StringUtils.isNotEmpty(name)) {
            name = "% " + name + "%";
        }
        employeeService.pageSelect(employeePage, name);
        return R.success(employeePage);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("")
    public R<String> update(@RequestBody Employee employee) {
        employeeService.updateById(employee);
        return R.success("更新成功");
    }

    @ApiOperation("通过id查询员工")
    @GetMapping("/{id}")
    public R<Employee> getEmployeeById(@PathVariable String id) {
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getId, id);
        Employee emp = employeeService.getOne(employeeLambdaQueryWrapper);
        return R.success(emp);
    }
}
