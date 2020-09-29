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

import org.neo4j.springframework.data.core.schema.*;
import org.neo4j.springframework.data.core.support.UUIDStringGenerator;

// end::mapping.annotations[]

/**
 * @author chendaye666
 */
// tag::mapping.annotations[]
// @Node is used to mark this class as a managed entity. It also is used to configure the Neo4j label.
// The label defaults to the name of the class, if you’re just using plain @Node.
@Node("Movie") // <.>
public class MovieEntity {

	// 使用Neo4j 内部ID
	@Id @GeneratedValue(UUIDStringGenerator.class)
	private String id;

	//	Each entity has to have an id. The movie class shown here uses the attribute title as a unique business key.
	//	If you don’t have such a unique key, you can use the combination of @Id and @GeneratedValue to configure SDN/RX to use Neo4j’s internal id.
	//	We also provide generators for UUIDs
//	@Id  // <.>
	private final String title;

	// This shows @Property as a way to use a different name for the field than for the graph property.
	@Property("tagline")  // <.>
	private final String description;

	// This defines a relationship to a class of type PersonEntity and the relationship type ACTED_IN
	@Relationship(type = "ACTED_IN", direction = INCOMING) // <.>
	// tag::mapping.relationship.properties[]
	private Map<PersonEntity, Roles> actorsAndRoles = new HashMap<>();
	// end::mapping.relationship.properties[]

	@Relationship(type = "DIRECTED", direction = INCOMING)
	private List<PersonEntity> directors = new ArrayList<>();

	//	This is the constructor to be used by your application code
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
