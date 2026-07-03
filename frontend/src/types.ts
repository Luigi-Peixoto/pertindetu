export type OfferingType = "service" | "product";

export type Review = {
  id: string;
  author: string;
  rating: number;
  comment: string;
  createdAt: string;
};

export type PortfolioPhoto = {
  id: string;
  title: string;
  imageUrl: string;
  uploadedAt: string;
};

export type Provider = {
  id: string;
  name: string;
  headline: string;
  avatarUrl: string;
  coverUrl: string;
  city: string;
  neighborhood: string;
  verified: boolean;
  responseTime: string;
  completedJobs: number;
  bio: string;
  specialties: string[];
  portfolio: PortfolioPhoto[];
  reviews: Review[];
};

export type Offering = {
  id: string;
  type: OfferingType;
  title: string;
  description: string;
  price: number;
  unit: string;
  imageUrl: string;
  category: string;
  tags: string[];
  providerId: string;
  providerName: string;
  providerAvatarUrl: string;
  city: string;
  neighborhood: string;
  available: boolean;
  reviews: Review[];
};

export type ConversationMessage = {
  id: string;
  sender: "client" | "provider";
  text: string;
  createdAt: string;
};

export type Conversation = {
  id: string;
  providerId: string;
  providerName: string;
  providerAvatarUrl: string;
  offeringId?: string;
  offeringTitle?: string;
  updatedAt: string;
  status: "active" | "archived";
  unread: number;
  messages: ConversationMessage[];
};

export type SearchResponse = {
  offerings: Offering[];
  providers: Provider[];
};
