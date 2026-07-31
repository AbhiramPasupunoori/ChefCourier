# ChefCourier

ChefCourier is a full-stack food ordering and delivery application built with
React, Vite, Spring Boot, Spring Security, JWT, JPA, and MySQL.

## Features

- Customer registration and JWT-based login
- Role-based access for customers, restaurant owners, delivery partners, and admins
- Restaurant and menu management
- Cart, address, ordering, payment-status, and review workflows
- Admin restaurant approval and delivery assignment
- Seeded demo restaurants, menu items, and users
- Separate development servers or a single merged application

## Required Software

- Git
- Java 21 or newer
- MySQL 8
- Node.js 20.19+ or 22.12+ and npm (separate development mode only)

Optional editor extensions:

- Extension Pack for Java
- Spring Boot Extension Pack
- ESLint
- Prettier

> This is not a Python/Django project, so it has no `requirements.txt`,
> `manage.py`, or Python migrations. Backend dependencies are installed by the
> Maven wrapper, and frontend dependencies are installed from `package-lock.json`.

## Quick Start (Local Development)

Clone the project:

```bash
git clone https://github.com/AbhiramPasupunoori/ChefCourier.git
cd ChefCourier
```

Install and start MySQL on macOS:

```bash
brew install mysql
brew services start mysql
```

Sign in and create the local database:

```bash
mysql -u root -p
```

Run the SQL commands from the [Database Setup](#database-setup) section below,
then continue with the backend and frontend commands.

Start the backend in terminal 1:

```bash
cd chef-courier-backend

export DB_URL='jdbc:mysql://127.0.0.1:3306/chefcourier_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata'
export DB_USERNAME='chefcourier_app'
export DB_PASSWORD='ChefCourier@123'
export JWT_SECRET='replace-this-with-a-long-random-development-secret'

./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` and set the variables with PowerShell:

```powershell
$env:DB_URL='jdbc:mysql://127.0.0.1:3306/chefcourier_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata'
$env:DB_USERNAME='chefcourier_app'
$env:DB_PASSWORD='ChefCourier@123'
$env:JWT_SECRET='replace-this-with-a-long-random-development-secret'
./mvnw.cmd spring-boot:run
```

Start the frontend in terminal 2:

```bash
cd chef-courier-frontend
npm ci
npm run dev
```

Open http://localhost:5173/.

## Database Setup

Install and start MySQL 8, then sign in as root:

```bash
mysql -u root -p
```

If a fresh local MySQL installation has no root password, press Enter at the
password prompt.

Create the database and application user:

```sql
CREATE DATABASE IF NOT EXISTS chefcourier_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'chefcourier_app'@'localhost'
  IDENTIFIED BY 'ChefCourier@123';

ALTER USER 'chefcourier_app'@'localhost'
  IDENTIFIED BY 'ChefCourier@123';

GRANT ALL PRIVILEGES ON chefcourier_db.*
  TO 'chefcourier_app'@'localhost';

FLUSH PRIVILEGES;
EXIT;
```

Then start the backend and frontend using the development commands above.

## Database Migrations

No manual migration command is currently required. Spring JPA uses
`spring.jpa.hibernate.ddl-auto=update`, so starting the backend creates or
updates the MySQL tables automatically.

For production, use a migration tool such as Flyway or Liquibase before making
schema changes instead of relying on automatic Hibernate updates.

## Run the Merged Application

The merged build compiles React, copies it into Spring Boot, and serves both the
website and API from port `8080`.

### 1. Start MySQL

Start the locally installed MySQL service on macOS:

```bash
brew services start mysql
```

On Windows, start the MySQL service from Windows Services or MySQL Installer.
The database and user must already be created using the
[Database Setup](#database-setup) instructions.

### 2. Build the Frontend and Backend Together

```bash
cd chef-courier-backend
./mvnw clean package -Pfrontend
```

### 3. Configure the Application

```bash
export DB_URL='jdbc:mysql://127.0.0.1:3306/chefcourier_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata'
export DB_USERNAME='chefcourier_app'
export DB_PASSWORD='ChefCourier@123'
export JWT_SECRET='replace-this-with-a-long-random-secret'
```

### 4. Run the Merged Application

```bash
java -jar target/chef-courier-backend-0.0.1-SNAPSHOT.jar
```

Keep that terminal running. After the application reports that it has started,
open another terminal and use the command for your operating system.

### 5. Open the Application

```bash
# macOS
open http://localhost:8080/

# Linux
xdg-open http://localhost:8080/
```

On Windows PowerShell:

```powershell
Start-Process http://localhost:8080/
```

You can also manually visit http://localhost:8080/. The merged application uses:

- Frontend: http://localhost:8080/
- Backend API: http://localhost:8080/api/
- Health check: http://localhost:8080/api/health

### 6. Stop the Application

Press `Control+C` in the terminal running the JAR. To stop the local MySQL
service on macOS, run:

```bash
brew services stop mysql
```

## Environment Variables

- `DB_URL` - MySQL JDBC connection URL
- `DB_USERNAME` - MySQL application username
- `DB_PASSWORD` - MySQL application password
- `JWT_SECRET` - JWT signing secret; use a long, private value
- `SEED_DEMO` - create demo data on startup (`true` by default)
- `VITE_API_URL` - optional frontend API base URL; leave unset to use the Vite proxy locally

## Demo Accounts

All demo accounts use the password `Password@123`.

| Role | Email |
| --- | --- |
| Customer | `customer@chefcourier.com` |
| Administrator | `admin@chefcourier.com` |
| Delivery partner | `delivery@chefcourier.com` |
| Restaurant owner | `indian.owner@chefcourier.com` |

## Useful Commands

```bash
# Backend tests
cd chef-courier-backend
./mvnw test

# Frontend checks and production build
cd ../chef-courier-frontend
npm run lint
npm run build

# API and database health checks (while backend is running)
curl http://localhost:8080/api/health
curl http://localhost:8080/api/health/database

# Stop the local MySQL service on macOS
brew services stop mysql
```

## Production Notes

- Set a unique `JWT_SECRET` and secure database credentials.
- Set `SEED_DEMO=false` if demo accounts must not be created.
- Keep MySQL data in persistent storage and back it up.
- Run the application behind HTTPS using a reverse proxy or hosting platform.
- Do not commit secrets or production environment files.
