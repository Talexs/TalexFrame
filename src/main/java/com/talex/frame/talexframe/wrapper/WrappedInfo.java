package com.talex.frame.talexframe.wrapper;

import com.talex.frame.talexframe.function.plugins.core.PluginInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.jar.JarFile;

/**
 * <br /> {@link com.talex.frame.talexframe.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/25 16:03 <br /> Project: TalexFrame <br />
 */
@Getter
@AllArgsConstructor
public class WrappedInfo {

    private PluginInfo info;
    private JarFile jarFile;

}
