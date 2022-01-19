package com.talex.frame.talexframe.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.Hutool;
import cn.hutool.core.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * ID配发器
 * <br /> {@link com.talex.frame.talexframe.service Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/15 23:31 <br /> Project: TalexFrame <br />
 */
@Service
public class IdGeneratorService {

    @Autowired
    RedisService redisService;

    /**
     *
     * 为 App 系统生成ID
     *
     * @param key App标识符
     *
     * @return 返回例子: [> TalexApp] (key的hash 前六位)-123456(时间戳后六位)-(两个随机字符 + 时间戳倒数第九至第七三位大标识符 + 一个等级字符)-自增序列
     */
    public String generateIdForApp(String key) {

        long num = redisService.increment(key);

        String hash = SaSecureUtil.md5BySalt(key, "TalexFrame-Salt");

        String timeStamp = String.valueOf(System.currentTimeMillis());

        char level = (char) ('a' + num / 999999 );
        num = num % 999999;

        StringBuilder numStr = new StringBuilder(String.valueOf(num));

        while( numStr.length() < 6 ) { numStr.append("0"); }

        return hash.substring(0, 6) + '-' +
                timeStamp.substring(7) + '-' +
                SaSecureUtil.md5(String.valueOf(hash.hashCode())).substring(0, 2) + timeStamp.substring(6, timeStamp.length() - 4) + level + '-' +
                numStr;

    }

    /**
     * 生成id（每日重置自增序列）
     * 格式：日期 + 6位自增数
     * 如：20210804000001
     * @param key 类型数据
     * @param length 长度
     * @return
     */
    public String generateId(String key, Integer length) {
        long num = redisService.increment(key, getEndTime());
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%0" + length + "d", num);
    }

    /**
     * 获取当天的结束时间
     */
    public Instant getEndTime() {

        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return endTime.toInstant(ZoneOffset.ofHours(8));

    }

}
