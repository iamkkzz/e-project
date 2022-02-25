package com.kkzz.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1.整合mybatis-plus
 *  1) 导入依赖
 *  2) 配置
 *      1.配置数据源
 *          导入驱动
 *          配置数据源相关信息
 *      2.配置mybatis-plus
 *          使用mapperScan
 *          配置sql映射文件位置
 *
 *
 *
 * 怎么使用逻辑删除
 *   1.(可省略)现在mybatis-plus的配置文件中配置logic-delete-field和logic-not-delete-value: 0
 *   2.在实体类上的字段上添加@TableLogic注解
 */

/**
 * 使用对象存储:
 * 1.引入依赖
 * 2.在配置文件中配置key等信息
 * 3.使用相关操作
 */

/**
 * 3.JSR303
 *  1.给Bean添加校验注解,参考javax.validation.constraints包下的注解,并定义自己的message提示
 *  2.开启校验功能@Valid效果,校验错误以后会有默认的响应
 *  3.给校验的bean后紧跟一个bindingResult,就可以获取到校验的结果
 *  4.分组校验(多场景的复杂校验)
 *      1.@NotBlank(message = "品牌名不能为空",groups = {AddGroup.class,UpdateGroup.class})
 *        给检验注解标注什么情况需要进行校验
 *      2.@Validated({AddGroup.class})
 *      3.如果开启了分组的功能,那么没有指定分组的校验注解将不会生效
 *  5.自定义校验
 *      1.编写一个自定义的校验注解
 *      @Documented
 *      @Constraint(validatedBy = {ListValueConstraintValidator.class})
 *      这里可以指定多个不同的校验器
 *      @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
 *      @Retention(RUNTIME)
 *      2.编写一个自定义校验器
 *      ListValueConstraintValidator
 *      3.关联自定义校验器和自定义的校验注解@Constraint(validatedBy = {ListValueConstraintValidator.class})
 *
 */

/**
 * 4.统一异常处理
 *  1.使用@ControllerAdvice
 *  2.使用@ExceptionHandler标注方法可以处理的异常
 */
@EnableRedisHttpSession
@EnableCaching
@EnableFeignClients(basePackages = "com.kkzz.mall.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }

}
