# reactive-rest-api

### Building the project
* Include spotless check, spotbugs check, jacoco check

```shell script
./gradlew build
```

### Clean the project

```shell script
./gradlew clean
```

### Apply code style for project

```shell script
./gradlew spotlessApply
```

### Check project for bugs

```shell script
./gradlew spotbugsTest
```

### Command combination for, the building of the project

```shell script
./gradlew clean spotlessApply build
```

### Bootable project jar file
* Create in project root directory folder: app-build, with reactive-rest-api.jar files and include liquibase db changes files.

```shell script
./gradlew bootJar
```

### Local environment setup

To manage JDK version (21 java version) on local machine install [jenv](https://www.jenv.be/).

To manage docker containers on local machine install [docker](https://www.docker.com/get-started/).

Start Local Docker Compose with Dependencies

```shell script
cd ./local-docker
./start-containers.sh
```

Check the containers are up and working

```shell script
docker container ls -a
```
Result:
```
CONTAINER ID   IMAGE             COMMAND                  CREATED              STATUS                        PORTS                                           NAMES
43353w342434   postgres:latest   "docker-entrypoint.s…"   About a minute ago   Up About a minute (healthy)   0.0.0.0:5442->5442/tcp, :::5442->5442/tcp       rest_api_postgresql
```

Deploy local postgresql changelogs into database with liquibase task

```shell script
./gradlew liquibaseSqlLocal update
```

Result:
```
Liquibase command 'update' was executed successfully.
```

Deploy into dev (or other environment) postgresql changelogs into database with liquibase task

```shell script
./gradlew liquibaseSqlDev -Purl=jdbc:postgresql://dev_server:5442/rest_api_db -Pusername=postgres -Ppassword=postgres update
```

Result:
```
Liquibase command 'update' was executed successfully.
```
