# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Package as JAR
mvn clean package

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=TliasWebManagerApplicationTests
```

## Project Overview

Enterprise department & employee management system with RBAC. Stack: Spring Boot 4.0.6 + MyBatis 4.0.1 + MySQL + Vue 3 (CDN, no build step).

## Architecture

**Backend** — standard layered architecture:
- `controller/` — REST endpoints (LoginController, DeptController, EmployeeController, NotificationController, DashboardController)
- `service/` — interfaces + impls in `service/` directory
- `mapper/` — MyBatis interfaces in `mapper/`, XML mappings in `src/main/resources/com/wb/mapper/`
- `entity/` — POJOs (Dept, Employee, Notification, SysUser, PageResult) + unified generic response wrapper `Result<T>`
- `config/` — `LoginInterceptor` (session auth + role check) + `WebConfig` (interceptor registration) + `GlobalExceptionHandler` (catches `DataIntegrityViolationException` + validation errors)
- `util/` — `IpUtils` (client IP extraction), `PasswordUtils` (BCrypt hashing)

**Frontend** — Vue 3 SPA pages served as static HTML under `src/main/resources/static/`:
- `login.html` / `register.html` — auth pages
- `home.html` — dashboard with stat cards and notification timeline
- `index.html` — department management (CRUD table)
- `employee.html` — employee management + attendance punch clock
- `settings.html` — profile editing

**Auth model:** Session-based. `LoginInterceptor` intercepts all `/depts/**`, `/employees/**`, `/notifications/**`, `/dashboard/**`. Two roles: `ADMIN` (full CRUD) and `EMPLOYEE` (read-only + self-attendance + notification read).

## Key Configurations

- **Application:** `src/main/resources/application.properties` — MySQL on `localhost:3306/tables`, Druid datasource, MyBatis camelCase mapping
- **MyBatis mapper XMLs:** `classpath:com/wb/mapper/*.xml`
- **Logging:** `logback.xml` — separate dev/prod appenders with rolling file policies. SQL logging controlled by logback (not `StdOutImpl`); dev profile enables `com.wb.mapper` at DEBUG level for SQL visibility
- **Actuator:** only `/health` endpoint exposed
- **Pagination:** PageHelper with MySQL dialect, reasonable defaults

## Database Constraints

- **Foreign key:** `Employee.dept_id` references `dept.id` via constraint `fk_emp_dept` (`ON DELETE RESTRICT`, `ON UPDATE CASCADE`)
- **Global exception handler:** catches `DataIntegrityViolationException` and returns `Result.error("该部门下存在员工，无法删除")`
- **Validation:** `@Valid` on entity fields, `MethodArgumentNotValidException` handled by `GlobalExceptionHandler`

## Security

- **Passwords:** BCrypt hashed via `PasswordUtils` (spring-security-crypto)
- **API responses:** `@JsonIgnore` on password fields in `SysUser` and `Employee`
- **Logs:** passwords are never logged
- **Actuator:** restricted to `/health` only

## Code Patterns

- Constructor injection (no `@Autowired` field injection)
- `Result<T>` generic response wrapper
- `PageResult<T>` for paginated list responses
- `IpUtils.getClientIp()` for IP extraction (used by all controllers)
- Input validation via Jakarta Validation annotations (`@NotBlank`, `@Size`)
