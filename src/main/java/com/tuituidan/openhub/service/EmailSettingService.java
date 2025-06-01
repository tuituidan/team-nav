package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.EmailSetting;
import com.tuituidan.openhub.repository.EmailSettingRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * SettingService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/27 0027
 */
@Service
public class EmailSettingService implements ApplicationRunner {

    @Resource
    private EmailSettingRepository emailSettingRepository;

    private JavaMailSender javaMailSender;

    @Resource
    private SettingService settingService;

    @Override
    public void run(ApplicationArguments args) {
        List<EmailSetting> list = emailSettingRepository.findAll();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        initEmailSetting(list.get(0));
    }

    private void initEmailSetting(EmailSetting setting) {
        if (StringUtils.isBlank(setting.getHost())) {
            this.javaMailSender = null;
            return;
        }
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(setting.getHost());
        sender.setPort(setting.getPort());
        sender.setUsername(setting.getUsername());
        sender.setPassword(setting.getPassword());
        sender.setProtocol(setting.getProtocol());
        sender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.ocketFactoryClass", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(properties);
        this.javaMailSender = sender;
    }

    /**
     * get
     *
     * @return EmailSetting
     */
    public EmailSetting get() {
        return emailSettingRepository.findAll().stream().findFirst().orElse(new EmailSetting().setId("setting-id"));
    }

    /**
     * save
     *
     * @param setting setting
     */
    public void saveSetting(EmailSetting setting) {
        setting.setId("email-setting-id");
        emailSettingRepository.save(setting);
        this.initEmailSetting(setting);
    }

    /**
     * send
     *
     * @param message message
     */
    public void send(SimpleMailMessage message) {
        if (javaMailSender == null) {
            return;
        }
        EmailSetting setting = get();
        message.setSubject(settingService.getSettingCache().getNavName());
        Assert.hasText(setting.getUsername(), "请先在系统设置中配置邮箱");
        message.setFrom(setting.getUsername());
        javaMailSender.send(message);
    }

}
