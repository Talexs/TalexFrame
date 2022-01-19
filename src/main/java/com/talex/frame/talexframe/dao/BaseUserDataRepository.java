package com.talex.frame.talexframe.dao;

import com.talex.frame.talexframe.entity.BaseUser;
import com.talex.frame.talexframe.mapper.IData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * 基本数据存储库
 * <br /> {@link com.talex.frame.talexframe.dao Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 11:19 <br /> Project: TalexFrame <br />
 */
@Repository
public interface BaseUserDataRepository<T extends BaseUser> extends JpaRepository<T, String> {

    /**
     *
     * 通过ID获取数据
     *
     * @param id 即UUID
     *
     */
    @NonNull
    @Override
    T getById(@NonNull String id);

}
