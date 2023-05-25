package com.zyh.spring.take_away.controller;

import com.zyh.spring.take_away.commen.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "公共资源处理器")
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${img.path}")
    String path;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R<String> upload(@RequestBody MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        //为文件重命名防止重名
        assert originalFilename != null;
        int i = originalFilename.lastIndexOf(".");
        String uuid = UUID.randomUUID().toString();
        originalFilename = uuid + originalFilename.substring(i);

        //判断文件目录是否存在（防止后续更改配置文件）
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            file.transferTo(new File(path + originalFilename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(originalFilename);
    }

    @ApiOperation("页面图片的下载")
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        File file = new File(path + name);

        FileInputStream fileInputStream;
        response.setContentType("image/jpeg");//设置响应给浏览器的数据类型
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes1 = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes1)) != -1) {
                response.getOutputStream().write(bytes1,0,len);
                response.getOutputStream().flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
