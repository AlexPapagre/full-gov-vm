#!/bin/bash

CONTAINER_NAME="nocgov-postgres"

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Container $CONTAINER_NAME already exists. Starting it..."
    docker start $CONTAINER_NAME
else
    echo "Running new Postgres container..."
    docker run -d \
        --name $CONTAINER_NAME \
        -e POSTGRES_DB=nocgovdb \
        -e POSTGRES_USER=nocgovuser \
        -e POSTGRES_PASSWORD=nocgovpassword \
        -p 127.0.0.1:5433:5432 \
        -v nocgov_postgres_data:/var/lib/postgresql/data \
        postgres:16
fi

echo "Postgres is up!"
