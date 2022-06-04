package com.talexframe.frame.core.modules.plugins.adapt;

import com.google.common.annotations.Beta;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.pojo.FrameBuilder;
import com.talexframe.frame.core.talex.FrameAnno;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.core.talex.TFrame;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

/**
 * {@link com.talexframe.frame.core.modules.plugins.adapt Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 08:20 Project: TalexFrame
 */
public abstract class PluginCompAdapter<T extends FrameCreator> extends FrameBuilder {

    protected final TFrame tframe = TFrame.tframe;
    private final Class<?> templateClass = (Class<T>) ( (ParameterizedType) this.getClass().getGenericSuperclass() ).getActualTypeArguments()[0];;

    public PluginCompAdapter() {

        super("ComponentAdapter");

        PluginScanner.adapt( this );

    }

    /** GetPriority **/
    public int getPriority() {

        return 1000;

    }

    private FrameAnno anno = null;

    @Beta
    public void setAnno(FrameAnno anno) {

        this.anno = anno;

    }

    /**
     *
     * For true for access, for false for deny
     *
     */
    public boolean shouldAccess(Class<? extends FrameCreator> clazz) {

        boolean res = templateClass.isAssignableFrom(clazz);

        if( res ) return true;

        if( anno != null ) {

            Annotation thisAnno = clazz.getAnnotation( anno.getClass() );

            return thisAnno != null;

        }

        return false;

    }

    @SneakyThrows
    public boolean inject( PluginScanner scanner, WebPlugin webPlugin, Object obj ) {

        // Method method = this.getClass().getMethod("injectWithInstance", PluginScanner.class, WebPlugin.class, templateClass);

        // method.invoke( this, scanner, webPlugin, (T) obj );

        return this.injectWithInstance(scanner, webPlugin, (T) obj);

    }

    /**
     *
     * Trigger after all class scanned
     *
     * @param scanner For the plugin scanner
     * @param webPlugin For the own plugin
     * @param instance For the instance
     *
     * @return should return a signal of provided instance, if the signal is false, it will have a warning if mode is DEBUG
     *
     */
    protected abstract boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, T instance );

    /**
     *
     * Trigger when plugin disabled
     *
     * @param scanner For the plugin scanner
     * @param webPlugin For the own plugin
     * @param clazz For the class
     *
     * @return If you return false, the frame will be crashed
     */
    public abstract boolean logoutInstance( PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz );

}
