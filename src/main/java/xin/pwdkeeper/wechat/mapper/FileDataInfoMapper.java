package xin.pwdkeeper.wechat.mapper;

import xin.pwdkeeper.wechat.bean.FileDataInfo; // 更新导入路径
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileDataInfoMapper {

    FileDataInfo selectById(Integer id);

    List<FileDataInfo> selectAll();

    int insert(FileDataInfo fileDataInfo);

    void update(FileDataInfo fileDataInfo);
    void deleteById(Integer id);

    FileDataInfo selectByOpenId(Integer accId);

    int updateFlagStatus(FileDataInfo fileDataInfo);
}