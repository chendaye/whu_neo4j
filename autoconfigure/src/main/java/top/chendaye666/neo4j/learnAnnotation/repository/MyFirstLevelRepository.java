package top.chendaye666.neo4j.learnAnnotation.repository;

import top.chendaye666.neo4j.learnAnnotation.annotation.FirstLevelRepository;
import top.chendaye666.neo4j.learnAnnotation.annotation.SecondLevelRepository;

/**
 * 我的 {@link FirstLevelRepository}
 *
 * @author 小马哥
 * @since 2018/5/14
 */
@SecondLevelRepository(value = "myFirstLevelRepository") // Bean 名称
public class MyFirstLevelRepository {
}
