export function LoadingState() {
  return <div className="stateBox">Carregando informacoes...</div>;
}

export function EmptyState({ text }: { text: string }) {
  return <div className="stateBox">{text}</div>;
}
