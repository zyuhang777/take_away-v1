package com.zyh.spring.take_away.config;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Configuration
@EnableKnife4j//增强版knife4j用到
@EnableSwagger2
public class Swagger {
    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage("com.github.xiaoymin.knife4j.controller"))
                //这里指定扫描有ApiOperation注解的类
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //所有路径
                .paths(PathSelectors.any())
                //不包含^/inner/.*的路径
                //.paths(input -> !input.matches("^/inner/.*"))
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("菜面的RestfulApi")//标题
                .description("测试使用")//描述
                .termsOfServiceUrl("https://blog.csdn.net/zyhk__?spm=1010.2135.3001.5343")//应用遵守的条款
                .contact(new Contact("zyh",
                        "https://blog.csdn.net/zyhk__?spm=1010.2135.3001.5343",
                        "2952160251@qq.com"))//联系人信息
                .version("1.0")//版本号
                .build();
    }
}
