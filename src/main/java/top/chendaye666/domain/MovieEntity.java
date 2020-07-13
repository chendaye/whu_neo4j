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
package top.chendaye666.domain;

// tag::mapping.annotations[]

import static org.neo4j.springframework.data.core.schema.Relationship.Direction.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.springframework.data.core.schema.Id;
import org.neo4j.springframework.data.core.schema.Node;
import org.neo4j.springframework.data.core.schema.Property;
import org.neo4j.springframework.data.core.schema.Relationship;

// end::mapping.annotations[]

/**
 * @author Michael J. Simons
 */
// tag::mapping.annotations[]
@Node("Movie") // <.>
public class MovieEntity {

	@Id  // <.>
	private final String title;

	@Property("tagline")  // <.>
	private final String description;

	@Relationship(type = "ACTED_IN", direction = INCOMING) // <.>
	// tag::mapping.relationship.properties[]
	private Map<PersonEntity, Roles> actorsAndRoles = new HashMap<>();
	// end::mapping.relationship.properties[]

	@Relationship(type = "DIRECTED", direction = INCOMING)
	private List<PersonEntity> directors = new ArrayList<>();

	public MovieEntity(String title, String description) { // <.>
		this.title = title;
		this.description = description;
	}

	// Getters omitted for brevity
	// end::mapping.annotations[]

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Map<PersonEntity, Roles> getActorsAndRoles() {
		return actorsAndRoles;
	}

	public List<PersonEntity> getDirectors() {
		return directors;
	}
	// tag::mapping.annotations[]
}
// end::mapping.annotations[]
