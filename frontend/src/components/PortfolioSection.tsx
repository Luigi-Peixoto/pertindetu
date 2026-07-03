import { ChangeEvent, FormEvent, useState } from "react";
import { ImagePlus, Upload } from "lucide-react";
import type { PortfolioPhoto } from "../types";

type PortfolioSectionProps = {
  photos: PortfolioPhoto[];
  onUpload: (payload: { title: string; imageUrl: string }) => Promise<void>;
};

const readFileAsDataUrl = (file: File) =>
  new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(String(reader.result));
    reader.onerror = () => reject(reader.error);
    reader.readAsDataURL(file);
  });

export function PortfolioSection({ photos, onUpload }: PortfolioSectionProps) {
  const [title, setTitle] = useState("");
  const [previewUrl, setPreviewUrl] = useState("");
  const [saving, setSaving] = useState(false);

  const selectFile = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setPreviewUrl(await readFileAsDataUrl(file));
    setTitle((current) => current || file.name.replace(/\.[^.]+$/, ""));
  };

  const submit = async (event: FormEvent) => {
    event.preventDefault();
    if (!previewUrl) return;

    setSaving(true);
    await onUpload({ title: title.trim() || "Foto do portfolio", imageUrl: previewUrl });
    setTitle("");
    setPreviewUrl("");
    setSaving(false);
  };

  return (
    <section className="sectionBlock">
      <div className="sectionTitleRow">
        <div>
          <p className="eyebrow">Portfolio</p>
          <h2>Fotos de trabalhos</h2>
        </div>
      </div>

      <form className="portfolioUpload" onSubmit={submit}>
        <label className="uploadDropzone">
          {previewUrl ? <img src={previewUrl} alt="" /> : <ImagePlus size={28} />}
          <span>{previewUrl ? "Trocar foto" : "Selecionar foto"}</span>
          <input type="file" accept="image/*" onChange={selectFile} />
        </label>
        <input value={title} onChange={(event) => setTitle(event.target.value)} placeholder="Titulo da foto" />
        <button className="primaryButton" type="submit" disabled={!previewUrl || saving}>
          <Upload size={18} />
          {saving ? "Enviando..." : "Adicionar ao portfolio"}
        </button>
      </form>

      <div className="portfolioGrid">
        {photos.map((photo) => (
          <article key={photo.id} className="portfolioPhoto">
            <img src={photo.imageUrl} alt="" />
            <div>
              <strong>{photo.title}</strong>
              <span>{new Date(photo.uploadedAt).toLocaleDateString("pt-BR")}</span>
            </div>
          </article>
        ))}
      </div>
    </section>
  );
}
