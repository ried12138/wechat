package xin.pwdkeeper.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = "xin.pwdkeeper.wechat.mapper")
@EnableScheduling
@SpringBootApplication
public class WechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }


}
