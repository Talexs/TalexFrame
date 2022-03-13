package com.talexframe.frame.core.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * <br /> {@link com.talexframe.frame.service Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/23 20:08 <br /> Project: TalexFrame <br />
 */
@Component
@Getter
public class MailServiceImpl {

    public static MailServiceImpl INSTANCE;

    private final JavaMailSender mailSender;

    @Value( "${spring.mail.username}" )
    private String fromMailOwner;

    public MailServiceImpl(JavaMailSender mailSender) {

        INSTANCE = this;

        this.mailSender = mailSender;
    }

}
