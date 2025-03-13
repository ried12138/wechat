package xin.pwdkeeper.wechat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.WechatUserInfo;
import xin.pwdkeeper.wechat.mapper.WechatUserInfoMapper;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private WechatUserInfoMapper wechatUserInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 用户登录状态校验
     * 用户账号密码校验
     * @param userOpenId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userOpenId) throws UsernameNotFoundException {
        WechatUserInfo user = wechatUserInfoMapper.selectByUserOpenId(userOpenId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userOpenId: " + userOpenId);
        }
        String password = redisTemplate.opsForValue().get(RedisKeysUtil.VERIFY_CODE_KEY + user.getUserOpenId());
        if (password == null) {
            //这里出现提示错误，需要结合前端来进行修改，最好是以不报错的方式返回正确的json方式通知前端用户的密码失效了
            return new org.springframework.security.core.userdetails.User(user.getUserOpenId(), password, true, false, true, true, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//            throw new UsernameNotFoundException("Password not found for userOpenId: " + userOpenId);
        }
        //Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))默认写了用户权限，全部为统一权限
        return new org.springframework.security.core.userdetails.User(user.getUserOpenId(), password, true, true, true, true,  Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}