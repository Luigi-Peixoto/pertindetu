import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { BadgeCheck, MessageCircle, Timer } from "lucide-react";
import { api } from "../api/client";
import { LoadingState } from "../components/LoadingState";
import { OfferingCard } from "../components/OfferingCard";
import { PortfolioSection } from "../components/PortfolioSection";
import { ReviewForm } from "../components/ReviewForm";
import { ReviewList } from "../components/ReviewList";
import { Stars } from "../components/Stars";
import type { Offering, Provider } from "../types";
import { averageRating, formatRating } from "../utils/reviews";

export function ProviderProfilePage() {
  const { id } = useParams();
  const [profile, setProfile] = useState<(Provider & { offerings: Offering[] }) | null>(null);

  useEffect(() => {
    if (id) api.provider(id).then(setProfile);
  }, [id]);

  if (!profile) return <main className="pageShell"><LoadingState /></main>;

  const rating = averageRating(profile.reviews);

  return (
    <main className="profilePage">
      <section className="profileCover" style={{ backgroundImage: `url(${profile.coverUrl})` }}>
        <div className="profileHeader">
          <img src={profile.avatarUrl} alt="" />
          <div>
            <h1>{profile.name} {profile.verified && <BadgeCheck size={24} />}</h1>
            <p>{profile.headline}</p>
            <div className="ratingLine">
              <Stars value={rating} />
              <span>{formatRating(rating)} ({profile.reviews.length} avaliacoes)</span>
            </div>
          </div>
          <Link className="primaryButton" to={`/mensagens?providerId=${profile.id}`}>
            <MessageCircle size={18} />
            Conversar
          </Link>
        </div>
      </section>

      <div className="detailColumns">
        <div>
          <section className="sectionBlock">
            <p className="eyebrow">Sobre</p>
            <h2>{profile.neighborhood}, {profile.city}</h2>
            <p>{profile.bio}</p>
            <div className="tagRow">
              {profile.specialties.map((item) => <span key={item}>{item}</span>)}
            </div>
          </section>

          <section className="sectionBlock">
            <div className="sectionTitleRow">
              <div>
                <p className="eyebrow">Ofertas</p>
                <h2>Servicos e produtos</h2>
              </div>
            </div>
            <div className="offeringGrid compact">
              {profile.offerings.map((offering) => <OfferingCard key={offering.id} offering={offering} />)}
            </div>
          </section>

          <ReviewList reviews={profile.reviews} />
          <PortfolioSection
            photos={profile.portfolio}
            onUpload={async (payload) => {
              const photo = await api.uploadPortfolioPhoto(profile.id, payload);
              setProfile({ ...profile, portfolio: [photo, ...profile.portfolio] });
            }}
          />
          <ReviewForm
            onSubmit={async (payload) => {
              const updated = await api.review({ targetType: "provider", targetId: profile.id, ...payload }) as Provider;
              setProfile({ ...profile, reviews: updated.reviews });
            }}
          />
        </div>

        <aside className="stickyPanel">
          <h2>{profile.completedJobs}</h2>
          <p>atendimentos concluidos</p>
          <div className="infoLine"><Timer size={18} /> {profile.responseTime}</div>
          <div className="infoLine"><BadgeCheck size={18} /> {profile.verified ? "Perfil verificado" : "Perfil em verificacao"}</div>
        </aside>
      </div>
    </main>
  );
}
