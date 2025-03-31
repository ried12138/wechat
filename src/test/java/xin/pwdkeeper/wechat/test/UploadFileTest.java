package xin.pwdkeeper.wechat.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/31   14:48
 * @Version 1.0
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UploadFileTest {

    @Test
    public void test() {
        try {
            File file = new File("./截屏2025-03-31 14.51.40.png");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String url = Base64.getEncoder().encodeToString(data);
            log.info("url:{}", url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
