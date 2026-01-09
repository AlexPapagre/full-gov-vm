#!/bin/bash

docker stop minio 2>/dev/null

docker rm minio 2>/dev/null

echo "MinIO stopped and container removed!"
