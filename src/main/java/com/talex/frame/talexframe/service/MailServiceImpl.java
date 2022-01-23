package com.talex.frame.talexframe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * <br /> {@link com.talex.frame.talexframe.service Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/23 20:08 <br /> Project: TalexFrame <br />
 */
@Component
public class MailServiceImpl {

    public static MailServiceImpl INSTANCE;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMailOwner;

    public MailServiceImpl() {

        INSTANCE = this;

    }

}
