package com.talex.frame.talexframe.function.event;

import com.talex.frame.talexframe.function.talex.FrameCreator;
import com.talex.frame.talexframe.function.talex.TFrame;

/**
 * @author TalexDreamSoul
 */
public class FrameListener extends FrameCreator {

    protected TFrame tframe = TFrame.tframe;

    public FrameListener(String provider) {

        super("EventListener", provider);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
