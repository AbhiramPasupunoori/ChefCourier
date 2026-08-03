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

const seededDishImages = {
  "Chicken Biryani":
    "/images/dishes/chicken-biryani.png",
  "Paneer Biryani":
    "/images/dishes/paneer-biryani.png",
  "Butter Chicken":
    "/images/dishes/butter-chicken.png",
  "Garlic Naan":
    "/images/dishes/garlic-naan.png",
  "Margherita Pizza":
    "/images/dishes/margherita-pizza.png",
  "Farmhouse Pizza":
    "/images/dishes/farmhouse-pizza.png",
  "Cheesy Garlic Bread":
    "/images/dishes/cheesy-garlic-bread.png",
  Tiramisu:
    "/images/dishes/tiramisu.png",
  "Vegetable Hakka Noodles":
    "/images/dishes/hakka-noodles.png",
  "Chicken Fried Rice":
    "/images/dishes/chicken-fried-rice.png",
  "Chilli Paneer":
    "/images/dishes/chilli-paneer.png",
  "Vegetable Spring Rolls":
    "/images/dishes/vegetable-spring-rolls.png",
  "Grilled Chicken Protein Bowl":
    "/images/dishes/protein-bowl.png",
  "Paneer Quinoa Bowl":
    "/images/dishes/paneer-quinoa-bowl.png",
  "Avocado Toast":
    "/images/dishes/avocado-toast.png",
  "Berry Smoothie":
    "/images/dishes/berry-smoothie.png",
};

function getDishImage(item) {
  const seededImage =
    seededDishImages[item.name];

  if (
    seededImage
    && (!item.imageUrl
      || item.imageUrl.includes(
        "placehold.co"
      ))
  ) {
    return seededImage;
  }

  return item.imageUrl
    || `https://placehold.co/900x600/F97316/FFFFFF?text=${encodeURIComponent(
      item.name
    )}`;
}

export default function RestaurantPage() {
  const { restaurantId } =
    useParams();

  const { user } =
    useAuth();

  const [
    restaurant,
    setRestaurant,
  ] = useState(null);

  const [
    menu,
    setMenu,
  ] = useState([]);

  const [
    message,
    setMessage,
  ] = useState("");

  const [
    error,
    setError,
  ] = useState("");

  useEffect(() => {
    async function loadPage() {
      try {
        const [
          restaurantResponse,
          menuResponse,
        ] = await Promise.all([
          restaurantApi.one(
            restaurantId
          ),
          restaurantApi.menu(
            restaurantId
          ),
        ]);

        setRestaurant(
          restaurantResponse.data
        );

        setMenu(
          menuResponse.data
        );
      } catch (requestError) {
        console.error(requestError);

        setError(
          "Restaurant details could not be loaded."
        );
      }
    }

    loadPage();
  }, [restaurantId]);

  async function addToCart(
    menuItemId
  ) {
    try {
      setMessage("");
      setError("");

      await cartApi.add({
        menuItemId,
        quantity: 1,
      });

      setMessage(
        "Food item added to your cart."
      );
    } catch (requestError) {
      setError(
        requestError.response?.data
          ?.message
        ||
        "Item could not be added."
      );
    }
  }

  if (error && !restaurant) {
    return (
      <main className="container">
        <p className="error">
          {error}
        </p>
      </main>
    );
  }

  if (!restaurant) {
    return (
      <main className="container">
        Loading restaurant...
      </main>
    );
  }

  return (
    <main className="container">
      <section className="hero small">
        <p className="eyebrow">
          {restaurant.cuisineType}
        </p>

        <h1>
          {restaurant.name}
        </h1>

        <p>
          {restaurant.description}
        </p>

        <div className="restaurant-meta">
          <span className="badge">
            {restaurant.city}
          </span>

          <span className="badge">
            ⭐
            {" "}
            {Number(
              restaurant.averageRating
              || 0
            ).toFixed(1)}
          </span>

          <span className="badge">
            {restaurant.active
              ? "Accepting orders"
              : "Currently closed"}
          </span>
        </div>
      </section>

      {message && (
        <p className="success">
          {message}
        </p>
      )}

      {error && (
        <p className="error">
          {error}
        </p>
      )}

      <section className="section-heading">
        <div>
          <h2>Restaurant menu</h2>

          <p>
            Choose your favourite
            dishes from the menu.
          </p>
        </div>
      </section>

      {!menu.length && (
        <div className="empty-state">
          This restaurant has not
          added menu items yet.
        </div>
      )}

      <section className="grid">
        {menu.map((item) => (
          <article
            className="card"
            key={item.id}
          >
            <img
              src={getDishImage(item)}
              alt={item.name}
            />

            <div className="restaurant-meta">
              <span
                className={
                  item.vegetarian
                    ? "badge vegetarian"
                    : "badge non-vegetarian"
                }
              >
                {item.vegetarian
                  ? "Vegetarian"
                  : "Non-Vegetarian"}
              </span>

              <span className="badge">
                {
                  item.preparationMinutes
                }
                {" min"}
              </span>
            </div>

            <h2>{item.name}</h2>

            <p>
              {item.description}
            </p>

            <strong className="price">
              ₹{item.price}
            </strong>

            {user?.role ===
              "CUSTOMER" && (
              <button
                onClick={() =>
                  addToCart(item.id)
                }
              >
                Add to Cart
              </button>
            )}

            {!user && (
              <p>
                Login as a customer
                to order this item.
              </p>
            )}
          </article>
        ))}
      </section>
    </main>
  );
}
