#!/bin/sh
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker 
docker build -f src/main/docker/Dockerfile.native-micro -t floodplain/demo-mongodb-review .
