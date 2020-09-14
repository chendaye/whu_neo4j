# 文档

[Neo4j 官方文档](https://neo4j.com/docs/)


[developer doc](https://neo4j.com/developer/get-started/)

[spring-data-neo4j 参考文档](https://docs.spring.io/spring-data/neo4j/docs/current/reference/html/#)

[Spring Data Neo4j⚡️RX doc](https://neo4j.github.io/sdn-rx/current/#introduction)






# 建模

## 建模节点实体

> 一个节点实体就是一个 Java 类； 代表一个特定的域实体； 对应图中的一个节点。比如：Move People

## 建模关系实体

> 关系实体是指节点和实体之间的关系

## 建模节点实体之间的关系


# Cypher

**Cypher 就是 图的遍历**

## start

> 查找图形中的起始节点

- 从单个节点开始查询 
```cyper
start user=node(1) # 从节点 id=1 开始查询
    return user;
```

- 从多个节点开始查询
```cypher
start user=node(1,3)
match user-[:HAS_SEEN]->movie
return distinct movie;  # 返回电影（不重复）
```

- 使用全文索引 Lucene 匹配起始节点
- 全文索引 Lucene 单独学
```cypher
start john=node:users(name = "John")
return join;

# 使用本地 Lucene 查询索引查找节点
# 这种方式可以使用 强大的 Lucene 查询结构，比如：通配符、多属性匹配
start john=node:users(name:"John")
return join;
```

- 基于模式的索引查找节点
- 指定使用模式索引的标签被定义为节点标识符的一个部分，用冒号分隔
- 必须在圆括号内翻译官与标识符相关的标签
- 查询索引被指定为 where 语句的一部分
- 基于标签的索引只能用于完全属性查找
- 对于全文索引必须使用手动方法创建索引
```cypher
match (john:USER)
where john.name='John'
return john;
```

- 多个起始节点
```cypher
# 返回 john Jack 都看过的电影
# 两个模式中都由 movie 节点，有相同的名字，因此代表同一个节点
start john=node:users(name:"John"),
      jack=node:users(name:"jack)
match john-[:HAS_SEEN]->movie,jack-[:HAS_SEEN]->movie
return movie;
```


## match

> 匹配图形模式

- ：后面指定关系类型
- -[:HAS_SEEN]-> HAS_SEEN 从左导到右方向
- user 看过 movie
```cypher
start user=node(1)
match (user)-[:HAS_SEEN]->(movie)
return movie;
```

- 如果不清楚关系的方向 可以 不要方向箭头
```cypher
start user=node(1)
match (user)-[:IS_FRIEND_OF]-(user)
return movie;
```

- 节点和关系可以命名，可以在之后引用
```cypher
start user=node(1)
match (user)-[r:IS_FRIEND_OF]-(user)
return movie;
```

- 节点也可以匿名
- 从 user 节点返回所有 HAS_SEEN 关系 而不关心 movie 节点
- 节点可以不要圆括号； 匿名节点必须要
```cypher
start user=node(1)
match (user)-[:HAS_SEEN]->()
return movie;
```

- 复杂匹配
```cypher
# john 的朋友看过的电影
start john=node(1)
match john-[:IS_FRIEND_OF]->()-[:HAS_SEEN]->(movie)
return movie;

# john看过 & john 的朋友看过的电影
# 用逗号分隔 就是 并(and)
start john=node(1)
match 
john-[:IS_FRIEND_OF]->()-[:HAS_SEEN]->(movie),
john-[:HAS_SEEN]->(movie)
return movie;

# john没看过 & john 的朋友看过的电影
# 第二个模式 是where的一部分； 过滤第一个模式返回的结果
start john=node(1)
match john-[:IS_FRIEND_OF]->()-[:HAS_SEEN]->(movie)
where NOT john-[:HAS_SEEN]->(movie)
return movie;
```

## where

> 过滤数据

- john 1992年之后出生的朋友
```cypher
start john=node:users("name:John")
match john-[:IS_FRIEND_OF]->(friend)
where friend.yearofBirth > 1992
return friend;
```

- 正则表达式 =~
- 正则表达式放在 /.../ 之间；遵循java 正则表达式语法
```cypher
start john=node:users("name:John")
match john-[:IS_FRIEND_OF]->(friend)
where friend.email =~ /*.gmail.com/
return friend;
```

- 因为存储的是半结构化的数据。不是每个节点一定有某个属性
```cypher
start john=node:users("name:John")
match john-[:IS_FRIEND_OF]->(friend)
where has(friend.email)  # 有邮箱的朋友
return friend;
```

## return

> 返回感兴趣的结果， 包括：节点、关系、节点(关系)属性

- 只返属性
```cypher
start john=node:users("name:John")
match john-[:IS_FRIEND_OF]->(friend)
where has(friend.email)  # 有邮箱的朋友
return friend.name;
```

- 返回关系
```cypher
start john=node:users("name:John")
match john-[r:HAS_SEEN]->(movie)
return r;

# 可以只返回关系的属性
start john=node:users("name:John")
match john-[r:HAS_SEEN]->(movie)
return r.start; 
```

- 返回路径 path
- 返回引用的路径，和相关电影名字
```cypher
start john=node(1)
match 
moviePath = john-[:IS_FRIEND_OF]->()-[:HAS_SEEN]->(movie)
where NOT john-[:HAS_SEEN]->(movie)
return movie.name,moviePath;
```


- 分页
- order：分页之前对结果排序
- skip: 划分结果集，跳转指定页
- linit: 限制返回数量
```cypher
start john=node:users("name:John")
match john-[r:HAS_SEEN]->(movie)
return movie
order by movie.name # 结果排序
skip 20 # 跳过前面2页； 10*2=20
limit 10 # 每页返回 10 个
```
