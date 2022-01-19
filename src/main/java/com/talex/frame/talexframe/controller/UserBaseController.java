package com.talex.frame.talexframe.controller;

import com.talex.frame.talexframe.dao.BaseUserDataRepository;
import com.talex.frame.talexframe.entity.BaseUser;
import com.talex.frame.talexframe.mapper.user.IUserDataMapper;
import com.talex.frame.talexframe.wrapper.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对基本的用户行为服务
 * <br /> {@link com.talex.frame.talexframe.controller Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 9:27 <br /> Project: TalexFrame <br />
 */
@RestController
public class UserBaseController {

    @Autowired
    private BaseUserDataRepository<BaseUser> userRepository;
//    private IUserDataMapper userDataMapper;

    @PostMapping("/api/user/login")
    public ResultData<String> login() {

        return ResultData.SUCCESS("1");

    }

    @PostMapping("/api/user/list")
    public ResultData<List<BaseUser>> list() {

        return ResultData.SUCCESS(userRepository.findAll());

    }

}
