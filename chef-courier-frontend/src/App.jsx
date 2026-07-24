import { useEffect, useState } from "react";
import {
  getBackendHealth,
  getDatabaseHealth,
} from "./api/healthApi";
import "./App.css";

function App() {
  const [backendStatus, setBackendStatus] = useState("Checking...");
  const [databaseStatus, setDatabaseStatus] = useState("Checking...");
  const [error, setError] = useState("");

  useEffect(() => {
    async function checkApplication() {
      try {
        const backend = await getBackendHealth();
        const database = await getDatabaseHealth();

        setBackendStatus(backend.status);
        setDatabaseStatus(database.status);
      } catch (requestError) {
        console.error(requestError);

        setBackendStatus("DOWN");
        setDatabaseStatus("UNKNOWN");
        setError(
          "ChefCourier could not connect to the backend. Make sure Spring Boot is running on port 8080."
        );
      }
    }

    checkApplication();
  }, []);

  return (
    <main className="app">
      <section className="hero">
        <div className="logo">CC</div>
        <p className="eyebrow">Food ordering and delivery</p>
        <h1>ChefCourier</h1>
        <p className="description">
          Discover restaurants, order your favourite food and track delivery
          from the restaurant to your doorstep.
        </p>

        <div className="status-grid">
          <article className="status-card">
            <span>Spring Boot backend</span>
            <strong>{backendStatus}</strong>
          </article>
          <article className="status-card">
            <span>MySQL database</span>
            <strong>{databaseStatus}</strong>
          </article>
        </div>

        {error && <p className="error-message">{error}</p>}
      </section>
    </main>
  );
}

export default App;
