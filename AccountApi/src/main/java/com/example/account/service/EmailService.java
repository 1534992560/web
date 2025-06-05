package com.example.account.service;

public interface EmailService {
    /**
     * 发送验证码邮件
     * @param to 收件人
     * @return 生成的验证码
     */
    String sendVerificationCode(String to);

    /**
     * 发送验证码邮件
     * @param to 收件人
     * @param isPasswordReset 是否是重置密码的验证码
     * @return 生成的验证码
     */
    String sendVerificationCode(String to, boolean isPasswordReset);
    
    /**
     * 验证验证码是否正确
     * @param email 邮箱
     * @param code 验证码
     * @return 是否正确
     */
    boolean verifyCode(String email, String code);
} 