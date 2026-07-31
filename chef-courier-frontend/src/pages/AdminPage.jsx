import {
  useEffect,
  useState,
} from "react";

import {
  adminApi,
} from "../api/api";

export default function AdminPage() {
  const [restaurants, setRestaurants] =
    useState([]);

  const [orders, setOrders] =
    useState([]);

  const [partners, setPartners] =
    useState([]);

  async function load() {
    const [
      restaurantResponse,
      orderResponse,
      partnerResponse,
    ] = await Promise.all([
      adminApi.restaurants(),
      adminApi.orders(),
      adminApi.partners(),
    ]);

    setRestaurants(
      restaurantResponse.data
    );

    setOrders(orderResponse.data);

    setPartners(partnerResponse.data);
  }

  useEffect(() => {
    load();
  }, []);

  async function approve(id) {
    await adminApi.updateRestaurantStatus(
      id,
      "APPROVED"
    );

    load();
  }

  async function reject(id) {
    await adminApi.updateRestaurantStatus(
      id,
      "REJECTED"
    );

    load();
  }

  async function assign(orderId) {
    if (!partners.length) {
      alert(
        "No delivery partner available"
      );

      return;
    }

    await adminApi.assignDelivery(
      orderId,
      partners[0].id
    );

    alert("Delivery assigned");
  }

  return (
    <main className="container">
      <h1>Admin Dashboard</h1>

      <h2>Restaurants</h2>

      {restaurants.map(
        (restaurant) => (
          <article
            className="list-row"
            key={restaurant.id}
          >
            <div>
              <strong>
                {restaurant.name}
              </strong>

              <p>{restaurant.status}</p>
            </div>

            {restaurant.status ===
              "PENDING" && (
              <div>
                <button
                  onClick={() =>
                    approve(
                      restaurant.id
                    )
                  }
                >
                  Approve
                </button>

                <button
                  onClick={() =>
                    reject(
                      restaurant.id
                    )
                  }
                >
                  Reject
                </button>
              </div>
            )}
          </article>
        )
      )}

      <h2>Orders Ready for Pickup</h2>

      {orders
        .filter(
          (order) =>
            order.status ===
            "READY_FOR_PICKUP"
        )
        .map((order) => (
          <article
            className="list-row"
            key={order.id}
          >
            <span>
              Order #{order.id}
            </span>

            <button
              onClick={() =>
                assign(order.id)
              }
            >
              Assign Delivery
            </button>
          </article>
        ))}
    </main>
  );
}
