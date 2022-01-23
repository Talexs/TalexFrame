package com.talex.frame.talexframe.service;

import com.google.common.util.concurrent.RateLimiter;
import com.talex.frame.talexframe.function.talex.TFrame;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <br /> {@link com.talex.frame.talexframe.service Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/23 23:51 <br /> Project: TalexFrame <br />
 */
@Getter
public class RateLimiterManager {

    private final TFrame tframe = TFrame.tframe;

    public RateLimiterManager() {



    }

    private RateLimiter globalLimiter = RateLimiter.create(100);

    private Map<Class, RateLimiter> classLimiterMapper = new HashMap<>(32);
    private Map<Method, RateLimiter> methodLimiterMapper = new HashMap<>(32);

}
