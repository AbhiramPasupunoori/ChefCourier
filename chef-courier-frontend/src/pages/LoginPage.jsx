import {
  useState,
} from "react";

import {
  useNavigate,
} from "react-router-dom";

import {
  useAuth,
} from "../context/AuthContext";

export default function LoginPage() {
  const [form, setForm] =
    useState({
      email: "",
      password: "",
    });

  const [error, setError] =
    useState("");

  const { login } = useAuth();

  const navigate =
    useNavigate();

  async function submit(event) {
    event.preventDefault();

    try {
      const user =
        await login(form);

      if (
        user.role ===
        "RESTAURANT_OWNER"
      ) {
        navigate("/owner");
      } else if (
        user.role ===
        "DELIVERY_PARTNER"
      ) {
        navigate("/delivery");
      } else if (
        user.role === "ADMIN"
      ) {
        navigate("/admin");
      } else {
        navigate("/");
      }
    } catch (requestError) {
      setError(
        requestError.response?.data
          ?.message ||
          "Login failed"
      );
    }
  }

  return (
    <main className="form-page">
      <form
        className="form-card"
        onSubmit={submit}
      >
        <h1>Login</h1>

        {error && (
          <p className="error">
            {error}
          </p>
        )}

        <input
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={(event) =>
            setForm({
              ...form,
              email: event.target.value,
            })
          }
        />

        <input
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={(event) =>
            setForm({
              ...form,
              password: event.target.value,
            })
          }
        />

        <button type="submit">
          Login
        </button>
      </form>
    </main>
  );
}
