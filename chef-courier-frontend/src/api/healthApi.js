import api from "./axiosInstance";

export async function getBackendHealth() {
  const response = await api.get("/api/health");
  return response.data;
}

export async function getDatabaseHealth() {
  const response = await api.get("/api/health/database");
  return response.data;
}
