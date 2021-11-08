package com.github.covidalert.microservicetemplate.repositories;

import com.github.covidalert.microservicetemplate.models.UserEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraphRepository extends Neo4jRepository<UserEntity, String> {
    @Query("MERGE (a:Person {id: $user1}) MERGE (b:Person {id: $user2}) MERGE (a)-[rel:SEE]-(b) ON CREATE SET rel.timestamp = $time ON MATCH SET rel.timestamp = $time")
    void insert(@Param("user1") String user1, @Param("user2") String user2, @Param("time") Long timestamp);

    @Query("MATCH (positive:Person {id: $positiveId})-[rel:SEE]-(potential:Person) WHERE rel.timestamp > $time  RETURN potential")
    List<String> findPotentials(@Param("positiveId") String positiveUserId, @Param("time") Long timestamp);

    @Query("MATCH (positive:Person {id: $positiveId})-[rels:SEE*0..]-(potential:Person) WHERE ALL (rel in rels WHERE rel.timestamp > $time) AND contact.id <> $positiveId RETURN DISTINCT potential")
    List<String> findInDepthPotentials(@Param("positiveId") String positiveUserId, @Param("time") Long timestamp);

    @Query("MATCH (a:Person)-[rel:SEE]-(b:Person) WHERE rel.timestamp < $time DELETE rel")
    void cleanRelationshipsFrom(@Param("time") Long timestamp);

    @Query("MATCH (a:Person) WHERE size((a)--()) = 0 DELETE a")
    void cleanAloneNodes();

}
