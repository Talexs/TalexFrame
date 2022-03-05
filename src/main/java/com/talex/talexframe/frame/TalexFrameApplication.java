package com.talex.talexframe.frame;

import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import com.talex.talexframe.frame.core.talex.TFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author TalexDreamSoul
 */
@SpringBootApplication
public class TalexFrameApplication {

    public static final long startedTimeStamp = System.nanoTime();
    public static ConfigurableApplicationContext context;

    public static void start(String[] args) {

        context = SpringApplication.run(TalexFrameApplication.class, args);

    }

    /** 每十五分钟从 TFrame 获取一次mysql实例 执行 checkStatus 查询状态 如果前面获取的内容为null则不执行 **/
    @Scheduled( cron = "0 0/15 0/1 * * ?")
    public void mysqlChecker() {

        TFrame tframe = TFrame.tframe;

        MysqlManager mysql = tframe.getMysqlManager();

        if( mysql == null ) return;

        mysql.checkStatus();

    }

}
