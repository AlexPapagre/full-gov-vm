#!/bin/bash

docker stop minio 2>/dev/null
docker rm minio 2>/dev/null

docker volume create minio_data 2>/dev/null

docker run -d \
    --name minio \
    -p 9000:9000 \
    -p 127.0.0.1:9001:9001 \
    -v minio_data:/data \
    -e MINIO_ROOT_USER=minioadmin \
    -e MINIO_ROOT_PASSWORD=minioadmin \
    minio/minio server /data --console-address ":9001"

echo ""
echo "MinIO started!"
echo ""
echo "API: http://localhost:9000"
echo "Console: http://localhost:9001"
echo ""
echo "Access Key: minioadmin"
echo "Secret Key: minioadmin"
echo ""
echo "Create a bucket called 'request-files'."
