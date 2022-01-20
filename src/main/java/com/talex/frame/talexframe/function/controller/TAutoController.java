package com.talex.frame.talexframe.function.controller;

import com.talex.frame.talexframe.function.repository.TAutoRepository;
import com.talex.frame.talexframe.function.repository.TRepository;
import com.talex.frame.talexframe.function.talex.TFrame;

/**
 * <br /> {@link com.talex.frame.talexframe.function.controller Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 18:51 <br /> Project: TalexFrame <br />
 */
public class TAutoController<T extends TAutoRepository<?>> extends TController {

    private T repository;
    private final String repositoryName;

    /**
     * @param provider Controller名
     * @param repositoryName 一定是 Repository 的TablName
     */
    public TAutoController(String provider, String repositoryName) {

        super(provider);

        this.repositoryName = repositoryName;

    }

    @Override
    public TRepository getRepository() {

        return TFrame.tframe.getRepositoryManager().getRepositories().get(repositoryName);
    }

}
