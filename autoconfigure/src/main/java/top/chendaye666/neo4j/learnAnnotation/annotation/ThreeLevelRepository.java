package top.chendaye666.neo4j.learnAnnotation.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SecondLevelRepository
public @interface ThreeLevelRepository {
    String value() default "";
}
