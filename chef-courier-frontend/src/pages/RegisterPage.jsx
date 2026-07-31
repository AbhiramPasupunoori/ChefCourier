import {
  useState,
} from "react";

import {
  useNavigate,
} from "react-router-dom";

import {
  useAuth,
} from "../context/AuthContext";

export default function RegisterPage() {
  const [form, setForm] =
    useState({
      fullName: "",
      email: "",
      password: "",
      phoneNumber: "",
      role: "CUSTOMER",
    });

  const { register } =
    useAuth();

  const navigate =
    useNavigate();

  async function submit(event) {
    event.preventDefault();

    await register(form);

    navigate("/");
  }

  function update(event) {
    setForm({
      ...form,
      [event.target.name]:
        event.target.value,
    });
  }

  return (
    <main className="form-page">
      <form
        className="form-card"
        onSubmit={submit}
      >
        <h1>Create account</h1>

        <input
          name="fullName"
          placeholder="Full name"
          value={form.fullName}
          onChange={update}
        />

        <input
          name="email"
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={update}
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={update}
        />

        <input
          name="phoneNumber"
          placeholder="Phone number"
          value={form.phoneNumber}
          onChange={update}
        />

        <select
          name="role"
          value={form.role}
          onChange={update}
        >
          <option value="CUSTOMER">
            Customer
          </option>

          <option value="RESTAURANT_OWNER">
            Restaurant Owner
          </option>

          <option value="DELIVERY_PARTNER">
            Delivery Partner
          </option>
        </select>

        <button type="submit">
          Register
        </button>
      </form>
    </main>
  );
}
