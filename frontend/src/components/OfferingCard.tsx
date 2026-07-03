import { Link } from "react-router-dom";
import { MapPin, Package, Wrench } from "lucide-react";
import type { Offering } from "../types";
import { averageRating, formatCurrency, formatRating } from "../utils/reviews";
import { Stars } from "./Stars";

type OfferingCardProps = {
  offering: Offering;
};

export function OfferingCard({ offering }: OfferingCardProps) {
  const rating = averageRating(offering.reviews);

  return (
    <article className="offeringCard">
      <Link to={`/ofertas/${offering.id}`} className="cardImageLink" aria-label={`Ver ${offering.title}`}>
        <img src={offering.imageUrl} alt="" />
      </Link>
      <div className="cardBody">
        <div className="pillRow">
          <span className="typePill">
            {offering.type === "service" ? <Wrench size={14} /> : <Package size={14} />}
            {offering.type === "service" ? "Servico" : "Produto"}
          </span>
          <span>{offering.category}</span>
        </div>
        <Link to={`/ofertas/${offering.id}`} className="cardTitle">{offering.title}</Link>
        <p>{offering.description}</p>
        <div className="ratingLine">
          <Stars value={rating} size="sm" />
          <span>{formatRating(rating)} ({offering.reviews.length})</span>
        </div>
        <div className="cardFooter">
          <div>
            <strong>{formatCurrency(offering.price)}</strong>
            <span> / {offering.unit}</span>
          </div>
          <span className="location"><MapPin size={15} /> {offering.neighborhood}</span>
        </div>
      </div>
    </article>
  );
}
