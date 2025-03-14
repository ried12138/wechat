package xin.pwdkeeper.wechat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.AccountInfo;
import xin.pwdkeeper.wechat.mapper.AccountInfoMapper;
import xin.pwdkeeper.wechat.service.AccountInfoService;

import java.util.List;

@Slf4j
@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Override
    public int addAccountInfo(AccountInfo accountInfo) {
        int count = accountInfoMapper.insert(accountInfo);
        if (count > 0){
            log.info("数据变动：account_info表中有"+count+"条数据被插入");
        }
        return count;
    }

    @Override
    public AccountInfo getAccountInfoById(int id) {
        return accountInfoMapper.selectById(id);
    }

    @Override
    public List<AccountInfo> getAllAccountInfos() {
        return accountInfoMapper.selectAll();
    }

    @Override
    public int updateAccountInfo(List<AccountInfo> accountInfo) {
        int count = accountInfoMapper.update(accountInfo);
        if (count > 0){
            log.info("数据变动：account_info表中有"+count+"条数据被修改");
        }
        return count;
    }

    @Override
    public void deleteAccountInfo(int id) {
        accountInfoMapper.delete(id);
    }

    /**
     * 批量标注flag = 1
     * @param ids
     */
    @Override
    public int removeTheMarkerAccountInfo(List<Integer> ids) {
        int count = accountInfoMapper.bulkChangesFlagState(ids);
        log.info("数据变动：account_info中有"+count+"条数据被标注为删除");
        return count;
    }

    @Override
    public PageInfo<AccountInfo> getAccountInByUserIdWithPagination(Integer userId, int pageNum, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<AccountInfo> accountInfos = accountInfoMapper.selectByUserId(userId);
        // 返回分页结果
        return new PageInfo<>(accountInfos);
    }
}