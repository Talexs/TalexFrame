package com.talex.talexframe.frame.core.modules.schedule;

import com.talex.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import com.talex.talexframe.frame.core.talex.TFrame;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <br /> {@link com.talex.talexframe.frame.core.modules.schedule Package }
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
    // public FrameRunnable(Runnable runnable) {
    //
    //     this.runnable = runnable;
    //
    // }
    //
    // @Async
    // public void runAsync() {
    //
    //     this.runnable.run();
    //
    // }
    //
    // @Async
    // public void runDelayAsync(long delayMS) {
    //
    //     ThreadUtil.
    //
    // }

    /** 每十五分钟从 TFrame 获取一次mysql实例 执行 checkStatus 查询状态 如果前面获取的内容为null则不执行 **/
    @Scheduled( cron = "0 0/15 0/1 * * ?")
    public void mysqlChecker() {

        Mysql mysql = new DAOManager.ProcessorGetter<Mysql>().getProcessor();

        mysql.checkStatus();

    }

}
