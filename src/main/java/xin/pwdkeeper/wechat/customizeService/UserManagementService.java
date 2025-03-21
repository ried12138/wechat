package xin.pwdkeeper.wechat.customizeService;

import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/13   18:40
 * @Version 1.0
 */
public interface UserManagementService {
    R addUserInfoData(RequestParams request);

    R removeUserInfoData(RequestParams request);

    R alterUserInfoData(RequestParams request);

    R fetchUserInfoDataPage(RequestParams request);

    R signOut(RequestParams request);

    R getDecryptDate(RequestParams request);
}
