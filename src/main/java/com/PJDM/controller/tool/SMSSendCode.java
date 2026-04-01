package com.PJDM.controller.tool;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.google.gson.Gson;
import com.PJDM.config.AliyunSmsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 短信发送验证码控制器
 *
 * @author Alanine
 * @since 2025-11-27
 */
@Component
public class SMSSendCode {

    @Autowired
    private AliyunSmsConfig aliyunSmsConfig;

    public void sendCode(String phone, String code) throws Exception {
        //java代码中操作 短信发送的提供者对象
        StaticCredentialProvider provider = StaticCredentialProvider
                .create(Credential.builder()
                        .accessKeyId(aliyunSmsConfig.getAccessKeyId())
                        .accessKeySecret(aliyunSmsConfig.getAccessKeySecret())
                        .build());
        AsyncClient client = AsyncClient.builder().region("ap-southeast-1").credentialsProvider(provider)
                .overrideConfiguration(ClientOverrideConfiguration.create().setEndpointOverride(aliyunSmsConfig.getEndpoint())).build();
        SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                .signName(aliyunSmsConfig.getSignName())
                .templateCode(aliyunSmsConfig.getTemplateCode())
                .phoneNumber(phone)
                .templateParam("{\"code\":" + code + ",\"min\":\"5\"}").build();
        CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
        SendSmsVerifyCodeResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));
        client.close();
    }
}
