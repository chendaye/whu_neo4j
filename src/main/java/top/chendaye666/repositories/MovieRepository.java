/*
 * Copyright (c) 2019-2020 "Neo4j,"
 * Neo4j Sweden AB [https://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.chendaye666.repositories;

// tag::getting.started[]
import org.neo4j.springframework.data.repository.query.Query;
import top.chendaye666.domain.MovieEntity;
import reactor.core.publisher.Mono;

import org.neo4j.springframework.data.repository.ReactiveNeo4jRepository;

import java.util.stream.Stream;

// end::getting.started[]
/**
 * @author chendaye
 * org.springframework.data.repository.Repository
 *
 * org.springframework.data.repository.CrudRepository
 *
 * org.springframework.data.repository.reactive.ReactiveCrudRepository
 *
 * org.springframework.data.repository.reactive.ReactiveSortingRepository
 *
 * A repository fitting to any of the movie entities above
 *
 * 先定义实体 Entity， 再定义 Repository 用来操作 Entity； 最后在 Controller 里面使用 Repository
 */
// tag::getting.started[]
public interface MovieRepository extends ReactiveNeo4jRepository<MovieEntity, String> {

	Mono<MovieEntity> findOneByTitle(String title);

	// 根据 tagline 查找
//	Mono<MovieEntity> findOneByTagline(String tagline);

//	Mono<MovieEntity> findById(Long id);
//
//	@Query(value = "MATCH (movie:Movie) RETURN movie;")
//	Mono<MovieEntity> getAllMovies();
}
// end::getting.started[]
