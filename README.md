# ChefCourier

ChefCourier is a full-stack food ordering and delivery application.

## Technologies

- Java
- Spring Boot
- Spring Security
- JWT
- MySQL
- React
- Vite
- Axios
- React Router

## Run the ChefCourier Backend

Open **Terminal 1**:

```bash
cd ~/Documents/GitHub/ChefCourier/chef-courier-backend
```

Set the database password:

```bash
export DB_PASSWORD='ChefCourier@123'
```

Start the Spring Boot backend:

```bash
./mvnw spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

Test it in another terminal:

```bash
curl http://localhost:8080/api/health
```

Stop the backend with:

```text
Control + C
```

---

## Run the ChefCourier Frontend

Open **Terminal 2**:

```bash
cd ~/Documents/GitHub/ChefCourier/chef-courier-frontend
```

Install packages the first time:

```bash
npm install
```

Start React:

```bash
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

Stop the frontend with:

```text
Control + C
```

---

## Run Both Applications

### Terminal 1

```bash
cd ~/Documents/GitHub/ChefCourier/chef-courier-backend
export DB_PASSWORD='ChefCourier@123'
./mvnw spring-boot:run
```

### Terminal 2

```bash
cd ~/Documents/GitHub/ChefCourier/chef-courier-frontend
npm run dev
```

Keep both terminals open while using ChefCourier.

## Before Running the Backend

MySQL must be running. Check:

```bash
lsof -nP -iTCP:3306 -sTCP:LISTEN
```

When MySQL is running, this command displays a `mysqld` process.

You can also test MySQL directly:

```bash
mysql -u chefcourier_app -p -h 127.0.0.1 -P 3306 chefcourier_db
```

Use the password:

```text
ChefCourier@123
```
