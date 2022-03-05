package com.talex.talexframe.frame.core.modules.controller;

import com.talex.talexframe.frame.core.modules.repository.TRepository;
import com.talex.talexframe.frame.core.modules.repository.TRepositoryManager;
import com.talex.talexframe.frame.core.talex.FrameCreator;
import com.talex.talexframe.frame.core.talex.TFrame;
import com.talex.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.controller Package }
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

        if( this.repository == null ) {

            throw new RuntimeException("无法找到 Repository");

        }

    }

    public abstract TRepository getRepository();

    public String getTableName() {

        return this.repository.getProvider();

    }

}
