package com.talex.frame.talexframe.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动保存实例
 * 标注在方法上，代表将执行这个方法并保存方法返回
 * <br /> {@link com.talex.frame.talexframe.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 15:14 <br /> Project: TalexFrame <br />
 */
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME)
public @interface TAutoSave {

    /** 如果该参数为真 则在加载时有可能为null 若为假数据不存在时会报错 **/
    boolean canIgnore() default true;

    /**
     *  是否为MySql字段 默认的数据在存储时会存储到mysql的info数据字段上 如果加入仍然会存储但会在字段中显示 <br />
     * 如果要存储数据字段仅支持 基本数据类型 以及 String
     *  字段越多性能越低
     */
    boolean isMySqlFiled() default false;

    /** 是否为主索引 **/
    boolean isMain() default false;

    /** MYSQL数据字段的类型 如果什么都不填则会由框架自动判断 建议大文本数据必填 **/
    String type() default "VARCHAR(32)";

    /** 默认内容 如果填写null 则为null 如果填写 n_null 则为 NOT NULL 其他清空为 DEFAULT defaultNull **/
    String defaultNull() default "null";

    /**
     *
     * 设置为True后每次更新都会将此值设置为 最后一次更新时间
     *
     * @return 是否更新时间
     */
    boolean update() default false;

    /**
     *
     * 当对象出现异常时请尝试开启
     * 开启意味着该类需要一个 Override 的 toString 以及一个 constructor 只有一个参数(String)
     * 注意, 开启此选项后请在类上加入注解
     *
     * @return 是否开启String模式
     */
    boolean String() default false;

    /**
     *
     * 当启用时，将会建立到表字段 UNIQUE KEY 中
     *
     */
    boolean UNIQUE_ONLY() default false;

}
