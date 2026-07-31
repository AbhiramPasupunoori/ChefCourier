import {
  useEffect,
  useState,
} from "react";

import {
  useParams,
} from "react-router-dom";

import {
  cartApi,
  restaurantApi,
} from "../api/api";

import {
  useAuth,
} from "../context/AuthContext";

export default function RestaurantPage() {
  const { restaurantId } =
    useParams();

  const { user } = useAuth();

  const [restaurant, setRestaurant] =
    useState(null);

  const [menu, setMenu] =
    useState([]);

  const [message, setMessage] =
    useState("");

  useEffect(() => {
    Promise.all([
      restaurantApi.one(restaurantId),
      restaurantApi.menu(restaurantId),
    ]).then(([restaurantResponse, menuResponse]) => {
      setRestaurant(
        restaurantResponse.data
      );

      setMenu(menuResponse.data);
    });
  }, [restaurantId]);

  async function addToCart(menuItemId) {
    await cartApi.add({
      menuItemId,
      quantity: 1,
    });

    setMessage(
      "Item added to cart"
    );
  }

  if (!restaurant) {
    return (
      <main className="container">
        Loading...
      </main>
    );
  }

  return (
    <main className="container">
      <section className="hero small">
        <h1>{restaurant.name}</h1>

        <p>{restaurant.description}</p>

        <p>
          {restaurant.cuisineType}
          {" • "}
          {restaurant.city}
        </p>
      </section>

      {message && (
        <p className="success">
          {message}
        </p>
      )}

      <section className="grid">
        {menu.map((item) => (
          <article
            className="card"
            key={item.id}
          >
            <img
              src={
                item.imageUrl ||
                "https://placehold.co/600x400"
              }
              alt={item.name}
            />

            <h2>{item.name}</h2>

            <p>{item.description}</p>

            <strong>
              ₹{item.price}
            </strong>

            {user?.role === "CUSTOMER" && (
              <button
                onClick={() =>
                  addToCart(item.id)
                }
              >
                Add to Cart
              </button>
            )}
          </article>
        ))}
      </section>
    </main>
  );
}
