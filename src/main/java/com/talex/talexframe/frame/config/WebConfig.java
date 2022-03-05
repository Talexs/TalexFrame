package com.talex.talexframe.frame.config;

import com.talex.talexframe.frame.core.modules.network.interceptor.TimeConsumingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <br /> {@link com.talex.talexframe.frame.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 11:07 <br /> Project: TalexFrame <br />
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new TimeConsumingInterceptor()).addPathPatterns("/**");

    }

}
