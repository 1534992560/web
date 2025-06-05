package com.example.account.service.impl;

import com.example.account.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String from;
    
    // 存储邮箱和验证码的映射，实际应用中应该使用Redis
    private final Map<String, String> verificationCodes = new HashMap<>();
    
    @Override
    public String sendVerificationCode(String to) {
        return sendVerificationCode(to, false);
    }

    @Override
    public String sendVerificationCode(String to, boolean isPasswordReset) {
        try {
            // 生成6位随机验证码
            String code = generateVerificationCode();
            
            // 创建邮件消息
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            
            if (isPasswordReset) {
                message.setSubject("家庭财务管理系统 - 找回密码验证码");
                message.setText("您正在进行找回密码操作，验证码是: " + code + 
                              "\n验证码有效期为5分钟，如非本人操作，请忽略此邮件。");
            } else {
                message.setSubject("家庭财务管理系统 - 注册验证码");
                message.setText("您的注册验证码是: " + code + 
                              "\n验证码有效期为5分钟，请尽快完成注册。");
            }
            
            // 发送邮件
            mailSender.send(message);
            
            // 保存验证码
            verificationCodes.put(to, code);
            
            // 5分钟后删除验证码
            new Thread(() -> {
                try {
                    Thread.sleep(5 * 60 * 1000);
                    verificationCodes.remove(to);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @Override
    public boolean verifyCode(String email, String code) {
        String savedCode = verificationCodes.get(email);
        if (savedCode == null) {
            return false;
        }
        boolean isValid = savedCode.equals(code);
        if (isValid) {
            // 验证成功后立即删除验证码，防止重复使用
            verificationCodes.remove(email);
        }
        return isValid;
    }
    
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 