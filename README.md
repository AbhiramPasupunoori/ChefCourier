# ChefCourier

ChefCourier is a full-stack food ordering and delivery application built with
Spring Boot, Spring Security, JWT, MySQL, React, Vite, Axios, and React Router.

The production build runs on one origin:

```text
http://localhost:8080/       React application
http://localhost:8080/api/   Spring Boot API
```

## Prerequisites

Install the following software before starting:

- Java 21 or newer
- Node.js 20.19+ or 22.12+ and npm (development mode only)
- MySQL
- Git

On macOS with Homebrew:

```bash
brew install openjdk@21 mysql git
brew services start mysql
```

## First-time setup after cloning

Clone the repository and enter it:

```bash
git clone <repository-url> ChefCourier
cd ChefCourier
```

Create the local database and application account:

```bash
mysql -u root
```

Run the following SQL:

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

Build the combined frontend and backend:

```bash
cd chef-courier-backend
./mvnw clean package -Pfrontend
```

The `frontend` Maven profile downloads a project-local Node.js 22 runtime,
performs `npm ci`, builds React, copies the generated files into the Spring Boot
classpath, and creates this executable:

```text
chef-courier-backend/target/chef-courier-backend-0.0.1-SNAPSHOT.jar
```

## Run the combined application

From `chef-courier-backend`:

```bash
export DB_URL='jdbc:mysql://127.0.0.1:3306/chefcourier_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata'
export DB_USERNAME='chefcourier_app'
export DB_PASSWORD='ChefCourier@123'
export JWT_SECRET='replace-this-with-a-long-random-production-secret'

java -jar target/chef-courier-backend-0.0.1-SNAPSHOT.jar
```

Open http://localhost:8080. Test the API from another terminal:

```bash
curl http://localhost:8080/api/health
```

Stop the application with `Control+C`.

## Run again after closing and reopening Terminal

Shell environment variables do not survive when Terminal closes. Start MySQL,
enter the cloned project, export the settings again, and run the existing JAR:

```bash
brew services start mysql

cd /path/to/ChefCourier/chef-courier-backend

export DB_URL='jdbc:mysql://127.0.0.1:3306/chefcourier_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata'
export DB_USERNAME='chefcourier_app'
export DB_PASSWORD='ChefCourier@123'
export JWT_SECRET='replace-this-with-the-same-long-random-secret'

java -jar target/chef-courier-backend-0.0.1-SNAPSHOT.jar
```

If the JAR is missing or the repository has been updated, rebuild first:

```bash
./mvnw clean package -Pfrontend
```

## Development mode

Development mode keeps Vite hot reload and Spring Boot restart support. It uses
two terminals, while Vite proxies `/api` requests to Spring Boot.

Terminal 1:

```bash
cd /path/to/ChefCourier/chef-courier-backend

export DB_URL='jdbc:mysql://127.0.0.1:3306/chefcourier_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata'
export DB_USERNAME='chefcourier_app'
export DB_PASSWORD='ChefCourier@123'
export JWT_SECRET='replace-this-with-a-long-random-development-secret'

./mvnw spring-boot:run
```

Terminal 2:

```bash
cd /path/to/ChefCourier/chef-courier-frontend
npm ci
npm run dev
```

Open http://localhost:5173 during development.

## Demo accounts

All seeded demo users use the password `Password@123`.

| Role | Email |
| --- | --- |
| Customer | `customer@chefcourier.com` |
| Administrator | `admin@chefcourier.com` |
| Delivery partner | `delivery@chefcourier.com` |
| Indian restaurant owner | `indian.owner@chefcourier.com` |
| Pizza restaurant owner | `pizza.owner@chefcourier.com` |
| Asian restaurant owner | `asian.owner@chefcourier.com` |
| Healthy restaurant owner | `healthy.owner@chefcourier.com` |

## Production notes

- Use a unique, secret `JWT_SECRET`; do not commit it.
- Use secure database credentials instead of the development defaults.
- Put the application behind an HTTPS reverse proxy or a managed hosting
  platform. Both the UI and API remain on the same public domain.
- Set `APP_SEED_DEMO=false` in production if demo accounts must not be created.
