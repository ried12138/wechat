package xin.pwdkeeper.wechat.customizeService;

import io.minio.errors.*;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
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
    R addUserInfoData(RequestParams request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    R removeUserInfoData(RequestParams request);

    R alterUserInfoData(RequestParams request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    R fetchUserInfoDataPage(RequestParams request);

    R signOut(RequestParams request);

    R getDecryptDate(RequestParams request);

    R updateCompleteUserInfo(RequestParams request);

    R getUserInfo(RequestParams request);
}
