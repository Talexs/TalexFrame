package com.talex.talexframe.frame.core.pojo.dao.factory;

import cn.hutool.core.util.ClassUtil;
import com.talex.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <br /> {@link com.talex.talexframe.frame.core.pojo.dao.factory Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:39 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class DAOManager {

    private static final Map<Class<?>, IDataProcessor> daoMap = new HashMap<>();

    private static boolean check(Class<?> clz) {

        return IDataProcessor.class.isAssignableFrom(clz);

    }

    @SneakyThrows
    public DAOManager() {

        // start

        for( Class<?> clz : ClassUtil.scanPackage(getClass().getPackage().getName(), DAOManager::check) ) {

            IDataProcessor processor = (IDataProcessor) clz.newInstance();

            if(processor.connect()) {

                daoMap.put(clz, processor);

                log.info("[DAO] " + clz.getName() + " connected.");

            } else {

                log.warn("[DAO] " + clz.getName() + " connect failed!");

            }

        }

    }

    public void shutdown() {

        daoMap.values().forEach(IDataProcessor::disconnect);

    }

    public static class ProcessorGetter<T> {

        Class<?> clz;

        public ProcessorGetter(Class<?> clz) {

            if( !check(clz) ) {

                throw new RuntimeException("Class is not assignable from IDataProcessor");

            }

            this.clz = clz;

        }

        public T getProcessor() {

            return (T) daoMap.get(clz);

        }

    }

}
