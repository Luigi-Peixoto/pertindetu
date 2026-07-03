import type { Conversation, Offering, PortfolioPhoto, Provider, SearchResponse } from "../types";

const parse = async <T>(responsePromise: Promise<Response>): Promise<T> => {
  const response = await responsePromise;

  if (!response.ok) {
    throw new Error("Nao foi possivel carregar os dados.");
  }

  return response.json() as Promise<T>;
};

export const api = {
  search: (params: URLSearchParams) => parse<SearchResponse>(fetch(`/api/search?${params.toString()}`)),
  offering: (id: string) => parse<Offering>(fetch(`/api/offerings/${id}`)),
  provider: (id: string) =>
    parse<Provider & { offerings: Offering[] }>(fetch(`/api/providers/${id}`)),
  conversations: () => parse<Conversation[]>(fetch("/api/messages")),
  startConversation: (payload: { providerId: string; offeringId?: string }) =>
    parse<Conversation>(fetch("/api/conversations", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    })),
  sendMessage: (conversationId: string, text: string) =>
    parse<Conversation>(fetch(`/api/messages/${conversationId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ text })
    })),
  uploadPortfolioPhoto: (providerId: string, payload: { title: string; imageUrl: string }) =>
    parse<PortfolioPhoto>(fetch(`/api/providers/${providerId}/portfolio`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    })),
  review: (payload: {
    targetType: "offering" | "provider";
    targetId: string;
    author: string;
    rating: number;
    comment: string;
  }) =>
    parse<unknown>(fetch("/api/reviews", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    }))
};
