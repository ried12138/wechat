//package xin.pwdkeeper.wechat.config;
//
//import io.swagger.annotations.ApiOperation;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * swagger 配置
// * @Author weiranliu
// * @Email liuweiran12138@outlook.com
// * @Date 2022/2/11   15:19
// * @Version 1.0
// */
//@Configuration    //表明当前类是配置类
//@EnableOpenApi    //表示开启生成接口文档功能（只有开启了OpenApi,才可以实现生成接口文档的功能）
//public class SwaggerConfig {
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .select()
//                .apis( RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("领券分享折扣网接口文档")//标题
//                .description("更多请咨询服务开发者reid")//描述
//                //附加信息
//                .contact(new Contact("reid", "http://www.taobaok.xyz", "liuweiran12138@outlook.com"))
//                .version("1.0")//版本
//                .build();
//    }
//}
