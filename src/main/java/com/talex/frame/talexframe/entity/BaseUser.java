package com.talex.frame.talexframe.entity;

import com.talex.frame.talexframe.mapper.IData;
import lombok.*;
import lombok.experimental.Accessors;

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
@Accessors( chain = true)
public class BaseUser implements IData {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "id", nullable = false )
    protected String id;

    @NotNull(message = "用户名不能为空")
    protected String username;

    protected String extraInformation;

    public PackedUser pack() {

        PackedUser pack = new PackedUser().setExtraInformation(this.extraInformation);

        pack.setUsername(this.username);
        pack.setId(this.id);

         return pack;

    }

}
