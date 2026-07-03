import type { Conversation, Offering, Provider, SearchResponse } from "../types";

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
  sendMessage: (conversationId: string, text: string) =>
    parse<unknown>(fetch(`/api/messages/${conversationId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ text })
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
