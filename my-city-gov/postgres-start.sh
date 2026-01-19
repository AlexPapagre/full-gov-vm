#!/bin/bash

CONTAINER_NAME="mycitygov-postgres"

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Container $CONTAINER_NAME already exists. Starting it..."
    docker start $CONTAINER_NAME
else
    echo "Running new Postgres container..."
    docker run -d \
        --name $CONTAINER_NAME \
        -e POSTGRES_DB=mycitygovdb \
        -e POSTGRES_USER=mycitygovuser \
        -e POSTGRES_PASSWORD=mycitygovpassword \
        -p 127.0.0.1:5432:5432 \
        -v mycitygov_postgres_data:/var/lib/postgresql/data \
        postgres:16
fi

echo "Postgres is up!"
