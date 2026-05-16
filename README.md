# Auth Microservice

This microservice is in charge of handling Authentication and managing user
credentials. It issues JSON Web Tokens (JWT) for authenticated users.

## Libraries

The project uses the following stack:

| Runtime dependencies | Development libs     |
| -------------------- | -------------------- |
| Java 25 LTS          | Spring Boot DevTools |
| Maven v3.9.13        | SpringDoc            |
| Spring Boot v4.0.6   | Flyway Migration     |
| Lombok               | Spotless Maven       |
| Validation           | Palantir Java Format |
| Spring Data JPA      |                      |
| Driver Mysql         |                      |

## Development Setup & Execution

To set up and run the microservice in a development environment follow the next
steps:

1. Copy the `.env.example` into `.env`. Adjust if needed.
2. Start the **DB container** through Docker compose:

   ```bash
   docker compose up -d
   ```

   To check if the DB is working go to `http://localhost:8088` (or your `.env`
   setup) and use the provided credentials (`user` & `password` by default).

3. Start Spring Boot through the Maven wrapper:

   ```bash
   ./mvnw spring-boot:run
   ```

Done.

> [!TIP]
>
> You can read the API spec through the provided OpenAPI v3 interface at
> [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html).
>
> Or import the OpenAPI specification into your preferred API client:
> [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)

---

## Inter-Microservice Security Integration

Other microservices in the system that need to validate and extract data from
the JWT Bearer tokens should follow these steps:

1. **Include Security Classes:** Copy the reference security implementation
   files from the springboot-layout in the
   [security dir](https://github.com/polirritmico/springboot-layout/tree/main/security)
   into your microservice's `security` package (e.g.,
   `cl.duoc.<MICROSERVICE-NAME>.security`):
   - `JwtAuthFilter.java`
   - `SecurityConfig.java`

2. **Add JWT Dependency:** Make sure your microservice includes the necessary
   JWT library:

   > ```xml
   > <dependency>
   > 	<groupId>com.auth0</groupId>
   > 	<artifactId>java-jwt</artifactId>
   > 	<version>4.4.0</version>
   > </dependency>
   > ```

3. **Configure Application Properties:** Your microservice must define the
   following properties in its `application.properties` or `.env` to properly
   decode the JWT (already provided in the
   [layout properties](https://github.com/polirritmico/springboot-layout/tree/main/src/main/resources)):

   ```properties
   jwt.secret=your_shared_jwt_secret
   jwt.issuer=your_jwt_issuer
   jwt.public=/api/v1/public-endpoint1,/api/v1/public-endpoint2
   ```

> [!IMPORTANT]
>
> The `jwt.secret` and `jwt.issuer` must match the ones used by this Auth
> Microservice to sign the tokens.

4. **Token Data Availability:** Once a request passes through the
   `JwtAuthFilter`, the token is parsed. The user context will be securely
   stored in Spring's `SecurityContextHolder`.
   - The token's subject is mapped to the `username`.
   - The token's `roles` claim is mapped to Spring Security's `GrantedAuthority`
     list. You can easily retrieve the authenticated user data in your
     controllers using standard Spring Security tools.

---

**_Happy coding!_** ☕
