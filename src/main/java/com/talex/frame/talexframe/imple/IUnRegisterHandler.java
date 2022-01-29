package com.talex.frame.talexframe.imple;

/**
 * 实现本接口后可实现注销时的处理
 * 目前仅支持 Controller 与 Repository
 * <br /> {@link com.talex.frame.talexframe.imple Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 1:48 <br /> Project: TalexFrame <br />
 */
public interface IUnRegisterHandler {

    void onUnRegister();

}