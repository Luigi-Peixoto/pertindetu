import { delay, http, HttpResponse } from "msw";
import { conversations, offerings, providers } from "./data";
import type { ConversationMessage, Review } from "../types";

const avg = (reviews: Review[]) =>
  reviews.length ? reviews.reduce((sum, review) => sum + review.rating, 0) / reviews.length : 0;

const matches = (value: string, query: string) => value.toLowerCase().includes(query.toLowerCase());

export const handlers = [
  http.get("/api/search", async ({ request }) => {
    await delay(250);
    const url = new URL(request.url);
    const query = url.searchParams.get("q")?.trim() ?? "";
    const type = url.searchParams.get("type") ?? "all";
    const category = url.searchParams.get("category") ?? "all";
    const sort = url.searchParams.get("sort") ?? "relevance";

    let results = offerings.filter((offering) => {
      const typeMatch = type === "all" || offering.type === type;
      const categoryMatch = category === "all" || offering.category === category;
      const textMatch =
        !query ||
        [offering.title, offering.description, offering.providerName, offering.neighborhood, ...offering.tags].some((item) =>
          matches(item, query)
        );

      return typeMatch && categoryMatch && textMatch;
    });

    if (sort === "price") {
      results = [...results].sort((a, b) => a.price - b.price);
    }

    if (sort === "rating") {
      results = [...results].sort((a, b) => avg(b.reviews) - avg(a.reviews));
    }

    const providerResults = providers.filter((provider) => {
      if (!query) return true;
      return [provider.name, provider.headline, provider.neighborhood, ...provider.specialties].some((item) => matches(item, query));
    });

    return HttpResponse.json({ offerings: results, providers: providerResults });
  }),

  http.get("/api/offerings/:id", async ({ params }) => {
    await delay(220);
    const offering = offerings.find((item) => item.id === params.id);
    if (!offering) return new HttpResponse(null, { status: 404 });
    return HttpResponse.json(offering);
  }),

  http.get("/api/providers/:id", async ({ params }) => {
    await delay(220);
    const provider = providers.find((item) => item.id === params.id);
    if (!provider) return new HttpResponse(null, { status: 404 });
    return HttpResponse.json({
      ...provider,
      offerings: offerings.filter((offering) => offering.providerId === provider.id)
    });
  }),

  http.get("/api/messages", async () => {
    await delay(180);
    return HttpResponse.json(conversations);
  }),

  http.post("/api/messages/:conversationId", async ({ params, request }) => {
    const body = (await request.json()) as { text: string };
    const conversation = conversations.find((item) => item.id === params.conversationId);
    if (!conversation) return new HttpResponse(null, { status: 404 });

    const message: ConversationMessage = {
      id: `m-${Date.now()}`,
      sender: "client",
      text: body.text,
      createdAt: new Date().toISOString()
    };

    conversation.messages.push(message);
    conversation.unread = 0;

    return HttpResponse.json(message, { status: 201 });
  }),

  http.post("/api/reviews", async ({ request }) => {
    const body = (await request.json()) as {
      targetType: "offering" | "provider";
      targetId: string;
      author: string;
      rating: number;
      comment: string;
    };

    const review: Review = {
      id: `r-${Date.now()}`,
      author: body.author || "Cliente",
      rating: body.rating,
      comment: body.comment,
      createdAt: new Date().toISOString().slice(0, 10)
    };

    if (body.targetType === "offering") {
      const offering = offerings.find((item) => item.id === body.targetId);
      if (!offering) return new HttpResponse(null, { status: 404 });
      offering.reviews.unshift(review);
      return HttpResponse.json(offering, { status: 201 });
    }

    const provider = providers.find((item) => item.id === body.targetId);
    if (!provider) return new HttpResponse(null, { status: 404 });
    provider.reviews.unshift(review);
    return HttpResponse.json(provider, { status: 201 });
  })
];
