package com.talexframe.frame.core.modules.schedule;

import com.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import com.talexframe.frame.core.talex.TFrame;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <br /> {@link com.talexframe.frame.core.modules.schedule Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 06:29 <br /> Project: TalexFrame <br />
 */
@Component
public class FrameRunnable {

    protected TFrame tFrame = TFrame.tframe;
    // protected

    // private Runnable runnable;
    //
    // adapt FrameRunnable(Runnable runnable) {
    //
    //     this.runnable = runnable;
    //
    // }
    //
    // @Async
    // adapt void runAsync() {
    //
    //     this.runnable.run();
    //
    // }
    //
    // @Async
    // adapt void runDelayAsync(long delayMS) {
    //
    //     ThreadUtil.
    //
    // }

    /**
     * 每十五分钟从 TFrame 获取一次mysql实例 执行 checkStatus 查询状态 如果前面获取的内容为null则不执行
     **/
    @Scheduled( cron = "0 0/15 0/1 * * ?" )
    public void mysqlChecker() {

        Mysql mysql = new DAOManager.ProcessorGetter<Mysql>(Mysql.class).getProcessor();

        mysql.checkStatus();

    }

}
