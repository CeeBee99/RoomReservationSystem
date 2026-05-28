# Room Reservation System

A distributed microservices application built with Spring Boot that allows users to browse a room catalogue and reserve rooms for time windows. The system prevents overlapping reservations, supports role-based access control, and processes confirmations asynchronously via event-driven messaging.

---

## Demo Credentials

| Role | Username | Password |
|---|---|---|
| User | User | Password |
| Room Manager | *(ask team)* | *(ask team)* |

---

## Features

- Browse available rooms and reserve for a time window
- Overlap detection via stateless OverlapEngine
- Asynchronous confirmation workflow via RabbitMQ
- Role-based access control with Keycloak OIDC
- Circuit breakers and rate limiting at the Edge layer
- Full CI/CD pipeline per service

---

## Architecture

The system follows a strict layered microservices architecture with 8 independently deployable Spring Boot services.

```
Browser
   |
EdgeService (Spring Cloud Gateway + Keycloak Auth + Rate Limiter + Circuit Breaker)
   |
   |--- /rooms/**      --> RoomManager
   |--- /reservations/** --> ReservationManager
                               |
                               |--- OverlapEngine (stateless)
                               |--- ReservationAccess (PostgreSQL)
                               |--- RabbitMQ (async events)
                                         |
                                    EventProcessor
```

---

## Services

| Service | Layer | Responsibility |
|---|---|---|
| EdgeService | Client | Routing, authentication, rate limiting, circuit breaking |
| RoomManager | Manager | Room catalogue lifecycle |
| ReservationManager | Manager | Reservation lifecycle and event orchestration |
| OverlapEngine | Engine | Stateless overlap detection algorithm |
| RoomAccess | DataAccess | CRUD on rooms table (PostgreSQL) |
| ReservationAccess | DataAccess | CRUD on reservations table (PostgreSQL) |
| EventProcessor | Utility | Consumes RabbitMQ events, publishes confirmations |
| ConfigService | Config | Spring Cloud Config Server backed by Git |

---

## Tech Stack

- Java / Spring Boot
- Spring Cloud Gateway
- Spring Security / Keycloak (OIDC)
- RabbitMQ (Spring Cloud Stream)
- PostgreSQL
- Redis (sessions and rate limiting)
- Resilience4J (circuit breakers)
- Docker / Kubernetes
- GitHub Actions (CI/CD per service)

---

## Getting Started

### Prerequisites

- Docker installed
- kubectl installed
- kind installed (`brew install kind` or equivalent)
- Java 17+

### Setup

```bash
# Clone all service repositories
# (Each service has its own repository per project spec)

# Start full local system with Docker Compose
docker compose up -d

# Or deploy to Kubernetes cluster
kind create cluster
kubectl apply -f k8s/
```

### Accessing the App

Once running, the application is accessible via the EdgeService ingress. All external traffic routes through EdgeService only.

```
http://localhost/
```

Sign in with:
- Username: `User`
- Password: `Password`

---

## Event Flow

```
Browser --> EdgeService --> ReservationManager --> OverlapEngine (CLEAR/CONFLICT)
                                    |
                              ReservationAccess (persist PENDING)
                                    |
                              RabbitMQ: ReservationCreated
                                    |
                            EventProcessor (log + publish ReservationConfirmed)
                                    |
                              ReservationManager (update to CONFIRMED)
```

---

## CI/CD Pipeline (per service)

Each service has its own GitHub Actions pipeline:

1. Checkout code
2. Set up Java 17
3. Vulnerability scan
4. Kubeval manifest validation
5. `./gradlew build` and test
6. Docker image build and push (on main)
7. Deploy to cluster (on main)

---

## Kubernetes Manifests

Each service includes:

- `Deployment` with graceful shutdown (`preStop: sleep 5`, `server.shutdown: graceful`)
- `ClusterIP Service`
- `Ingress` routing all external traffic to EdgeService via nginx

---

## Design Decisions

**Stateless OverlapEngine** — overlap detection logic lives in a dedicated stateless Engine service behind a strategy interface. Swapping the overlap algorithm (e.g. from StrictOverlapStrategy to BufferOverlapStrategy requiring 15 minutes between reservations) requires redeploying only this service, leaving all other services untouched.

**Event driven confirmation** — ReservationManager publishes a ReservationCreated event and immediately returns 201 to the browser. Confirmation processing happens asynchronously via EventProcessor, decoupling the confirmation workflow from the reservation request.

**Single database owner** — each DataAccess service owns exactly one database. No two services share a database. Cross-domain data access happens through service API calls only.

**EdgeService as sole entry point** — no service other than EdgeService is reachable from outside the cluster. Authentication, rate limiting, and circuit breaking are enforced at this layer only.

---

## What This Project Demonstrates

- Distributed microservices architecture with strict layered contracts
- Kubernetes orchestration with Deployment, Service, and Ingress manifests
- Asynchronous event driven messaging with RabbitMQ
- OIDC authentication and RBAC with Keycloak
- Circuit breaker and rate limiting patterns with Resilience4J and Redis
- Per service CI/CD pipelines with GitHub Actions
- Strategy pattern for swappable algorithms without modifying dependent services

---

## License

MIT
