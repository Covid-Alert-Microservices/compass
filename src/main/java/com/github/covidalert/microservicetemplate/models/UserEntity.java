package com.github.covidalert.microservicetemplate.models;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("User")
public class UserEntity {
    @Id
    private final String id;

    @Relationship(type = "SEE", direction = Relationship.Direction.OUTGOING)
    private final Set<TimestampEntity> timestamps = new HashSet();

    public UserEntity(String id) {
        this.id = id;
    }
}
