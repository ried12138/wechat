package xin.pwdkeeper.wechat.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xin.pwdkeeper.wechat.bean.*;
import xin.pwdkeeper.wechat.mapper.AccountInfoMapper;
import xin.pwdkeeper.wechat.mapper.FileDataInfoMapper;
import xin.pwdkeeper.wechat.mapper.WechatUserInfoMapper;
import xin.pwdkeeper.wechat.service.MinioService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 图片上传服务，只存入， 没有实际的删除逻辑，
 * 上传成功之后，文件将永久保存到服务器
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/31   11:31
 * @Version 1.0
 */
@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private FileDataInfoMapper fileDataInfoMapper;

    @Autowired
    private WechatUserInfoMapper wechatUserInfoMapper;

    @Value("${minio.bucket}")
    private String bucketName;


    /**
     * 上传图片
     * @return
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    @Override
    public R uploadFile(RequestParams request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        AccountInfo accountInfo = (AccountInfo)request.getRequestParam();
        FileRequestBean fileRequestBean = accountInfo.getFileRequestBean();
        if (fileRequestBean == null){
            return R.ok("文件信息为空,无图片可以上传");
        }
        String base64Image = fileRequestBean.getBase64Image();
        //去掉 Base64 数据头部（如果有的话）
        String imageData = base64Image;
        String extension = "";
        if (imageData.contains(",")) {
            String[] split = imageData.split(",");
            imageData = split[1]; // 去掉 "image/png,base64Code" 这样的头部
            extension = split[0];
        }else{
            return R.failed("base64Image字段内容格式不正确，请按照API对接文档对接数据");
        }
        WechatUserInfo wechatUserInfo = wechatUserInfoMapper.selectByUserOpenId(request.getOpenId());
        if (wechatUserInfo == null){
            return R.failed(null,"用户不存在");
        }
        String objectName = fileRequestBean.getObjectName();
        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        // 将字节数组包装为 InputStream
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        //存储文件名要进行唯一标识
        String storageName = objectName + "@" + wechatUserInfo.getUserOpenId();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(storageName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(extension)
                            .build());
        } catch (Exception e) {
            log.error("上传文件时发生错误", e);
            return R.failed("上传文件时发生错误: " + e.getMessage());
        }
        //上传mysql
        FileDataInfo fileDataInfo = new FileDataInfo(null, wechatUserInfo.getId(),accountInfo.getId(), storageName, null, bucketName, null, null, 1);
        if (fileDataInfoMapper.insert(fileDataInfo) == 1){
            log.info("数据变动：file_data_info表中有"+1+"条数据被插入");
        }
        return R.ok("文件上传成功");
    }

    /**
     * 获取图片url
     * @param request
     * @return
     */
    @Override
    public R getImageUrl(RequestParams request) {
        WechatUserInfo wechatUserInfo = wechatUserInfoMapper.selectByUserOpenId(request.getOpenId());
        if (wechatUserInfo == null){
            return R.failed(null,"用户不存在");
        }
        AccountInfo accountInfo = (AccountInfo)request.getRequestParam();
        FileDataInfo fileDataInfo = fileDataInfoMapper.selectByOpenId(accountInfo.getId());
        if (fileDataInfo == null){
            return R.ok();
        }
        HashMap<String, String> map = new HashMap<>();
        try {
            String url = generateAccessUrl(fileDataInfo.getFileName());
            map.put("imageUrl",url);
            map.put("objectName",fileDataInfo.getFileName().split("@")[0]);
        } catch (Exception e) {
            R.failed(null,"生成url失败");
        }
        return R.ok(map);
    }

    /**
     * 删除图片,数据库中标记flag 不被查询出出来
     * @param request
     * @return
     */
    @Transactional
    @Override
    public R imageDelete(RequestParams request) {
        WechatUserInfo wechatUserInfo = wechatUserInfoMapper.selectByUserOpenId(request.getOpenId());
        if (wechatUserInfo == null){
            return R.failed(null,"用户不存在");
        }
        AccountInfo accountInfo = (AccountInfo) request.getRequestParam();
        FileDataInfo fileDataInfo = fileDataInfoMapper.selectByOpenId(accountInfo.getId());
        if (fileDataInfo == null){
            return R.ok("没有要删除的图片");
        }
        if (fileDataInfoMapper.updateFlagStatus(fileDataInfo) == 1){
            return R.ok("删除成功");
        }
        return R.failed(null,"删除失败");
    }

    /**
     * 生成临时文件访问url 默认url有效期7天
     * @param objectName
     * @return
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    private String generateAccessUrl(String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String minioUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(1, TimeUnit.DAYS) // 设置有效期为 7 天
                        .build()
        );
        //如果使用nginx代理，则将注释掉的代码放开，就可以通过ngxin代理访问图片
//        String nginxProxyUrl = "http://www.pwdkeeper.xin/imageMinio/" + bucketName + "/" + objectName;
//        return nginxProxyUrl;
        return minioUrl;
    }
}