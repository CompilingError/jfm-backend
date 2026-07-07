# JFM Backend

JFM Backend is the local Spring Boot service used by the JFM desktop application. It provides the HTTP API and SQLite-backed persistence layer for movies, tags, artists, and related desktop workflows.

## Tech stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- SQLite JDBC
- Hibernate SQLite dialect
- Springdoc OpenAPI

## Runtime role

In the v1 desktop release, the Electron frontend starts this backend automatically as a bundled jar. The backend listens locally and is not intended to be exposed publicly.

Default desktop runtime address:

```text
127.0.0.1:8080
```

Default API base URL used by the frontend:

```text
http://localhost:8080
```

## Database configuration

The backend uses SQLite. The datasource can be configured with environment variables.

Default values:

```yaml
spring:
  datasource:
    url: jdbc:sqlite:./data/jfm.db

server:
  address: 127.0.0.1
  port: 8080
```

For the packaged desktop app, Electron passes an explicit database URL so the SQLite file is created under:

```text
C:\ProgramData\JFM\user-config\database\jfm.db
```

Supported environment variables:

```text
JFM_DB_URL
JFM_SERVER_ADDRESS
JFM_SERVER_PORT
```

Example:

```bash
JFM_DB_URL=jdbc:sqlite:./data/jfm.db \
JFM_SERVER_ADDRESS=127.0.0.1 \
JFM_SERVER_PORT=8080 \
java -jar target/jfm-backend-0.0.1-SNAPSHOT.jar
```

On Windows PowerShell:

```powershell
$env:JFM_DB_URL = "jdbc:sqlite:./data/jfm.db"
$env:JFM_SERVER_ADDRESS = "127.0.0.1"
$env:JFM_SERVER_PORT = "8080"
java -jar target/jfm-backend-0.0.1-SNAPSHOT.jar
```

## Build

```bash
mvn -DskipTests package
```

The jar is generated under:

```text
target/
```

For the desktop release, the frontend release script copies the newest backend jar from this folder into:

```text
jfm-frontend/resources/backend/jfm-backend.jar
```

## Development

Run the backend locally:

```bash
mvn spring-boot:run
```

Then run the frontend in development mode from the frontend repository:

```bash
npm run start
```

## Desktop release notes

This backend branch adds release-friendly defaults for the desktop app:

- SQLite datasource configuration.
- Hibernate SQLite dialect.
- `ddl-auto: update` for local schema updates.
- Local-only server binding through `127.0.0.1`.
- Environment-variable overrides for database path, server address, and server port.

## Related repository

Frontend repository:

```text
https://github.com/CompilingError/jfm-frontend
```
