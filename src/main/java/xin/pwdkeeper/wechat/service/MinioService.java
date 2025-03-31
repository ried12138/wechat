package xin.pwdkeeper.wechat.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/31   11:31
 * @Version 1.0
 */
public interface MinioService {
    R uploadFile(RequestParams request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    R getImageUrl(String openId);

    R imageDelete(String openId);
}
