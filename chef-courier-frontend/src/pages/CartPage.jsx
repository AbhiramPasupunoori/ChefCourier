import {
  useEffect,
  useState,
} from "react";

import {
  useNavigate,
} from "react-router-dom";

import {
  addressApi,
  cartApi,
  orderApi,
} from "../api/api";

export default function CartPage() {
  const [cart, setCart] =
    useState(null);

  const [addresses, setAddresses] =
    useState([]);

  const [addressId, setAddressId] =
    useState("");

  const [paymentMethod, setPaymentMethod] =
    useState("COD");

  const navigate =
    useNavigate();

  async function load() {
    const [
      cartResponse,
      addressResponse,
    ] = await Promise.all([
      cartApi.get(),
      addressApi.list(),
    ]);

    setCart(cartResponse.data);
    setAddresses(addressResponse.data);

    if (addressResponse.data.length) {
      setAddressId(
        addressResponse.data[0].id
      );
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function checkout() {
    const response =
      await orderApi.place({
        addressId: Number(addressId),
        paymentMethod,
        specialInstructions: "",
      });

    if (
      response.data.status ===
      "PENDING_PAYMENT"
    ) {
      await orderApi.pay(
        response.data.id
      );
    }

    navigate("/orders");
  }

  if (!cart) {
    return (
      <main className="container">
        Loading...
      </main>
    );
  }

  return (
    <main className="container">
      <h1>Shopping Cart</h1>

      {cart.items.map((item) => (
        <article
          className="list-row"
          key={item.id}
        >
          <div>
            <strong>{item.name}</strong>

            <p>
              {item.quantity}
              {" × ₹"}
              {item.unitPrice}
            </p>
          </div>

          <button
            onClick={async () => {
              await cartApi.remove(
                item.id
              );

              load();
            }}
          >
            Remove
          </button>
        </article>
      ))}

      <h2>
        Subtotal: ₹{cart.subtotal}
      </h2>

      <select
        value={addressId}
        onChange={(event) =>
          setAddressId(
            event.target.value
          )
        }
      >
        {addresses.map((address) => (
          <option
            key={address.id}
            value={address.id}
          >
            {address.label}
            {" - "}
            {address.addressLine1}
          </option>
        ))}
      </select>

      <select
        value={paymentMethod}
        onChange={(event) =>
          setPaymentMethod(
            event.target.value
          )
        }
      >
        <option value="COD">
          Cash on Delivery
        </option>

        <option value="ONLINE">
          Simulated Online Payment
        </option>
      </select>

      <button
        disabled={
          !cart.items.length ||
          !addressId
        }
        onClick={checkout}
      >
        Place Order
      </button>
    </main>
  );
}
