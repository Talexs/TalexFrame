package com.talex.frame.talexframe.function.controller;

import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import com.talex.frame.talexframe.function.repository.TAutoRepository;
import com.talex.frame.talexframe.function.repository.TRepository;
import com.talex.frame.talexframe.function.talex.TFrame;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;

/**
 * <br /> {@link com.talex.frame.talexframe.function.controller Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 18:51 <br /> Project: TalexFrame <br />
 */
@Getter
public class TAutoController<T extends TAutoRepository<?>> extends TController {

    /**
     *
     * 如果使用的是 TAutoController 那么应该调用这个 repository
     *
     */
    protected T asRepository;
    private final String repositoryName;
    private final WebPlugin ownPlugin;
    private Class<T> templateData;

    /**
     * @param repositoryName 一定是 Repository 的TablName
     * @param webPlugin 所属插件
     */
    public TAutoController(String repositoryName, WebPlugin webPlugin) {

        super(repositoryName);

        this.repositoryName = repositoryName;
        this.ownPlugin = webPlugin;

    }

    @Override
    public TRepository getRepository() {

        this.templateData = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        asRepository = (T) TFrame.tframe.getRepositoryManager().getRepositories().get(templateData);

        return asRepository;
    }

}
