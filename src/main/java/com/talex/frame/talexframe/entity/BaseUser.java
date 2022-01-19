package com.talex.frame.talexframe.entity;

import com.talex.frame.talexframe.mapper.IData;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 基本用户
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
@Table(name = "user")
public class BaseUser implements IData {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "id", nullable = false )
    protected String id;

    @NotNull(message = "用户名不能为空")
    protected String username;

    protected String extraInformation;

}
