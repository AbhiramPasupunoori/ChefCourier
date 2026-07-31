import client from "./client";

export const authApi = {
  register: (data) =>
    client.post("/api/auth/register", data),

  login: (data) =>
    client.post("/api/auth/login", data),

  me: () =>
    client.get("/api/auth/me"),
};

export const addressApi = {
  list: () =>
    client.get("/api/addresses"),

  create: (data) =>
    client.post("/api/addresses", data),

  update: (id, data) =>
    client.put(`/api/addresses/${id}`, data),

  remove: (id) =>
    client.delete(`/api/addresses/${id}`),
};

export const restaurantApi = {
  list: (search = "") =>
    client.get(
      `/api/restaurants?search=${encodeURIComponent(
        search
      )}`
    ),

  one: (id) =>
    client.get(`/api/restaurants/${id}`),

  menu: (id) =>
    client.get(
      `/api/restaurants/${id}/menu-items`
    ),

  reviews: (id) =>
    client.get(
      `/api/reviews/restaurant/${id}`
    ),
};

export const cartApi = {
  get: () =>
    client.get("/api/customer/cart"),

  add: (data) =>
    client.post(
      "/api/customer/cart/items",
      data
    ),

  update: (id, data) =>
    client.put(
      `/api/customer/cart/items/${id}`,
      data
    ),

  remove: (id) =>
    client.delete(
      `/api/customer/cart/items/${id}`
    ),
};

export const orderApi = {
  list: () =>
    client.get("/api/customer/orders"),

  place: (data) =>
    client.post("/api/customer/orders", data),

  pay: (id) =>
    client.post(
      `/api/customer/orders/${id}/pay`
    ),

  cancel: (id) =>
    client.patch(
      `/api/customer/orders/${id}/cancel`
    ),

  review: (id, data) =>
    client.post(
      `/api/customer/orders/${id}/review`,
      data
    ),
};

export const ownerApi = {
  restaurants: () =>
    client.get("/api/owner/restaurants"),

  createRestaurant: (data) =>
    client.post(
      "/api/owner/restaurants",
      data
    ),

  menuItems: (restaurantId) =>
    client.get(
      `/api/owner/restaurants/${restaurantId}/menu-items`
    ),

  addCategory: (restaurantId, data) =>
    client.post(
      `/api/owner/restaurants/${restaurantId}/categories`,
      data
    ),

  addMenuItem: (restaurantId, data) =>
    client.post(
      `/api/owner/restaurants/${restaurantId}/menu-items`,
      data
    ),

  orders: (restaurantId) =>
    client.get(
      `/api/owner/restaurants/${restaurantId}/orders`
    ),

  updateOrder: (
    restaurantId,
    orderId,
    status
  ) =>
    client.patch(
      `/api/owner/restaurants/${restaurantId}/orders/${orderId}/status`,
      { status }
    ),
};

export const adminApi = {
  restaurants: () =>
    client.get("/api/admin/restaurants"),

  updateRestaurantStatus: (id, status) =>
    client.patch(
      `/api/admin/restaurants/${id}/status`,
      { status }
    ),

  orders: () =>
    client.get("/api/admin/orders"),

  partners: () =>
    client.get(
      "/api/admin/delivery-partners"
    ),

  assignDelivery: (
    orderId,
    deliveryPartnerId
  ) =>
    client.post(
      `/api/admin/orders/${orderId}/assign-delivery`,
      { deliveryPartnerId }
    ),
};

export const deliveryApi = {
  tasks: () =>
    client.get("/api/delivery/tasks"),

  updateStatus: (id, status) =>
    client.patch(
      `/api/delivery/tasks/${id}/status`,
      { status }
    ),
};
