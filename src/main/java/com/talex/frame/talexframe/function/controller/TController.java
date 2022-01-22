package com.talex.frame.talexframe.function.controller;

import com.talex.frame.talexframe.function.repository.TRepository;
import com.talex.frame.talexframe.function.talex.FrameCreator;
import com.talex.frame.talexframe.function.talex.TFrame;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.controller Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:54 <br /> Project: TalexFrame <br />
 */
@Getter
public abstract class TController extends FrameCreator {

    protected TFrame tframe = TFrame.tframe;
    protected TRepository repository;

    public TController(String provider) {

        super("TCONTROLLER", provider);

        this.repository = getRepository();

        if( this.repository == null ) {

            throw new RuntimeException("无法找到 Repository");

        }

    }

    public abstract TRepository getRepository();

}
