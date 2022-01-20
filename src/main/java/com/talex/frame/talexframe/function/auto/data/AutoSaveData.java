package com.talex.frame.talexframe.function.auto.data;

import cn.hutool.json.JSONObject;
import com.talex.frame.talexframe.pojo.annotations.TAutoSave;
import com.talex.frame.talexframe.function.talex.FrameData;
import com.talex.frame.talexframe.wrapper.WrappedData;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * 自动保存数据类
 * <br /> {@link com.talex.frame.talexframe.function.auto.data Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 14:49 <br /> Project: TalexFrame <br />
 */
public class AutoSaveData extends FrameData {

    public AutoSaveData(String provider) {

        super(provider);
    }

    @SneakyThrows
    public static WrappedData<?> deserialize(Class<? extends AutoSaveData> clz, JSONObject json) {

        AutoSaveData type = clz.newInstance();

        for( Field field : type.getClass().getDeclaredFields() ) {

            TAutoSave as = field.getAnnotation(TAutoSave.class);

            if(as == null) { continue; }

            if(!json.containsKey(field.getName())) {

                if(!as.canIgnore()) { throw new IllegalArgumentException("Cannot auto load data because " + field + " is not in provide data."); }

            }

            if(!field.isAccessible()) { field.setAccessible(true); }

            if( as.String() ) {

                Constructor<?> constructor = field.getType().getConstructor(String.class);

                constructor.setAccessible(true);

                field.set(type, constructor.newInstance(json.getStr(field.getName())));

            } else {

                field.set(type, json.get(field.getName(), field.getType()));

            }

        }

        if(type instanceof IDataLoaded ) {

            ( (IDataLoaded) type ).onDataLoaded();

        }

        return new WrappedData<>(type);

    }

    @SneakyThrows
    @Override
    public JSONObject toJSONObject() {

        JSONObject json = new JSONObject();

        Class<?> clz = getClass();

        for( Field field : clz.getDeclaredFields() ) {

            TAutoSave as = field.getAnnotation(TAutoSave.class);

            if(as == null) { continue; }

            if(!field.isAccessible()) {

                field.setAccessible(true);

            }

            if(as.String()) {

                json.putOpt(field.getName(), field.get(this).toString());

            } else {

                json.putOpt(field.getName(), field.get(this));

            }

        }

        return json;

    }

}
