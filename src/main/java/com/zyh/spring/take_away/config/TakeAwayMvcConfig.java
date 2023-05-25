package com.zyh.spring.take_away.config;

import com.zyh.spring.take_away.commen.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Slf4j
@Configuration
public class TakeAwayMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始映射静态资源文件");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建一个消息转换器
        MappingJackson2HttpMessageConverter MessageConverter = new MappingJackson2HttpMessageConverter();
        //将直接的json映射器传入消息转换器中
        MessageConverter.setObjectMapper(new JacksonObjectMapper());
//        将自己的消息转换器加入springmvc
        converters.add(0,MessageConverter);
    }


}
