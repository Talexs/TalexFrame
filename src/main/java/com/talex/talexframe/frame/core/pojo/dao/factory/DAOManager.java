package com.talex.talexframe.frame.core.pojo.dao.factory;

import cn.hutool.core.util.ClassUtil;
import com.talex.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import com.talex.talexframe.frame.core.talex.TFrame;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * <br /> {@link com.talex.talexframe.frame.core.pojo.dao.factory Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:39 <br /> Project: TalexFrame <br />
 */
public class DAOManager {

    private final TFrame tframe = TFrame.tframe;

    private static final Map<Class<?>, IDataProcessor> daoMap = new HashMap<>();

    @SneakyThrows
    public DAOManager() {

        // start

        for( Class<?> clz : ClassUtil.scanPackage(getClass().getName(), aClass -> aClass.isAssignableFrom(IDataProcessor.class)) ) {

            IDataProcessor processor = (IDataProcessor) clz.newInstance();

            processor.connect();

            this.daoMap.put(clz, processor);

        }

    }

    public void shutdown() {

        daoMap.values().forEach(IDataProcessor::disconnect);

    }

    public static class ProcessorGetter<T> {

        public T getProcessor() {

            return (T) daoMap.get(((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

        }

    }

}
