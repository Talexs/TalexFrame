package com.talexframe.frame.core.modules.application;

import com.talexframe.frame.core.modules.repository.TRepo;
import com.talexframe.frame.core.modules.repository.TRepoManager;
import com.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul
 * 2022/1/20 16:54 <br /> Project: TalexFrame <br />
 */
@Getter
public abstract class TApp extends FrameCreator {

    protected final TFrame tframe = TFrame.tframe;
    protected FrameSender frameSender = tframe.getFrameSender();
    protected TAppManager controllerManager = tframe.getAppManager();
    protected TRepoManager repositoryManager = tframe.getRepoManager();
    protected TRepo repo;

    public TApp(String provider) {

        super("TAPP", provider);

        this.repo = this.getRepo();

        // 不强制要求 Repository

        // if ( this.repo == null ) {

            // throw new RuntimeException("无法找到 Repo");

        // }

    }

    public abstract TRepo getRepo();

    public String getTableName() {

        return this.repo.getProvider();

    }

}
