import {
  useEffect,
  useState,
} from "react";

import {
  addressApi,
} from "../api/api";

const initialForm = {
  label: "Home",
  addressLine1: "",
  addressLine2: "",
  city: "",
  state: "",
  postalCode: "",
  country: "India",
  defaultAddress: true,
};

export default function AddressPage() {
  const [addresses, setAddresses] =
    useState([]);

  const [form, setForm] =
    useState(initialForm);

  async function load() {
    const response =
      await addressApi.list();

    setAddresses(response.data);
  }

  useEffect(() => {
    load();
  }, []);

  async function submit(event) {
    event.preventDefault();

    await addressApi.create(form);

    setForm(initialForm);

    load();
  }

  return (
    <main className="container">
      <h1>My Addresses</h1>

      <form
        className="form-card"
        onSubmit={submit}
      >
        {Object.keys(initialForm)
          .filter(
            (key) =>
              key !== "defaultAddress"
          )
          .map((key) => (
            <input
              key={key}
              placeholder={key}
              value={form[key]}
              onChange={(event) =>
                setForm({
                  ...form,
                  [key]:
                    event.target.value,
                })
              }
            />
          ))}

        <button type="submit">
          Add Address
        </button>
      </form>

      <section className="grid">
        {addresses.map((address) => (
          <article
            className="card"
            key={address.id}
          >
            <h2>{address.label}</h2>

            <p>
              {address.addressLine1}
            </p>

            <p>
              {address.city},
              {" "}
              {address.state}
            </p>

            <button
              onClick={async () => {
                await addressApi.remove(
                  address.id
                );

                load();
              }}
            >
              Delete
            </button>
          </article>
        ))}
      </section>
    </main>
  );
}
