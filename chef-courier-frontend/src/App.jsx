import {
  BrowserRouter,
  Route,
  Routes,
} from "react-router-dom";

import {
  AuthProvider,
} from "./context/AuthContext";

import NavBar from "./components/NavBar";
import Protected from "./components/Protected";

import HomePage from "./pages/HomePage";
import RestaurantPage from "./pages/RestaurantPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import AddressPage from "./pages/AddressPage";
import CartPage from "./pages/CartPage";
import OrdersPage from "./pages/OrdersPage";
import OwnerPage from "./pages/OwnerPage";
import DeliveryPage from "./pages/DeliveryPage";
import AdminPage from "./pages/AdminPage";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <NavBar />

        <Routes>
          <Route
            path="/"
            element={<HomePage />}
          />

          <Route
            path="/restaurants/:restaurantId"
            element={<RestaurantPage />}
          />

          <Route
            path="/login"
            element={<LoginPage />}
          />

          <Route
            path="/register"
            element={<RegisterPage />}
          />

          <Route
            path="/addresses"
            element={
              <Protected roles={["CUSTOMER"]}>
                <AddressPage />
              </Protected>
            }
          />

          <Route
            path="/cart"
            element={
              <Protected roles={["CUSTOMER"]}>
                <CartPage />
              </Protected>
            }
          />

          <Route
            path="/orders"
            element={
              <Protected roles={["CUSTOMER"]}>
                <OrdersPage />
              </Protected>
            }
          />

          <Route
            path="/owner"
            element={
              <Protected
                roles={[
                  "RESTAURANT_OWNER",
                ]}
              >
                <OwnerPage />
              </Protected>
            }
          />

          <Route
            path="/delivery"
            element={
              <Protected
                roles={[
                  "DELIVERY_PARTNER",
                ]}
              >
                <DeliveryPage />
              </Protected>
            }
          />

          <Route
            path="/admin"
            element={
              <Protected roles={["ADMIN"]}>
                <AdminPage />
              </Protected>
            }
          />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
