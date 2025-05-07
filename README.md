# rock-com
**rock-com** is a web-based system designed to support the organization and management of racket sport tournaments, including tennis, table tennis, and badminton.

The platform allows organizers to configure competitions, referees to manage match data, and participants to register and track their progress.

## Workflow
- **Quality of code:** npx prettier . --write npx eslint . for frontend
- **Branch naming convention:** `feature/<JIRA-TICKET-ID>`, `bugfix/<JIRA-TICKET-ID>`, etc.
- **Pull Request naming convention:** `[<JIRA-TICKET-ID>] - <JIRA task title>`
- **Merging:** The person who opened the PR is responsible for merging it.
- **Approval:** At least one team member must approve the PR before it can be merged.
- **Communication:** Share a link to your PR in the team Discord channel to keep everyone informed.

## Definition of Done
- Existing functionality remains unaffected (nothing that worked before is broken)
- Necessary tests are created and passing
- All merge conflicts are resolved
- Code has been reviewed and approved
- Requested changes from the review are applied
- Pull request is merged
- Feature branch is deleted after merge
- Corresponding JIRA ticket is marked as done/closed

## How to run the project

### Frontend

#### Setup
npm install


#### Running the Backend
npm run dev


---
### Database

#### Prerequisites
- Docker
- Docker desktop running

#### First-Time Setup
To build and start the PostgreSQL container for the first time, run:
```bash
docker compose -f backend/db/docker-compose.yml up --build -d
```

#### Updating the Schema
If you modify the database schema (e.g., by changing .sql files used during initialization), or if the schema has changed in the repository (e.g., after pulling updates made by others), you need to **reset** the database:
```bash
docker compose -f backend/db/docker-compose.yml down -v
```
This command shuts down the containers and removes any associated volumes, including the database data.

Then, start it again:
```bash
docker compose -f backend/db/docker-compose.yml up --build -d
```

---
### Backend
#### Prerequisites
- Java 21 or newer
- Make sure the PostgreSQL container is up and running before starting the backend

#### Setup
1. Navigate to the `backend` directory. 
2. Ensure that the file `mvnw` is executable. If not, run:
```bash
chmod u+x mvnw
```

#### Running the Backend
Start the Spring Boot app from `backend` directory with:
```
./mvnw spring-boot:run
```