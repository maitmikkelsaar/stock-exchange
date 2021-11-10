# Stock Exchange App with Angular and Spring Boot
## Build stock-api image from root directory
```sh
cd stock-api
./gradlew :jibDockerBuild
```

## Build stock-back image from root directory
```sh
cd stock-front
docker build -t stock-front .
```

## Run containers from root directory
```sh
docker-compose up -d
```