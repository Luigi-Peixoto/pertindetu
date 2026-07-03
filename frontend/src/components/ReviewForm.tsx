import { FormEvent, useState } from "react";
import { Send } from "lucide-react";
import { Stars } from "./Stars";

type ReviewFormProps = {
  onSubmit: (payload: { author: string; rating: number; comment: string }) => Promise<void>;
};

export function ReviewForm({ onSubmit }: ReviewFormProps) {
  const [author, setAuthor] = useState("");
  const [comment, setComment] = useState("");
  const [rating, setRating] = useState(5);
  const [saving, setSaving] = useState(false);

  const submit = async (event: FormEvent) => {
    event.preventDefault();
    if (!comment.trim()) return;

    setSaving(true);
    await onSubmit({ author: author.trim() || "Cliente", rating, comment: comment.trim() });
    setAuthor("");
    setComment("");
    setRating(5);
    setSaving(false);
  };

  return (
    <form className="reviewForm" onSubmit={submit}>
      <div className="formHeader">
        <div>
          <p className="eyebrow">Sua experiencia</p>
          <h3>Avalie com estrelas</h3>
        </div>
        <Stars value={rating} interactive onChange={setRating} />
      </div>
      <input value={author} onChange={(event) => setAuthor(event.target.value)} placeholder="Seu nome" />
      <textarea value={comment} onChange={(event) => setComment(event.target.value)} placeholder="Conte como foi o atendimento" />
      <button className="primaryButton" type="submit" disabled={saving || !comment.trim()}>
        <Send size={18} />
        {saving ? "Enviando..." : "Enviar avaliacao"}
      </button>
    </form>
  );
}
