import {
  useEffect,
  useMemo,
  useState,
} from "react";

import {
  Link,
} from "react-router-dom";

import {
  restaurantApi,
} from "../api/api";

const cuisineFilters = [
  "All",
  "Indian",
  "Italian",
  "Asian",
  "Healthy",
];

const seededRestaurantImages = {
  "Spice Route Kitchen":
    "/images/restaurants/spice-route-kitchen.png",
  "Urban Pizza Co.":
    "/images/restaurants/urban-pizza-co.png",
  "Wok and Roll":
    "/images/restaurants/wok-and-roll.png",
  "Green Bowl Cafe":
    "/images/restaurants/green-bowl-cafe.png",
};

function getRestaurantImage(restaurant) {
  const seededImage =
    seededRestaurantImages[restaurant.name];

  if (
    seededImage
    && (!restaurant.imageUrl
      || restaurant.imageUrl.includes(
        "placehold.co"
      ))
  ) {
    return seededImage;
  }

  return restaurant.imageUrl
    || "/images/restaurants/spice-route-kitchen.png";
}

export default function HomePage() {
  const [
    restaurants,
    setRestaurants,
  ] = useState([]);

  const [
    search,
    setSearch,
  ] = useState("");

  const [
    selectedCuisine,
    setSelectedCuisine,
  ] = useState("All");

  const [
    loading,
    setLoading,
  ] = useState(true);

  const [
    error,
    setError,
  ] = useState("");

  async function loadRestaurants() {
    try {
      setLoading(true);
      setError("");

      const response =
        await restaurantApi.list(
          search
        );

      setRestaurants(
        response.data
      );
    } catch (requestError) {
      console.error(requestError);

      setError(
        "Restaurants could not be loaded."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadRestaurants();
  }, []);

  const filteredRestaurants =
    useMemo(() => {
      if (
        selectedCuisine === "All"
      ) {
        return restaurants;
      }

      return restaurants.filter(
        (restaurant) =>
          restaurant.cuisineType
            ?.toLowerCase()
            .includes(
              selectedCuisine.toLowerCase()
            )
      );
    }, [
      restaurants,
      selectedCuisine,
    ]);

  function handleSubmit(event) {
    event.preventDefault();
    loadRestaurants();
  }

  return (
    <main className="container">
      <section className="hero">
        <p className="eyebrow">
          Fresh food. Fast delivery.
        </p>

        <h1>
          Delicious meals delivered
          from local kitchens.
        </h1>

        <p>
          Discover restaurants across
          Hyderabad, browse their menus
          and track your delivery from
          kitchen to doorstep.
        </p>

        <form
          className="search-row"
          onSubmit={handleSubmit}
        >
          <input
            value={search}
            onChange={(event) =>
              setSearch(
                event.target.value
              )
            }
            placeholder="Search restaurant, cuisine or city"
          />

          <button type="submit">
            Search Restaurants
          </button>
        </form>
      </section>

      <section className="section-heading">
        <div>
          <h2>
            Popular restaurants
          </h2>

          <p>
            Browse restaurants and
            cuisines available near you.
          </p>
        </div>
      </section>

      <div className="filter-row">
        {cuisineFilters.map(
          (cuisine) => (
            <button
              type="button"
              key={cuisine}
              className={
                selectedCuisine
                  === cuisine
                  ? "filter-button active"
                  : "filter-button"
              }
              onClick={() =>
                setSelectedCuisine(
                  cuisine
                )
              }
            >
              {cuisine}
            </button>
          )
        )}
      </div>

      {loading && (
        <p>Loading restaurants...</p>
      )}

      {error && (
        <p className="error">
          {error}
        </p>
      )}

      {!loading &&
        !filteredRestaurants.length && (
          <div className="empty-state">
            No restaurants matched
            your search.
          </div>
        )}

      <section className="grid">
        {filteredRestaurants.map(
          (restaurant) => (
            <article
              className="card"
              key={restaurant.id}
            >
              <img
                src={getRestaurantImage(
                  restaurant
                )}
                alt={restaurant.name}
              />

              <h2>
                {restaurant.name}
              </h2>

              <p>
                {restaurant.description}
              </p>

              <div className="restaurant-meta">
                <span className="badge">
                  {restaurant.cuisineType}
                </span>

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
              </div>

              <Link
                className="button"
                to={
                  `/restaurants/${restaurant.id}`
                }
              >
                Explore Menu
              </Link>
            </article>
          )
        )}
      </section>
    </main>
  );
}
