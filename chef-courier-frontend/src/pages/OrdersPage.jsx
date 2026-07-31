import {
  useEffect,
  useState,
} from "react";

import {
  orderApi,
} from "../api/api";

export default function OrdersPage() {
  const [orders, setOrders] =
    useState([]);

  async function load() {
    const response =
      await orderApi.list();

    setOrders(response.data);
  }

  useEffect(() => {
    load();
  }, []);

  return (
    <main className="container">
      <h1>My Orders</h1>

      {orders.map((order) => (
        <article
          className="card order-card"
          key={order.id}
        >
          <h2>
            Order #{order.id}
          </h2>

          <p>
            {order.restaurantName}
          </p>

          <p>
            Status:
            {" "}
            <strong>
              {order.status}
            </strong>
          </p>

          <p>
            Payment:
            {" "}
            {order.paymentStatus}
          </p>

          <p>
            Total: ₹{order.totalAmount}
          </p>

          {order.items.map((item) => (
            <p key={item.id}>
              {item.itemName}
              {" × "}
              {item.quantity}
            </p>
          ))}

          {[
            "PENDING_PAYMENT",
            "PLACED",
            "CONFIRMED",
          ].includes(order.status) && (
            <button
              onClick={async () => {
                await orderApi.cancel(
                  order.id
                );

                load();
              }}
            >
              Cancel
            </button>
          )}

          {order.status ===
            "DELIVERED" && (
            <button
              onClick={async () => {
                await orderApi.review(
                  order.id,
                  {
                    rating: 5,
                    comment:
                      "Great food and delivery",
                  }
                );

                alert(
                  "Review submitted"
                );
              }}
            >
              Add Review
            </button>
          )}
        </article>
      ))}
    </main>
  );
}
