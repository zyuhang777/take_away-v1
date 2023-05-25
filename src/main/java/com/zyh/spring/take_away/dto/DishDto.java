package com.zyh.spring.take_away.dto;

import com.zyh.spring.take_away.pojo.Dish;
import com.zyh.spring.take_away.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
