package xin.pwdkeeper.wechat.service;

import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 验证码服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/12   11:11
 * @Version 1.0
 */
public interface VerifyCodeService {
    public R parseVerifyCodeService(RequestParams request);

    public R generateVerifyCode(RequestParams request) throws NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
