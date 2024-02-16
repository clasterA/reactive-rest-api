# reactive-rest-api, database schema deployment.

To manage liquibase on local machine install [liquibase](https://www.liquibase.org/).

* Install Liquibase on Windows [liquibase_windows](https://docs.liquibase.com/start/install/liquibase-windows.html)
* Install Liquibase on macOS [liquibase_linux](https://docs.liquibase.com/start/install/liquibase-macos.html)
* Install Liquibase on Linux/Unix [liquibase_linux](https://docs.liquibase.com/start/install/liquibase-linux.html)

### Build project with gradle task command: bootJar
* This operation create in **project root directory, app-build** folder, with reactive-rest-api.jar files and include liquibase db changes files.

```shell script
export DB_JDBC_URL=jdbc:postgresql://localhost:5442/rest_api_db
export DB_LIQUIBASE_USERNAME=postgres
export DB_LIQUIBASE_PASSWORD=postgres
export DB_LIQUIBASE_DRIVER=org.postgresql.Driver
export DB_LIQUIBASE_CHANGELOG=db/changelog/db.changelog-master.xml
```

### Apply liquibase db update
* This operation must be performed from the **project root directory, the app-build folder** (reactive-rest-api/app-build)

```shell script
liquibase update --url=$DB_JDBC_URL --driver=$DB_LIQUIBASE_DRIVER --username=$DB_LIQUIBASE_USERNAME --password=$DB_LIQUIBASE_PASSWORD --changelog-file=$DB_LIQUIBASE_CHANGELOG
```

### Result
```
UPDATE SUMMARY
Run:                          5
Previously run:               0
Filtered out:                 0
-------------------------------
Total change sets:            5

Liquibase command 'update' was executed successfully.
```
