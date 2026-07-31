import {
  useEffect,
  useState,
} from "react";

import {
  deliveryApi,
} from "../api/api";

const nextStatus = {
  ASSIGNED: "ACCEPTED",
  ACCEPTED: "PICKED_UP",
  PICKED_UP: "OUT_FOR_DELIVERY",
  OUT_FOR_DELIVERY: "DELIVERED",
};

export default function DeliveryPage() {
  const [tasks, setTasks] =
    useState([]);

  async function load() {
    const response =
      await deliveryApi.tasks();

    setTasks(response.data);
  }

  useEffect(() => {
    load();
  }, []);

  async function progress(task) {
    await deliveryApi.updateStatus(
      task.id,
      nextStatus[task.status]
    );

    load();
  }

  return (
    <main className="container">
      <h1>Delivery Tasks</h1>

      {tasks.map((task) => (
        <article
          className="card"
          key={task.id}
        >
          <h2>
            Order #{task.orderId}
          </h2>

          <p>
            Pickup:
            {" "}
            {task.restaurantAddress}
          </p>

          <p>
            Drop:
            {" "}
            {task.deliveryAddress}
          </p>

          <p>
            Status:
            {" "}
            {task.status}
          </p>

          {nextStatus[task.status] && (
            <button
              onClick={() =>
                progress(task)
              }
            >
              Mark as
              {" "}
              {nextStatus[task.status]}
            </button>
          )}
        </article>
      ))}
    </main>
  );
}
