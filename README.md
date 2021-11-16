# Geolocation Microservice
[![Build](https://github.com/Covid-Alert-Microservices/compass/actions/workflows/build.yaml/badge.svg)](https://github.com/Covid-Alert-Microservices/compass/actions/workflows/build.yaml) [![Release](https://img.shields.io/github/v/release/Covid-Alert-Microservices/compass)](https://github.com/Covid-Alert-Microservices/compass/tags) [![ReleaseDate](https://img.shields.io/github/release-date/Covid-Alert-Microservices/compass)](https://github.com/Covid-Alert-Microservices/compass/tags)

## Description
This repository manages geolocation for Covid-Alert. It provides a 30-days backup of anonymous geolocation resources and a custom proximity graph to easily find potential infected members. 

## Environment variables

The following environment variables can be configured:
- `KEYCLOAK_URL`: the URL to the Keycloak instance (default: `http://localhost:5000`)
- `KAFKA_URL`: the URL to the Kafka node (default: `http://localhost:29092`)
- `POSTGRES_HOST`: the host for the PostgreSQL database (default: `localhost:5432/postgres`)
- `POSTGRES_USER`: the user for the PostgreSQL database (default: `postgres`)
- `POSTGRES_PASSWORD`: the password for the PostgreSQL database (default: `postgres`)
- `NEO4J_DATABASE_NAME`: the name for the Neo4j database (default: `neo4j`)
- `NEO4J_USER`: the user for the Neo4j database (default: `neo4j`)
- `NEO4J_PASSWORD`: the password for the Neo4j database (default: `root`)
- `NEO4J_URI`: the uri for the Neo4j database (default: `bolt://localhost:7687`)

## Dependencies
In order to use this microservice, make sure you cloned [Kafka](https://github.com/Covid-Alert-Microservices/kafka) and [Keycloak](https://github.com/Covid-Alert-Microservices/keycloak) repositories.