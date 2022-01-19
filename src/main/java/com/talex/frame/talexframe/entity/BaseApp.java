package com.talex.frame.talexframe.entity;

import com.talex.frame.talexframe.mapper.IData;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 基本Application应用 # 为开放平台设置
 * <br /> {@link com.talex.frame.talexframe.mapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/15 22:48 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "app")
public class BaseApp implements IData {

    @Id
    @Column( name = "id", nullable = false )
    private String id;

    private String username, password, uuid;

}
