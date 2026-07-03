import type { Review } from "../types";
import { formatRating, averageRating } from "../utils/reviews";
import { Stars } from "./Stars";

type ReviewListProps = {
  reviews: Review[];
};

export function ReviewList({ reviews }: ReviewListProps) {
  const rating = averageRating(reviews);

  return (
    <section className="sectionBlock">
      <div className="sectionTitleRow">
        <div>
          <p className="eyebrow">Avaliacoes</p>
          <h2>{reviews.length ? `${formatRating(rating)} de media` : "Ainda sem avaliacoes"}</h2>
        </div>
        <Stars value={rating} />
      </div>

      <div className="reviewList">
        {reviews.map((review) => (
          <article key={review.id} className="reviewItem">
            <div className="reviewHeader">
              <strong>{review.author}</strong>
              <Stars value={review.rating} size="sm" />
            </div>
            <p>{review.comment}</p>
            <span>{new Date(review.createdAt).toLocaleDateString("pt-BR")}</span>
          </article>
        ))}
      </div>
    </section>
  );
}
