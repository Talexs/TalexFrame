package com.talex.talexframe.launcher;

import com.talex.talexframe.frame.TalexFrameApplication;
import com.talex.talexframe.frame.core.talex.TFrame;
import com.talex.talexframe.launcher.guide.FirstGuide;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * <br /> {@link com.talex.talexframe.launcher Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 上午 12:09 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class Launcher {

    public static void main(String[] args) {

      log.info("[TalexFrame] Launcher start");

      // 如果文件不存在则启动第一次向导

      File file = TFrame.getMainFile();

      if( !new File(file, "/config/guided.talexs").exists() ) {

          new FirstGuide();

      }

      // 进入启动 - 先启动 SpringBoot 整个应用
        TalexFrameApplication.start( args );

    }

}
