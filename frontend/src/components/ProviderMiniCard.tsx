import { Link } from "react-router-dom";
import { BadgeCheck, MapPin } from "lucide-react";
import type { Provider } from "../types";
import { averageRating, formatRating } from "../utils/reviews";
import { Stars } from "./Stars";

type ProviderMiniCardProps = {
  provider: Provider;
};

export function ProviderMiniCard({ provider }: ProviderMiniCardProps) {
  const rating = averageRating(provider.reviews);

  return (
    <article className="providerMiniCard">
      <img src={provider.avatarUrl} alt="" />
      <div>
        <Link to={`/prestadores/${provider.id}`} className="providerName">
          {provider.name}
          {provider.verified && <BadgeCheck size={17} />}
        </Link>
        <p>{provider.headline}</p>
        <div className="ratingLine">
          <Stars value={rating} size="sm" />
          <span>{formatRating(rating)} ({provider.reviews.length})</span>
        </div>
        <span className="location"><MapPin size={15} /> {provider.neighborhood}, {provider.city}</span>
      </div>
    </article>
  );
}
