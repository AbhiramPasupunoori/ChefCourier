import {
  useEffect,
  useState,
} from "react";

import {
  ownerApi,
} from "../api/api";

export default function OwnerPage() {
  const [restaurants, setRestaurants] =
    useState([]);

  const [selected, setSelected] =
    useState(null);

  const [orders, setOrders] =
    useState([]);

  async function loadRestaurants() {
    const response =
      await ownerApi.restaurants();

    setRestaurants(response.data);

    if (response.data.length) {
      setSelected(response.data[0]);
    }
  }

  async function loadOrders() {
    if (!selected) {
      return;
    }

    const response =
      await ownerApi.orders(
        selected.id
      );

    setOrders(response.data);
  }

  useEffect(() => {
    loadRestaurants();
  }, []);

  useEffect(() => {
    loadOrders();
  }, [selected]);

  async function updateStatus(
    orderId,
    status
  ) {
    await ownerApi.updateOrder(
      selected.id,
      orderId,
      status
    );

    loadOrders();
  }

  return (
    <main className="container">
      <h1>
        Restaurant Owner Dashboard
      </h1>

      {restaurants.map(
        (restaurant) => (
          <button
            key={restaurant.id}
            onClick={() =>
              setSelected(restaurant)
            }
          >
            {restaurant.name}
          </button>
        )
      )}

      {selected && (
        <>
          <h2>
            {selected.name} Orders
          </h2>

          {orders.map((order) => (
            <article
              className="card order-card"
              key={order.id}
            >
              <h3>
                Order #{order.id}
              </h3>

              <p>{order.customerName}</p>

              <p>{order.status}</p>

              {order.status ===
                "PLACED" && (
                <>
                  <button
                    onClick={() =>
                      updateStatus(
                        order.id,
                        "CONFIRMED"
                      )
                    }
                  >
                    Confirm
                  </button>

                  <button
                    onClick={() =>
                      updateStatus(
                        order.id,
                        "REJECTED"
                      )
                    }
                  >
                    Reject
                  </button>
                </>
              )}

              {order.status ===
                "CONFIRMED" && (
                <button
                  onClick={() =>
                    updateStatus(
                      order.id,
                      "PREPARING"
                    )
                  }
                >
                  Start Preparing
                </button>
              )}

              {order.status ===
                "PREPARING" && (
                <button
                  onClick={() =>
                    updateStatus(
                      order.id,
                      "READY_FOR_PICKUP"
                    )
                  }
                >
                  Ready for Pickup
                </button>
              )}
            </article>
          ))}
        </>
      )}
    </main>
  );
}
