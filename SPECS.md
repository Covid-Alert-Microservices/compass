# Sequence diagrams

### Handling geolocation requests

![Seq1](/assets/Seq1.png)

<!-- 
@startuml

autonumber
skinparam responseMessageBelowArrow true
actor Agent
participant Controller
database PostgresDB
queue Kafka

activate Kafka
Agent -> Controller: POST /geo
activate Controller
Controller -> PostgresDB: insert location
activate PostgresDB
PostgresDB -> Controller: location
deactivate PostgresDB
Controller -[#0000FF]-> Kafka : location_created {location}
Controller -> Agent: location
deactivate Controller

@enduml
 -->

### Using Neo4j for optimization 


![Seq2](/assets/Seq2.png)

<!-- 
@startuml

autonumber
skinparam responseMessageBelowArrow true
queue Kafka
participant Listener
database PostgresDB
database Neo4j


activate Kafka
Kafka -[#0000FF]-> Listener: location_created {location}
activate Listener

Listener -> PostgresDB: get potential infected locations
note over PostgresDB
  **Optimization request**
  timestamp +- 5min
  lat +- 0.1deg
  lon +- 0.1deg
end note
activate PostgresDB
PostgresDB -> Listener: locations
deactivate PostgresDB
note over Listener
  only close locations
  should be considered
end note
Listener -> Listener: compute location distances
note over Listener
  drop too far locations
end note
Listener -> Neo4j: create link between users
note over Neo4j
  merge users nodes
  upsert relationships
end note
activate Neo4j
Neo4j -> Listener: transaction status
deactivate Neo4j
deactivate Listener

@enduml
 -->

### Positive case

![Seq3](/assets/Seq3.png)

<!-- 
@startuml

autonumber
skinparam responseMessageBelowArrow true
queue Kafka
participant Listener
database Neo4j


activate Kafka
Kafka -[#0000FF]-> Listener: user_positive {user, timestamp}
activate Listener


Listener -> Neo4j: get contact users
note over Neo4j
  1st degree contact
                OR
  subgraph contact
end note
activate Neo4j
Neo4j -> Listener: contact users
deactivate Neo4j
loop foreach contact user
Listener -[#0000FF]-> Kafka : send_alert {user}
end loop
deactivate Listener

@enduml
 -->



