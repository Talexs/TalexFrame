package com.talex.frame.talexframe.config;

import com.talex.frame.talexframe.interceptor.TimeConsumingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <br /> {@link com.talex.frame.talexframe.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 11:07 <br /> Project: TalexFrame <br />
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new TimeConsumingInterceptor()).addPathPatterns("/**");

    }

}
