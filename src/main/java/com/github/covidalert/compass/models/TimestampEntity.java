package com.github.covidalert.compass.models;

import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties()
public class TimestampEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long timestamp;

    @TargetNode
    private UserEntity user;
}
