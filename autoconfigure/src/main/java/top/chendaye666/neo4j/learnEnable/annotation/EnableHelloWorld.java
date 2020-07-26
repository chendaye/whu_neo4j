package top.chendaye666.neo4j.learnEnable.annotation;

import org.springframework.context.annotation.Import;
import top.chendaye666.neo4j.learnEnable.annotation.HelloWorldImportSelector;

import java.lang.annotation.*;

/**
 *  激活 HelloWorld 模块
 *
 * @author 小马哥
 * @since 2018/5/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
//@Import(HelloWorldConfiguration.class)
@Import(HelloWorldImportSelector.class)
public @interface EnableHelloWorld {
}
