import { Star } from "lucide-react";

type StarsProps = {
  value: number;
  size?: "sm" | "md";
  interactive?: boolean;
  onChange?: (value: number) => void;
};

export function Stars({ value, size = "md", interactive = false, onChange }: StarsProps) {
  const iconSize = size === "sm" ? 16 : 20;

  return (
    <div className="stars" aria-label={`${value} de 5 estrelas`}>
      {[1, 2, 3, 4, 5].map((star) => {
        const filled = star <= Math.round(value);
        const icon = <Star size={iconSize} fill={filled ? "currentColor" : "none"} />;

        if (!interactive) {
          return <span key={star} className={filled ? "star starFilled" : "star"}>{icon}</span>;
        }

        return (
          <button
            key={star}
            type="button"
            className={filled ? "starButton starFilled" : "starButton"}
            onClick={() => onChange?.(star)}
            aria-label={`Avaliar com ${star} estrela${star > 1 ? "s" : ""}`}
          >
            {icon}
          </button>
        );
      })}
    </div>
  );
}
