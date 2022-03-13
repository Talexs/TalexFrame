package com.talexframe.frame.core.modules.controller;

import com.talexframe.frame.core.modules.repository.TRepository;
import com.talexframe.frame.core.modules.repository.TRepositoryManager;
import com.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.function.controller Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:54 <br /> Project: TalexFrame <br />
 */
@Getter
public abstract class TController extends FrameCreator {

    protected final TFrame tframe = TFrame.tframe;
    protected FrameSender frameSender = tframe.getFrameSender();
    protected TControllerManager controllerManager = tframe.getControllerManager();
    protected TRepositoryManager repositoryManager = tframe.getRepositoryManager();
    protected TRepository repository;

    public TController(String provider) {

        super("TCONTROLLER", provider);

        this.repository = getRepository();

        if ( this.repository == null ) {

            throw new RuntimeException("无法找到 Repository");

        }

    }

    public abstract TRepository getRepository();

    public String getTableName() {

        return this.repository.getProvider();

    }

}
