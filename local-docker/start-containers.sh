#!/bin/bash

docker-compose -f "docker-compose.yml" --project-directory "." up -d --build
