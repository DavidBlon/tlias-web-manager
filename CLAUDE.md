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
- `entity/` — POJOs (Dept, Employee, Notification, SysUser) + unified response wrapper `Result`
- `config/` — `LoginInterceptor` (session auth + role check) + `WebConfig` (interceptor registration) + `GlobalExceptionHandler` (catches `DataIntegrityViolationException` for FK violations)

**Frontend** — Vue 3 SPA pages served as static HTML under `src/main/resources/static/`:
- `login.html` / `register.html` — auth pages
- `home.html` — dashboard with stat cards and notification timeline
- `index.html` — department management (CRUD table)
- `employee.html` — employee management + attendance punch clock
- `settings.html` — profile editing

**Auth model:** Session-based. `LoginInterceptor` intercepts all `/depts/**`, `/employees/**`, `/notifications/**`, `/dashboard/**`. Two roles: `ADMIN` (full CRUD) and `EMPLOYEE` (read-only + self-attendance + notification read).

## Key Configurations

- **Application:** `src/main/resources/application.properties` — MySQL connection on `localhost:3306/tables`, Druid datasource, MyBatis camelCase mapping enabled
- **MyBatis mapper XMLs:** located at `classpath:com/wb/mapper/*.xml` (note: `mybatis.mapper-locations` property is set to `classpath:mapper/*.xml` which is incorrect — the XMLs live under `com/wb/mapper/`)
- **Logging:** `logback.xml` — separate dev/prod appenders with rolling file policies
- **Git proxy:** configured via `git config http.proxy http://127.0.0.1:7890`

## Database Constraints

- **Foreign key:** `Employee.dept_id` references `dept.id` via constraint `fk_emp_dept` (`ON DELETE RESTRICT`, `ON UPDATE CASCADE`)
- **SQL migration:** `src/main/resources/sql/add-foreign-key.sql` — run once against MySQL to add the FK
- **Global exception handler:** `GlobalExceptionHandler` catches `DataIntegrityViolationException` and returns `Result.error("该部门下存在员工，无法删除")` — no application-level check needed before dept deletion
- **Prerequisite:** Before adding the FK, ensure no orphan `dept_id` values exist in `Employee` table

## Important Notes

- Passwords are stored and transmitted in plaintext (no hashing)
- All frontend dependencies are loaded via CDN (no npm/node build step)
- The `@ComponentScan` in the main application class includes `"com.wq"` package which is empty/unused
- Database name in `application.properties` is `tables` (not `tlias`)
