## Onboarding-Tool

It's about onboarding new employees at a company.

---
### DEMO of the project (only Frontend): https://onboarding.boivalenko.de

---
A new employee has a certain number of onboarding tasks to complete. 
When all of these tasks have been completed by the employee, 
the workflow should end. The employee is logged out or deactivated 
from the system and notified by email. 
The onboarding team also receives the notification.

There are 3 extra Docker containers:

- PostgreSQL database
- Java backend (Spring Boot)
- Angular frontend

The entry point for a user is a website which is available under the
address: **http://localhost:4200/**

---

### Prerequisites

In order to run this application you need to install two tools: **Docker** & **Docker Compose**.

Instructions how to install **Docker** on [Ubuntu](https://docs.docker.com/install/linux/docker-ce/ubuntu/), [Windows](https://docs.docker.com/docker-for-windows/install/) , [Mac](https://docs.docker.com/docker-for-mac/install/) .

**Dosker Compose** is already included in installation packs for *Windows* and *Mac*, so only Ubuntu users needs to follow [this instructions](https://docs.docker.com/compose/install/) .

### How to run it?

An entire application can be run with a single command in a terminal:

onboarding/app/
```
$ rm target
$ mvn clean package --% -Dmaven.test.skip=true
```

onboarding/ui/
```
$ rm dist
$ ng build
```

onboarding/
```
$ docker-compose up -d
```

If you want to stop it use following command:

```
$ docker-compose down
```

#### Onboarding-Tool Database

PostgreSQL database *postgres* contains only single schema: *onboarding*

After running the app it can be accessible using this connectors:

- Host: *localhost*
- Database: *postgres*
- Schema: *onboarding*
- User: *postgres*
- Password: *root*

Like other parts of application Postgres database is containerized and
the definition of its Docker container can be found in
*docker-compose.yml* file.

#### Onboarding-Tool (Backend)
(german) https://github.com/andreyboiv/onboarding-tool/blob/master/app/README.md

#### Onboarding-Tool (Frontend)
It can be entered using link: **http://localhost:4200/**

(german) https://github.com/andreyboiv/onboarding-tool/blob/master/ui/README.md
