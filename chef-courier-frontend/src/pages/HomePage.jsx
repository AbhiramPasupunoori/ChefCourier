import {
  useEffect,
  useState,
} from "react";

import {
  Link,
} from "react-router-dom";

import {
  restaurantApi,
} from "../api/api";

export default function HomePage() {
  const [restaurants, setRestaurants] =
    useState([]);

  const [search, setSearch] =
    useState("");

  async function loadRestaurants() {
    const response =
      await restaurantApi.list(search);

    setRestaurants(response.data);
  }

  useEffect(() => {
    loadRestaurants();
  }, []);

  return (
    <main className="container">
      <section className="hero">
        <p className="eyebrow">
          Fast food delivery
        </p>

        <h1>
          Your favourite meals,
          delivered by ChefCourier
        </h1>

        <div className="search-row">
          <input
            value={search}
            onChange={(event) =>
              setSearch(event.target.value)
            }
            placeholder="Search restaurant or cuisine"
          />

          <button onClick={loadRestaurants}>
            Search
          </button>
        </div>
      </section>

      <section className="grid">
        {restaurants.map(
          (restaurant) => (
            <article
              className="card"
              key={restaurant.id}
            >
              <img
                src={
                  restaurant.imageUrl ||
                  "https://placehold.co/600x400"
                }
                alt={restaurant.name}
              />

              <h2>{restaurant.name}</h2>

              <p>
                {restaurant.cuisineType}
              </p>

              <p>
                ⭐ {restaurant.averageRating}
              </p>

              <Link
                className="button"
                to={`/restaurants/${restaurant.id}`}
              >
                View Menu
              </Link>
            </article>
          )
        )}
      </section>
    </main>
  );
}
