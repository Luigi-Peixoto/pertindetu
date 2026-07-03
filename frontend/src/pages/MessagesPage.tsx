import { FormEvent, useEffect, useMemo, useState } from "react";
import { Archive, Clock, Search, Send } from "lucide-react";
import { useSearchParams } from "react-router-dom";
import { api } from "../api/client";
import { LoadingState } from "../components/LoadingState";
import type { Conversation } from "../types";

export function MessagesPage() {
  const [searchParams] = useSearchParams();
  const [conversations, setConversations] = useState<Conversation[]>([]);
  const [activeId, setActiveId] = useState<string>("");
  const [text, setText] = useState("");
  const [historyQuery, setHistoryQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<"all" | "active" | "archived">("all");
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);

  useEffect(() => {
    const providerId = searchParams.get("providerId");
    const offeringId = searchParams.get("offeringId") ?? undefined;

    async function load() {
      const items = await api.conversations();

      if (providerId) {
        const conversation = await api.startConversation({ providerId, offeringId });
        const merged = [conversation, ...items.filter((item) => item.id !== conversation.id)]
          .sort((a, b) => b.updatedAt.localeCompare(a.updatedAt));
        setConversations(merged);
        setActiveId(conversation.id);
        setLoading(false);
        return;
      }

      setConversations(items);
      setActiveId(items[0]?.id ?? "");
      setLoading(false);
    }

    load();
  }, [searchParams]);

  const active = useMemo(() => conversations.find((item) => item.id === activeId), [conversations, activeId]);
  const visibleConversations = useMemo(() => {
    const query = historyQuery.trim().toLowerCase();

    return conversations.filter((conversation) => {
      const statusMatch = statusFilter === "all" || conversation.status === statusFilter;
      const queryMatch =
        !query ||
        [conversation.providerName, conversation.offeringTitle ?? "", ...conversation.messages.map((message) => message.text)]
          .some((value) => value.toLowerCase().includes(query));

      return statusMatch && queryMatch;
    });
  }, [conversations, historyQuery, statusFilter]);

  const submit = async (event: FormEvent) => {
    event.preventDefault();
    if (!active || !text.trim()) return;

    setSending(true);
    const updated = await api.sendMessage(active.id, text.trim());
    setConversations((items) =>
      items.map((conversation) => conversation.id === active.id ? updated : conversation)
        .sort((a, b) => b.updatedAt.localeCompare(a.updatedAt))
    );
    setText("");
    setSending(false);
  };

  if (loading) return <main className="pageShell"><LoadingState /></main>;

  return (
    <main className="messagesPage">
      <section className="conversationList">
        <div className="sectionTitleRow">
          <div>
            <p className="eyebrow">Historico</p>
            <h1>Conversas</h1>
          </div>
        </div>

        <label className="historySearch">
          <Search size={17} />
          <input value={historyQuery} onChange={(event) => setHistoryQuery(event.target.value)} placeholder="Buscar no historico" />
        </label>

        <div className="segmented historyFilter">
          {[
            ["all", "Todas"],
            ["active", "Ativas"],
            ["archived", "Arquivadas"]
          ].map(([value, label]) => (
            <button
              key={value}
              className={statusFilter === value ? "active" : ""}
              onClick={() => setStatusFilter(value as "all" | "active" | "archived")}
              type="button"
            >
              {label}
            </button>
          ))}
        </div>

        {visibleConversations.map((conversation) => (
          <button
            key={conversation.id}
            className={conversation.id === activeId ? "conversationItem active" : "conversationItem"}
            type="button"
            onClick={() => setActiveId(conversation.id)}
          >
            <img src={conversation.providerAvatarUrl} alt="" />
            <span>
              <strong>{conversation.providerName}</strong>
              <small>{conversation.offeringTitle}</small>
              <small>{conversation.messages[conversation.messages.length - 1]?.text}</small>
            </span>
            <span className="conversationMeta">
              {conversation.status === "archived" && <Archive size={14} />}
              <small>{new Date(conversation.updatedAt).toLocaleDateString("pt-BR")}</small>
              {conversation.unread > 0 && <mark>{conversation.unread}</mark>}
            </span>
          </button>
        ))}
      </section>

      <section className="chatPanel">
        {active && (
          <>
            <header className="chatHeader">
              <img src={active.providerAvatarUrl} alt="" />
              <div>
                <h2>{active.providerName}</h2>
                <p>{active.offeringTitle}</p>
                <span><Clock size={14} /> Atualizada em {new Date(active.updatedAt).toLocaleString("pt-BR")}</span>
              </div>
            </header>

            <div className="messageStream">
              {active.messages.map((message) => (
                <div key={message.id} className={message.sender === "client" ? "messageBubble mine" : "messageBubble"}>
                  <p>{message.text}</p>
                  <span>{new Date(message.createdAt).toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" })}</span>
                </div>
              ))}
            </div>

            <form className="messageComposer" onSubmit={submit}>
              <input value={text} onChange={(event) => setText(event.target.value)} placeholder="Escreva uma mensagem" />
              <button className="primaryButton" type="submit" disabled={!text.trim() || sending}>
                <Send size={18} />
                {sending ? "Enviando..." : "Enviar"}
              </button>
            </form>
          </>
        )}
      </section>
    </main>
  );
}
