import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { MessageCircle, PackageCheck, UserRound } from "lucide-react";
import { api } from "../api/client";
import { LoadingState } from "../components/LoadingState";
import { ReviewForm } from "../components/ReviewForm";
import { ReviewList } from "../components/ReviewList";
import { Stars } from "../components/Stars";
import type { Offering } from "../types";
import { averageRating, formatCurrency, formatRating } from "../utils/reviews";

export function OfferingDetailPage() {
  const { id } = useParams();
  const [offering, setOffering] = useState<Offering | null>(null);

  useEffect(() => {
    if (id) api.offering(id).then(setOffering);
  }, [id]);

  if (!offering) return <main className="pageShell"><LoadingState /></main>;

  const rating = averageRating(offering.reviews);

  return (
    <main className="detailLayout">
      <section className="detailHero">
        <img src={offering.imageUrl} alt="" />
        <div className="detailHeroContent">
          <span className="typePill">{offering.type === "service" ? "Servico" : "Produto"}</span>
          <h1>{offering.title}</h1>
          <div className="ratingLine">
            <Stars value={rating} />
            <span>{formatRating(rating)} ({offering.reviews.length} avaliacoes)</span>
          </div>
          <p>{offering.description}</p>
        </div>
      </section>

      <div className="detailColumns">
        <div>
          <section className="sectionBlock">
            <p className="eyebrow">Detalhes</p>
            <h2>{formatCurrency(offering.price)} / {offering.unit}</h2>
            <div className="tagRow">
              {offering.tags.map((tag) => <span key={tag}>{tag}</span>)}
            </div>
            <p className="mutedText">Disponivel em {offering.neighborhood}, {offering.city}. Agendamento e combinados podem ser feitos por mensagem.</p>
          </section>

          <ReviewList reviews={offering.reviews} />
          <ReviewForm
            onSubmit={async (payload) => {
              const updated = await api.review({ targetType: "offering", targetId: offering.id, ...payload }) as Offering;
              setOffering(updated);
            }}
          />
        </div>

        <aside className="stickyPanel">
          <img src={offering.providerAvatarUrl} alt="" className="avatarLarge" />
          <h2>{offering.providerName}</h2>
          <Link className="secondaryButton" to={`/prestadores/${offering.providerId}`}>
            <UserRound size={18} />
            Ver perfil
          </Link>
          <Link className="primaryButton" to="/mensagens">
            <MessageCircle size={18} />
            Enviar mensagem
          </Link>
          <div className="infoLine"><PackageCheck size={18} /> Oferta ativa e mockada via MSW</div>
        </aside>
      </div>
    </main>
  );
}
