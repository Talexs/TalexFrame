package com.talex.frame.talexframe.entity;

import cn.dev33.satoken.secure.SaBase64Util;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONTokener;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.HashMap;

/**
 * User的包装类
 * <br /> {@link com.talex.frame.talexframe.entity Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 9:58 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@ToString
public class PackedUser extends BaseUser {

    /**
     *
     * 包装用户属性 整合入 ExtraInformation
     *
     */
    protected JSONObject properties = new JSONObject();

    @Override
    public String getExtraInformation() {

        String str = properties.toJSONString(2, null);

        this.extraInformation = SaBase64Util.encode(str);

        return super.getExtraInformation();
    }

    @Override
    public PackedUser setExtraInformation(String extraInformation) {

        super.setExtraInformation(extraInformation);

        String str = SaBase64Util.decode(this.extraInformation);

        if( StrUtil.isBlankIfStr(str) ) { return this; }

        this.properties = JSONUtil.parseObj(str);

        return this;

    }

    public BaseUser unPack() {

        return this;

    }

}
