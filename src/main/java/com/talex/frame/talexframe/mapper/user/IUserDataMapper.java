package com.talex.frame.talexframe.mapper.user;

import com.talex.frame.talexframe.entity.BaseUser;
import com.talex.frame.talexframe.mapper.IData;
import com.talex.frame.talexframe.mapper.IDataMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 对User的数据管理
 * <br /> {@link com.talex.frame.talexframe.mapper.user Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 9:43 <br /> Project: TalexFrame <br />
 */
@Repository
public interface IUserDataMapper extends IDataMapper {

    /**
     *
     * 向数据库中插入一条记录
     *
     * @param user 基本用户
     */
    @Insert("Insert Into talex_user(uuid, username, password, extra_info) Value(#{id}, #{password}, #{password}, #{extraInformation})")
    @Options(keyProperty = "uuid")
    void insert(BaseUser user);

    /**
     *
     * 通过 UUID 获得一个用户
     *
     * @param uuid UUID
     *
     */
    @Select("Select 1 From talex_user Where uuid=#{id}")
    BaseUser selectUserByUUID(String uuid);

    /**
     *
     * 通过 UserName 获得一个用户
     *
     * @param username UserName
     *
     */
    @Select("Select 1 From talex_user Where username=#{username}")
    BaseUser selectUserByUserName(String username);

}
