import type { Review } from "../types";

export const averageRating = (reviews: Review[]) => {
  if (!reviews.length) return 0;
  return reviews.reduce((sum, review) => sum + review.rating, 0) / reviews.length;
};

export const formatRating = (value: number) => value.toFixed(1).replace(".", ",");

export const formatCurrency = (value: number) =>
  new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(value);
