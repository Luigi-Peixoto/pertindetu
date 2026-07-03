import { FormEvent, useEffect, useMemo, useState } from "react";
import { Send } from "lucide-react";
import { api } from "../api/client";
import { LoadingState } from "../components/LoadingState";
import type { Conversation } from "../types";

export function MessagesPage() {
  const [conversations, setConversations] = useState<Conversation[]>([]);
  const [activeId, setActiveId] = useState<string>("");
  const [text, setText] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.conversations().then((items) => {
      setConversations(items);
      setActiveId(items[0]?.id ?? "");
    }).finally(() => setLoading(false));
  }, []);

  const active = useMemo(() => conversations.find((item) => item.id === activeId), [conversations, activeId]);

  const submit = async (event: FormEvent) => {
    event.preventDefault();
    if (!active || !text.trim()) return;

    const message = await api.sendMessage(active.id, text.trim());
    setConversations((items) =>
      items.map((conversation) =>
        conversation.id === active.id
          ? { ...conversation, messages: [...conversation.messages, message as Conversation["messages"][number]], unread: 0 }
          : conversation
      )
    );
    setText("");
  };

  if (loading) return <main className="pageShell"><LoadingState /></main>;

  return (
    <main className="messagesPage">
      <section className="conversationList">
        <div className="sectionTitleRow">
          <div>
            <p className="eyebrow">Mensagens</p>
            <h1>Conversas</h1>
          </div>
        </div>
        {conversations.map((conversation) => (
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
            </span>
            {conversation.unread > 0 && <mark>{conversation.unread}</mark>}
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
              <button className="primaryButton" type="submit" disabled={!text.trim()}>
                <Send size={18} />
                Enviar
              </button>
            </form>
          </>
        )}
      </section>
    </main>
  );
}
