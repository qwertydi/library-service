#!/bin/sh

IMAGE_NAME="library-service-api"

echo "Building the Docker image: $IMAGE_NAME"

docker build -t ${IMAGE_NAME} .

if [ $? -ne 0 ]; then
  echo "Docker image build failed. Exiting."
  exit 1
fi
