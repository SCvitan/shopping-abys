const BASE = "http://localhost:8080/api/v1";

async function request<T>(
  url: string,
  options?: RequestInit
): Promise<T> {
  const res = await fetch(`${BASE}${url}`, {
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...(options?.headers ?? {}),
    },
    ...options,
  });

  if (!res.ok) {
    const body = await res.text();
    throw new Error(body || res.statusText);
  }

  const text = await res.text();
  if (!text) return {} as T;
  return JSON.parse(text) as T;
}

// Auth
export function register(email: string, password: string) {
  return request<{ email: string }>("/auth/register", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}

export function login(email: string, password: string) {
  return request<{ email: string }>("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}

export function logout() {
  return request<void>("/auth/logout", { method: "POST" });
}

// Products
export interface Product {
  id: number;
  title: string;
  description: string;
  price: number;
  thumbnail?: string;
  category?: string;
  favorite?: boolean;
}

export interface ProductPage {
  products: Product[];
  total: number;
  skip: number;
  limit: number;
}

export function fetchProducts(page = 0): Promise<ProductPage> {
  return request<ProductPage>(`/products?skip=${page * 12}&limit=12`);
}

export function fetchProduct(id: number): Promise<Product> {
  return request<Product>(`/products/${id}`);
}

export function toggleFavorite(id: number) {
  return request<void>(`/products/${id}/favorite`, { method: "POST" });
}

// Cart
export interface CartItem {
  productId: number;
  title: string;
  price: number;
  quantity: number;
}

export interface Cart {
  items: CartItem[];
  totalPrice: number;
}

export function fetchCart(): Promise<Cart> {
  return request<Cart>("/cart");
}

export function addToCart(productId: number, quantity: number) {
  return request<void>("/cart/items", {
    method: "POST",
    body: JSON.stringify({ productId, quantity }),
  });
}

export function removeFromCart(productId: number) {
  return request<void>(`/cart/items/${productId}`, { method: "DELETE" });
}
