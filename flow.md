# 🚀 ConferenceHub Flow & Testing Scenario

This document provides a step-by-step guide to verify that all your microservices are running correctly and demonstrates a complete standard usage scenario (e.g., creating a keynote, conferencing, etc.).

---

## 🛠️ 1. Verifying Services (Health & Availability)

After running `docker compose up -d`, check the following endpoints to ensure everything is running perfectly.

### Infrastructure Services
- **Service Discovery (Eureka)**: Open [http://localhost:8761](http://localhost:8761) in your browser. You should see all services registered (gateway, keynote, conference, notification).
- **Config Server**: Visit [http://localhost:8888/api-gateway/default](http://localhost:8888/api-gateway/default) to check if the central configs are loading successfully.
- **Keycloak (Auth)**: Open [http://localhost:8084/](http://localhost:8084/). Access the admin console to verify your realms and users.

### Microservices Health Checks (Actuator)
These endpoints return `{"status":"UP"}` if the service is fully started.
- **API Gateway**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Keynote Service**: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
- **Conference Service**: [http://localhost:8082/actuator/health](http://localhost:8082/actuator/health)
- **Notification Service**: [http://localhost:8083/actuator/health](http://localhost:8083/actuator/health)

### Monitoring Stack
- **Prometheus (Metrics)**: [http://localhost:9090](http://localhost:9090)
- **Grafana (Dashboards)**: [http://localhost:3000](http://localhost:3000)
- **Zipkin (Distributed Tracing)**: [http://localhost:9411](http://localhost:9411)

---

## 🎬 2. The Main Scenario: "Create Keynote & Conference"

We will interact with the system entirely through the **API Gateway (Port 8080)** to simulate a real frontend client.

> **Note:** Due to Keycloak security, requests to `/api/**` require an `Authorization: Bearer <token>` header. If security is enabled for testing, first generate a token via Keycloak and replace `<token>` in the commands below.

### Step A: Create a Speaker (Keynote)
First, we need a speaker for our conference. Let's create a new Keynote profile.

**Request:**
```bash
curl -X POST http://localhost:8080/api/keynotes \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <token>" \
     -d '{
           "firstName": "John",
           "lastName": "Doe",
           "email": "john.doe@example.com",
           "function": "Software Architect"
         }'
```

**Verify Keynote:**
```bash
curl -X GET http://localhost:8080/api/keynotes \
     -H "Authorization: Bearer <token>"
```
*(Make a note of the newly created Keynote's `id`, e.g., `1`)*

### Step B: Create a Conference
Now, we create a conference and attach the speaker (using the Keynote `id`).

**Request:**
```bash
curl -X POST http://localhost:8080/api/conferences \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <token>" \
     -d '{
           "title": "Microservices in Action",
           "type": "TECH",
           "date": "2026-06-15",
           "startTime": "09:00",
           "endTime": "11:00",
           "duration": 120,
           "registeredAttendees": 250,
           "score": 4.8,
           "keynoteId": 1
         }'
```
*Behind the scenes: The `conference-service` performs a synchronous call via OpenFeign to `keynote-service` to verify Keynote `1` exists.*

**Verify Conference:**
```bash
curl -X GET http://localhost:8080/api/conferences \
     -H "Authorization: Bearer <token>"
```

### Step C: Verify Asynchronous Notifications (Kafka)
When the conference is successfully created, the `conference-service` publishes a `CONFERENCE_CREATED` event to **Kafka**. The `notification-service` is listening.

**To verify the flow worked:**
1. Check the logs of the notification service:
   ```bash
   docker compose logs notification-service
   ```
2. You should see a log message stating an event was received and a notification was sent or saved to the DB (e.g., `Received Conference Event for: Microservices in Action`).
3. Alternatively, check the Notifications endpoint if one is exposed:
   ```bash
   curl -X GET http://localhost:8080/api/notifications \
        -H "Authorization: Bearer <token>"
   ```

### Step D: Distributed Tracing using Zipkin (Optional)
To see how a single request interacts with multiple services:
1. Open **Zipkin** at [http://localhost:9411](http://localhost:9411).
2. Click **"Run Query"**.
3. You will see graphical traces for the `POST /api/conferences` request, showing exactly how long the request spent inside the Gateway, Conference Service, and the Feign call to Keynote Service!

<!-- this is from keyalocak -->
### login credentails:
> grant_type: password
> username: admin
> password: admin
> client_id: conferencehub-client