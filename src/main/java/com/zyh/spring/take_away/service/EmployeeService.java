package com.zyh.spring.take_away.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.spring.take_away.pojo.Employee;

import java.util.List;

public interface EmployeeService extends IService<Employee> {
    Page<Employee> pageSelect(Page<Employee> employeePage, String name);

}
