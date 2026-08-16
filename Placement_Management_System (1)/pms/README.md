# Placement Management System

A Java backend project built with **Spring Boot, Spring Data JPA (Hibernate), MySQL, and REST APIs**,
based on the project synopsis for a college placement management application.

## Tech Stack
- Core Java 17
- Spring Boot 3.3
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Lombok
- Bean Validation (Jakarta Validation)

## Architecture

```
Client / Frontend → REST Controller → DTO → Service Layer → Repository Layer → Hibernate / JPA → MySQL
```

- **Controller layer** — handles HTTP requests/responses only.
- **Service layer** — contains all business logic and validation rules.
- **Repository layer** — Spring Data JPA interfaces for database access.
- **Entity layer** — JPA-mapped classes representing database tables.
- **DTO layer** — decouples the API contract from the internal entity model.
- **Exception layer** — custom exceptions + a global `@RestControllerAdvice` handler.

## Entities & Relationships

| Entity | Relationship |
|---|---|
| Company `1 --- N` Job | One company posts many jobs |
| Student `1 --- N` Application | One student can submit many applications |
| Job `1 --- N` Application | One job receives many applications |
| Application `1 --- N` Interview | One application can have many interview rounds |
| Student `1 --- 1` Placement | One student has at most one placement record |
| Student `N --- N` Skill | Many-to-many via `student_skills` join table |

## Project Structure

```
src/main/java/com/placement/pms/
 ├── PmsApplication.java
 ├── entity/          # JPA entities + enums (ApplicationStatus, InterviewResult, PlacementStatus)
 ├── repository/       # Spring Data JPA repositories
 ├── service/          # Service interfaces
 ├── service/impl/      # Service implementations (business logic)
 ├── controller/        # REST controllers
 ├── dto/              # Request/response DTOs with validation annotations
 └── exception/         # Custom exceptions + GlobalExceptionHandler
```

## Getting Started

### 1. Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL 8+ running locally

### 2. Configure the database
Edit `src/main/resources/application.properties` if your MySQL credentials differ from the defaults:
```
spring.datasource.username=root
spring.datasource.password=root
```
The database `placement_management_db` is created automatically (`createDatabaseIfNotExist=true`),
and tables are generated/updated automatically via `spring.jpa.hibernate.ddl-auto=update`.

### 3. Build & run
```bash
mvn clean install
mvn spring-boot:run
```
The API starts on `http://localhost:8080`.

## REST API Overview

| Resource | Endpoints |
|---|---|
| Students | `POST /api/students`, `GET /api/students`, `GET /api/students/{id}`, `PUT /api/students/{id}`, `PATCH /api/students/{id}/deactivate`, `DELETE /api/students/{id}` |
| Companies | `POST /api/companies`, `GET /api/companies`, `GET /api/companies/{id}`, `PUT /api/companies/{id}`, `DELETE /api/companies/{id}` |
| Jobs | `POST /api/jobs`, `GET /api/jobs`, `GET /api/jobs/{id}`, `PUT /api/jobs/{id}`, `PATCH /api/jobs/{id}/deactivate`, `DELETE /api/jobs/{id}` |
| Applications | `POST /api/applications`, `GET /api/applications`, `GET /api/applications/{id}`, `PATCH /api/applications/{id}/status?status=SHORTLISTED`, `DELETE /api/applications/{id}` |
| Interviews | `POST /api/interviews`, `GET /api/interviews?applicationId=1`, `GET /api/interviews/{id}`, `PUT /api/interviews/{id}` |
| Placements | `POST /api/placements`, `GET /api/placements`, `GET /api/placements/{id}` |
| Dashboard | `GET /api/dashboard` |

All `GET` list endpoints support `page`, `size`, and `sort` query params (Spring Data pagination), e.g.
`GET /api/students?page=0&size=10&sort=name,asc`.

## Business Rules Implemented
- A student cannot apply twice for the same job (`ApplicationAlreadyExistsException`, HTTP 409).
- A student below the job's eligibility CGPA cannot apply (`StudentNotEligibleException`, HTTP 400).
- Applying to an inactive/closed job is rejected (`InvalidRequestException`, HTTP 400).
- Duplicate student email / company name is rejected (`DuplicateResourceException`, HTTP 409).
- A student can have only one `Placement` record (enforced at the service layer).
- All input DTOs are validated with Jakarta Bean Validation (`@NotBlank`, `@Email`, `@DecimalMin`, etc.).
- All exceptions return a consistent JSON error body via `GlobalExceptionHandler`.

## Sample Request Flow

1. `POST /api/companies` — register a company.
2. `POST /api/jobs` — create a job opening linked to that company.
3. `POST /api/students` — register a student (skills are auto-created if new).
4. `POST /api/applications` — student applies for the job (eligibility + duplicate checks run here).
5. `PATCH /api/applications/{id}/status?status=SHORTLISTED` — admin shortlists the student.
6. `POST /api/interviews` — schedule an interview round for that application.
7. `PUT /api/interviews/{id}` — record the interview result/feedback.
8. `PATCH /api/applications/{id}/status?status=SELECTED` — mark student as selected.
9. `POST /api/placements` — create the final placement record.
10. `GET /api/dashboard` — view aggregate placement statistics.

## Sample Request Bodies

**Create Company**
```json
{ "name": "Acme Corp", "contactEmail": "hr@acme.com", "contactPhone": "9876543210", "address": "Pune, India" }
```

**Create Job**
```json
{
  "title": "Software Engineer",
  "description": "Backend role using Java and Spring Boot",
  "eligibleCgpa": 7.0,
  "requiredSkills": "Java, Spring Boot, SQL",
  "packageOffered": 1200000,
  "applicationDeadline": "2026-12-31",
  "companyId": 1
}
```

**Create Student**
```json
{
  "name": "Aaryan Sharma",
  "email": "aaryan@example.com",
  "department": "Information Technology",
  "cgpa": 8.5,
  "resumeSummary": "IT student skilled in Java and Python",
  "skills": ["Java", "Spring Boot", "SQL"]
}
```

**Apply for Job**
```json
{ "studentId": 1, "jobId": 1 }
```

## Testing
Import `postman/PlacementManagementSystem.postman_collection.json` into Postman to try all endpoints.

## Future Enhancements (from synopsis)
Spring Security + JWT auth, role-based access control, resume upload, AI-based resume analysis,
job recommendations, aptitude tests, interview analytics, placement prediction, React frontend, cloud deployment.

## Notes for Viva / Explanation
- The request lifecycle: `Controller` receives the HTTP request → maps it to a `DTO` → delegates to the
  `Service` layer → service applies business rules and talks to `Repository` → Hibernate/JPA persists to MySQL
  → entity is mapped back to a DTO → returned as JSON.
- Relationships and cascade choices (e.g. `Student 1--N Application` with `CascadeType.ALL, orphanRemoval = true`)
  are documented inline in the entity classes so you can explain the reasoning during evaluation.
