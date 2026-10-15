package xin.pwdkeeper.wechat.customizeService;

import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;

/**
 * 学习码(防盗版授权)服务
 * 提供三种能力：
 * 1. 用户在微信公众号回复“学习码”时生成验证码（XXM+6位随机数字），存入Redis并绑定发送人openId；
 * 2. 请求方拿验证码来校验，校验通过则删除Redis中的验证码，并返回加密后的授权凭证（验证码+openId）；
 * 3. 请求方每11分钟携带凭证来进行心跳校验，解密、轮转验证码并重新加密返回新凭证。
 *
 * @Author weiranliu
 * @Version 1.0
 */
public interface LicenseService {

    /**
     * 生成学习验证码，并将 验证码->openId 写入Redis（有效期5分钟）
     * @param request 需包含 openId（消息发送人）
     * @return 包含验证码的结果体
     */
    R generateLicenseCode(RequestParams request);

    /**
     * 校验学习验证码，通过后删除Redis中的验证码并返回加密授权凭证
     * @param request requestParam 中需包含 licenseCode
     * @return 包含加密凭证 licenseToken 的结果体
     */
    R verifyLicenseCode(RequestParams request);

    /**
     * 心跳校验：解密凭证、校验时间戳、轮转验证码后重新加密返回新凭证
     * @param request requestParam 中需包含 licenseToken
     * @return 包含新加密凭证 licenseToken 的结果体
     */
    R heartbeatLicense(RequestParams request);
}
