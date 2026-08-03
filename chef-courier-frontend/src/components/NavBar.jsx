import {
  Link,
} from "react-router-dom";

import {
  useAuth,
} from "../context/AuthContext";

export default function NavBar() {
  const {
    user,
    logout,
  } = useAuth();

  return (
    <nav className="navbar">
      <Link
        className="brand"
        to="/"
        aria-label="Chef Courier home"
        title="Go to home page"
      >
        <img
          src="/images/branding/chef-courier-logo-transparent.png"
          alt="Chef Courier"
        />
      </Link>

      <div className="nav-links">
        <Link to="/">
          Restaurants
        </Link>

        {user?.role === "CUSTOMER" && (
          <>
            <Link to="/addresses">
              Addresses
            </Link>

            <Link to="/cart">
              Cart
            </Link>

            <Link to="/orders">
              Orders
            </Link>
          </>
        )}

        {user?.role ===
          "RESTAURANT_OWNER" && (
          <Link to="/owner">
            Owner Dashboard
          </Link>
        )}

        {user?.role ===
          "DELIVERY_PARTNER" && (
          <Link to="/delivery">
            Deliveries
          </Link>
        )}

        {user?.role === "ADMIN" && (
          <Link to="/admin">
            Admin
          </Link>
        )}

        {!user ? (
          <>
            <Link to="/login">
              Login
            </Link>

            <Link to="/register">
              Register
            </Link>
          </>
        ) : (
          <button
            className="link-button"
            onClick={logout}
          >
            Logout
          </button>
        )}
      </div>
    </nav>
  );
}
