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
package top.chendaye666.controller;

// tag::getting.started[]

import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.springframework.data.core.ReactiveNeo4jClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import top.chendaye666.domain.MovieEntity;
import top.chendaye666.repositories.MovieRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import org.neo4j.springframework.data.core.Neo4jClient;
import java.util.List;
import java.util.Map;

// end::getting.started[]
/**
 * @author chendaye666
 */
// tag::getting.started[]
@RestController
@RequestMapping("/movies")
public class MovieController {

	private final MovieRepository movieRepository;
	private final Neo4jClient client;
	private final ReactiveNeo4jClient reactiveClient;



	public MovieController(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;

		// 初始化客户端
		Driver driver = GraphDatabase.driver("bolt://www.lengo.top:7687", AuthTokens.basic("neo4j", "lengo"));
		client = Neo4jClient.create(driver);
		reactiveClient = ReactiveNeo4jClient.create(driver);  // reactive
	}

	@PutMapping
	Mono<MovieEntity> createOrUpdateMovie(@RequestBody MovieEntity newMovie) {
		return movieRepository.save(newMovie);
	}

	@GetMapping(value = { "", "/" }, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<MovieEntity> getMovies() {
		return movieRepository
			.findAll();
	}

	@GetMapping("/by-title")
	Mono<MovieEntity> byTitle(@RequestParam String title) {
		return movieRepository.findOneByTitle(title);
	}

	@DeleteMapping("/{id}")
	Mono<Void> delete(@PathVariable Long id) {
		return movieRepository.deleteById(id);
	}

	@GetMapping("/all")
	Flux<MovieEntity> findAll() {
		return movieRepository.findAll();
	}

	@GetMapping("/{id}")
	Mono<MovieEntity> findById(@PathVariable Long id) {
		return movieRepository.findById(id);
	}


	@GetMapping("/all-by-title")
	Mono<MovieEntity> allByTitle(@RequestParam String title) {
		return movieRepository.findByTitle(title);
	}

	@GetMapping("/client")
	Flux<Map<String, Object>> byClient(@RequestParam String title) {
		Flux<Map<String, Object>> all = reactiveClient
				.query(() -> "MATCH (n:Movie) RETURN n")
				.fetch()
				.all();
		return all;
	}

}
// end::getting.started[]
