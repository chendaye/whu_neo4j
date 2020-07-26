# 自动装配

## Spring 模式注解装配

[模式注解](https://github.com/spring-projects/spring-framework/wiki)

```
模式注解是一种用于声明在应用中扮演“组件”角色的注解。如 Spring Framework 中的 @Repository 标注在任何类上 ，用
于扮演仓储角色的模式注解。
@Component 作为一种由 Spring 容器托管的通用模式组件，任何被 @Component 标准的组件均为组件扫描的候选对象。
类 似地，凡是被 @Component 元标注(meta-annotated)的注解，如 @Service ，当任何组件标注它时，也被视作组件扫 描的候选对象
```

> 模式注解举例

|  Spring Framework 注解   | 场景说明  | 起始版本  |
|  ----  | ----  |----  |
| `@Repository`  |数据仓储模式注解 |2.0 |
| `@Component`  | 通用组件模式注解 |2.5 |
| `@Controller`  | Web 控制器模式注解 |2.5 |
| `@Configuration`  | 配置类模式注解 |3.0 |

## 装配方式

### `<context:component-scan>` 方式

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring- context.xsd">
  <!-- 激活注解驱动特性 --> <context:annotation-config />
  <!-- 找寻被 @Component 或者其派生 Annotation 标记的类(Class)，将它们注册为 Spring Bean --> <context:component-scan base-package="com.imooc.dive.in.spring.boot" />
  </beans>
   
```

###  `@ComponentScan` 方式

```java
 
@ComponentScan(basePackages = "com.imooc.dive.in.spring.boot") public class SpringConfiguration {
...
}
```

## 自定义模式注解

> `@Component` “派生性”

-  @Component
      - @Repository
          - FirstLevelRepository
             - SecondLevelRepository
               - ThreeLevelRepository

```java
package top.chendaye666.neo4j.annotation;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * 一级 {@link Repository @Repository}
 *
 * @author 小马哥
 * @since 2018/5/14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface FirstLevelRepository {

    String value() default "";

}

```

```java
package top.chendaye666.neo4j.annotation;

import org.springframework.stereotype.Repository;import top.chendaye666.neo4j.learnAnnotation.annotation.FirstLevelRepository;

import java.lang.annotation.*;

/**
 * 二级 {@link Repository}
 *
 * @author 小马哥
 * @since 2018/5/14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FirstLevelRepository
public @interface SecondLevelRepository {

    String value() default "";

}

```

```java
package top.chendaye666.neo4j.annotation;

import top.chendaye666.neo4j.learnAnnotation.annotation.SecondLevelRepository;import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SecondLevelRepository
public @interface ThreeLevelRepository {
    String value() default "";
}

```

```java
package top.chendaye666.neo4j.repository;

import top.chendaye666.neo4j.learnAnnotation.annotation.ThreeLevelRepository;

@ThreeLevelRepository(value = "testRepository") // Bean 名称
public class TestRepository {
}

```

## Spring @Enable 模块装配

> Spring Framework 3.1 开始支持”@Enable 模块驱动“。所谓“模块”是指具备相同领域的功能组件集合， 组合所形成一个独立 的单元。
> 比如 Web MVC 模块、AspectJ代理模块、Caching(缓存)模块、JMX(Java 管 理扩展)模块、Async(异步处 理)模块等。

### @Enable 注解模块举例


|  框架实现   | @Enable 注解模块  | 激活模块  |
|  ----  | ----  |----  |
| `Spring Framework`  |@EnableWebMvc |Web MVC 模块 |
|   |@EnableTransactionManagement|事务管理模块 |
|   |@EnableCaching|Caching 模块 |
|   | @EnableMBeanExport|JMX 模块 |
|   | @EnableAsync|异步处理模块 |
|   | @EnableWebFlux|Web Flux 模块 |
|   | @EnableAspectJAutoProxy|AspectJ 代理模块 |
|   | | |
|  Spring Boot | @EnableAutoConfiguration| 自动装配模块|
|  | @EnableManagementContext| Actuator 管理模块|
|  | @EnableConfigurationProperties| 配置属性绑定模块|
|  | @EnableOAuth2Sso| OAuth2 单点登录模块|
|   | | |
|Spring Cloud| @EnableEurekaServer| Eureka服务器模块|
|   | @EnableConfigServer| 配置服务器模块|
|   | @EnableFeignClients| Feign客户端模块|
|   |  @EnableZuulProxy| 服务网关 Zuul 模块|
|   |  @EnableCircuitBreaker| 服务熔断模块|

### 实现方式

#### 注解驱动方式

```java
 
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
@Documented @Import(DelegatingWebMvcConfiguration.class) public @interface EnableWebMvc {
}

 @Configuration
public class DelegatingWebMvcConfiguration extends
WebMvcConfigurationSupport {

```

#### 接口编程方式

```java
 @Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME) @Documented @Import(CachingConfigurationSelector.class) public @interface EnableCaching {
...
}
```

```java
 public class CachingConfigurationSelector extends AdviceModeImportSelector<EnableCaching> {
    /**
    * {@inheritDoc}
    * @return {@link ProxyCachingConfiguration} or {@code
    AspectJCacheConfiguration} for
    * {@code PROXY} and {@code ASPECTJ} values of {@link
    EnableCaching#mode()}, respectively

```

### 自定义 @Enable 模块

#### 基于注解驱动实现 - @EnableHelloWorld

#### 基于接口驱动实现 - @EnableServer

> `HelloWorldImportSelector` -> `HelloWorldConfiguration` -> HelloWorld

```java
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

```
```java
package top.chendaye666.neo4j.learnEnable.annotation;

import top.chendaye666.neo4j.learnEnable.configuration.HelloWorldConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * HelloWorld {@link ImportSelector} 实现
 *
 * @author 小马哥
 * @since 2018/5/14
 */
public class HelloWorldImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{HelloWorldConfiguration.class.getName()};
    }
}

```


```java
package top.chendaye666.neo4j.learnEnable.configuration;

import org.springframework.context.annotation.Bean;

/**
 * HelloWorld 配置
 *
 * @author 小马哥
 * @since 2018/5/14
 */
public class HelloWorldConfiguration {

    @Bean
    public String helloWorld() { // 方法名即 Bean 名称
        return "Hello,World 2018";
    }

}

```

```java
package top.chendaye666.neo4j.learnEnable.bootstrap;

import top.chendaye666.neo4j.learnEnable.annotation.EnableHelloWorld;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * {@link EnableHelloWorld} 引导类
 *
 * @author 小马哥
 * @since 2018/5/14
 */
@EnableHelloWorld
public class EnableHelloWorldBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EnableHelloWorldBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        // helloWorld Bean 是否存在
        String helloWorld =
                context.getBean("helloWorld", String.class);

        System.out.println("helloWorld Bean : " + helloWorld);

        // 关闭上下文
        context.close();
    }
}

```

## Spring 条件装配


|  Spring 注解   | 场景说明  | 起始版本|
|  ----  | ----  |----  |
| `@Profile`  |配置化条件装配|3.1 |
| `@Condition`  |编程条件装配|4.0 |

### 实现方式

#### 配置方式 - @Profile

```java
package top.chendaye666.neo4j.learnConditionConfiguration.Profile.service;

/**
 * 计算服务
 *
 * @author 小马哥
 * @since 2018/5/15
 */
public interface CalculateService {

    /**
     * 从多个整数 sum 求和
     * @param values 多个整数
     * @return sum 累加值
     */
    Integer sum(Integer... values);
}

```

```java
package top.chendaye666.neo4j.learnConditionConfiguration.Profile.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Java 7 for 循环实现 {@link CalculateService}
 *
 * @author 小马哥
 * @since 2018/5/15
 */
@Profile("Java7")
@Service
public class Java7CalculateService implements CalculateService {

    @Override
    public Integer sum(Integer... values) {
        System.out.println("Java 7 for 循环实现 ");
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        CalculateService calculateService = new Java7CalculateService();
        System.out.println(calculateService.sum(1,2,3,4,5,6,7,8,9,10));
    }

}

```

```java
package top.chendaye666.neo4j.learnConditionConfiguration.Profile.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * Java 8 Lambda 实现 {@link CalculateService}
 *
 * @author 小马哥
 * @since 2018/5/15
 */
@Profile("Java8")
@Service
public class Java8CalculateService implements CalculateService {

    @Override
    public Integer sum(Integer... values) {
        System.out.println("Java 8 Lambda 实现");
        int sum = Stream.of(values).reduce(0, Integer::sum);
        return sum;
    }

    public static void main(String[] args) {
        CalculateService calculateService = new Java8CalculateService();
        System.out.println(calculateService.sum(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

}

```

```java
package top.chendaye666.neo4j.learnConditionConfiguration.Profile.bootstrap;

import top.chendaye666.neo4j.learnConditionConfiguration.Profile.service.CalculateService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * {@link CalculateService} 引导类
 *
 * @author 小马哥
 * @since 2018/5/15
 */
@SpringBootApplication(scanBasePackages = "top.chendaye666.neo4j.learnConditionConfiguration.Profile.service")
public class CalculateServiceBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(CalculateServiceBootstrap.class)
                .web(WebApplicationType.NONE)
                .profiles("Java8")
                .run(args);

        // CalculateService Bean 是否存在
        CalculateService calculateService = context.getBean(CalculateService.class);

        System.out.println("calculateService.sum(1...10) : " +
                calculateService.sum(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // 关闭上下文
        context.close();
    }
}

```

#### 编程方式 - @Conditional

```java
package top.chendaye666.neo4j.learnConditionConfiguration.Condition.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 系统属性条件判断
 *
 * @author 小马哥
 * @since 2018/5/15
 */
public class OnSystemPropertyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnSystemProperty.class.getName());

        String propertyName = String.valueOf(attributes.get("name"));

        String propertyValue = String.valueOf(attributes.get("value"));

//        String javaPropertyValue = System.getProperty(propertyName);

        return propertyValue.equals("Mercy");
    }
}

```

```java
package top.chendaye666.neo4j.learnConditionConfiguration.Condition.condition;


import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Java 系统属性 条件判断
 *
 * @author 小马哥
 * @since 2018/5/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Conditional(OnSystemPropertyCondition.class)
public @interface ConditionalOnSystemProperty {

    /**
     * Java 系统属性名称
     * @return
     */
    String name();

    /**
     * Java 系统属性值
     * @return
     */
    String value();
}

```

```java
package top.chendaye666.neo4j.learnConditionConfiguration.Condition.bootstrap;

import top.chendaye666.neo4j.learnConditionConfiguration.Condition.condition.ConditionalOnSystemProperty;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 系统属性条件引导类
 *
 * @author 小马哥
 * @since 2018/5/15
 */
public class ConditionalOnSystemPropertyBootstrap {

    @Bean
    @ConditionalOnSystemProperty(name = "user.name", value = "Mercy")
    public String helloWorld() {
        return "Hello,World 小马哥";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ConditionalOnSystemPropertyBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);
        // 通过名称和类型获取 helloWorld Bean
        String helloWorld = context.getBean("helloWorld", String.class);

        System.out.println("helloWorld Bean : " + helloWorld);

        // 关闭上下文
        context.close();
    }
}

```

## Spring Boot 自动装配

### 底层装配技术

- Spring 模式注解装配 
- Spring @Enable 模块装配 
- Spring 条件装配装配 
- Spring 工厂加载机制
    - 实现类: SpringFactoriesLoader 
    - 配置资源: META-INF/spring.factories
    
### 实现方法

- 激活自动装配 - @EnableAutoConfiguration
- 实现自动装配 - XXXAutoConfiguration
- 配置自动装配实现 - META-INF/spring.factories

### 自定义自动装配

> `HelloWorldAutoConfiguration`

- 条件判断: `user.name == "Mercy"`
- 模式注解: `@Configuration`
- `@Enable` 模块: `@EnableHelloWorld -> HelloWorldImportSelector -> HelloWorldConfiguration - > helloWorld`

```java
package top.chendaye666.neo4j.learnAutoComfigyration.configuration;

import top.chendaye666.neo4j.learnEnable.annotation.EnableHelloWorld;
import top.chendaye666.neo4j.learnConditionConfiguration.Condition.condition.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Configuration;

/**
 * HelloWorld 自动装配
 *
 * @author 小马哥
 * @since 2018/5/15
 */
@Configuration // Spring 模式注解装配
@EnableHelloWorld // Spring @Enable 模块装配
@ConditionalOnSystemProperty(name = "user.name", value = "Mercy") // 条件装配
public class HelloWorldAutoConfiguration {
}

```    

```java
package top.chendaye666.neo4j.learnAutoComfigyration.bootstrap;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * {@link EnableAutoConfiguration} 引导类
 *
 * @author 小马哥
 * @since 2018/5/15
 */
@EnableAutoConfiguration
public class EnableAutoConfigurationBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EnableAutoConfigurationBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        // helloWorld Bean 是否存在
        String helloWorld =
                context.getBean("helloWorld", String.class);

        System.out.println("helloWorld Bean : " + helloWorld);

        // 关闭上下文
        context.close();

    }
}

```