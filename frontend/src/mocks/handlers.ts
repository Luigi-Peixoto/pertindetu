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
    return HttpResponse.json([...conversations].sort((a, b) => b.updatedAt.localeCompare(a.updatedAt)));
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

    const reply: ConversationMessage = {
      id: `m-${Date.now()}-reply`,
      sender: "provider",
      text: "Recebi sua mensagem. Ja te respondo com os detalhes.",
      createdAt: new Date(Date.now() + 1000).toISOString()
    };

    conversation.messages.push(message);
    conversation.messages.push(reply);
    conversation.updatedAt = reply.createdAt;
    conversation.status = "active";
    conversation.unread = 0;

    return HttpResponse.json(conversation, { status: 201 });
  }),

  http.post("/api/conversations", async ({ request }) => {
    const body = (await request.json()) as { providerId: string; offeringId?: string };
    const provider = providers.find((item) => item.id === body.providerId);
    if (!provider) return new HttpResponse(null, { status: 404 });

    const offering = offerings.find((item) => item.id === body.offeringId);
    const existing = conversations.find((item) => item.providerId === provider.id && item.offeringId === offering?.id);
    if (existing) {
      existing.status = "active";
      return HttpResponse.json(existing);
    }

    const now = new Date().toISOString();
    const conversation = {
      id: `c-${provider.id}-${Date.now()}`,
      providerId: provider.id,
      providerName: provider.name,
      providerAvatarUrl: provider.avatarUrl,
      offeringId: offering?.id,
      offeringTitle: offering?.title,
      updatedAt: now,
      status: "active" as const,
      unread: 0,
      messages: [
        {
          id: `m-${Date.now()}-hello`,
          sender: "provider" as const,
          text: "Oi! Me manda sua duvida por aqui e combinamos os detalhes.",
          createdAt: now
        }
      ]
    };

    conversations.unshift(conversation);
    return HttpResponse.json(conversation, { status: 201 });
  }),

  http.post("/api/providers/:id/portfolio", async ({ params, request }) => {
    const provider = providers.find((item) => item.id === params.id);
    if (!provider) return new HttpResponse(null, { status: 404 });

    const body = (await request.json()) as { title: string; imageUrl: string };
    const photo = {
      id: `pf-${Date.now()}`,
      title: body.title || "Nova foto",
      imageUrl: body.imageUrl,
      uploadedAt: new Date().toISOString().slice(0, 10)
    };

    provider.portfolio.unshift(photo);
    return HttpResponse.json(photo, { status: 201 });
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
