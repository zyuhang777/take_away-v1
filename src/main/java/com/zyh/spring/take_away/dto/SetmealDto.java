package com.zyh.spring.take_away.dto;

import com.zyh.spring.take_away.pojo.Setmeal;
import com.zyh.spring.take_away.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
