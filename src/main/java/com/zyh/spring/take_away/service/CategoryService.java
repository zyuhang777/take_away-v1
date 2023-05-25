package com.zyh.spring.take_away.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.spring.take_away.pojo.Category;

public interface CategoryService extends IService<Category> {
    boolean removeCategoryById(Long id);
}
