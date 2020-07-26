package top.chendaye666.neo4j.learnAnnotation.bootstrap;

import top.chendaye666.neo4j.learnAnnotation.repository.MyFirstLevelRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 仓储的引导类
 *
 * @author 小马哥
 * @since 2018/5/14
 */
@ComponentScan(basePackages = "top.chendaye666.neo4j.learnAnnotation.repository")
public class RepositoryBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(RepositoryBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        // myFirstLevelRepository Bean 是否存在;
        // MyFirstLevelRepository 作为一个 Bean 可以获取
        MyFirstLevelRepository myFirstLevelRepository =
                context.getBean("myFirstLevelRepository",MyFirstLevelRepository.class);

        System.out.println("myFirstLevelRepository Bean : "+myFirstLevelRepository);

        // 关闭上下文
        context.close();
    }
}
