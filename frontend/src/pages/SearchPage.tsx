import { FormEvent, useEffect, useMemo, useState } from "react";
import { ArrowRight, Filter, Search, SlidersHorizontal } from "lucide-react";
import { api } from "../api/client";
import { EmptyState, LoadingState } from "../components/LoadingState";
import { OfferingCard } from "../components/OfferingCard";
import { ProviderMiniCard } from "../components/ProviderMiniCard";
import type { SearchResponse } from "../types";

const suggestions = [
  { label: "Maquiagem", query: "maquiagem", type: "service", category: "Beleza" },
  { label: "Eletrica", query: "eletrica", type: "service", category: "Manutencao" },
  { label: "Brownie", query: "brownie", type: "product", category: "Alimentos" },
  { label: "Domicilio", query: "domicilio", type: "service", category: "all" },
  { label: "Ponta Negra", query: "Ponta Negra", type: "all", category: "all" }
];

export function SearchPage() {
  const [query, setQuery] = useState("");
  const [submittedQuery, setSubmittedQuery] = useState("");
  const [type, setType] = useState("all");
  const [category, setCategory] = useState("all");
  const [sort, setSort] = useState("relevance");
  const [data, setData] = useState<SearchResponse | null>(null);
  const [loading, setLoading] = useState(true);

  const params = useMemo(() => {
    const next = new URLSearchParams();
    next.set("q", submittedQuery);
    next.set("type", type);
    next.set("category", category);
    next.set("sort", sort);
    return next;
  }, [submittedQuery, type, category, sort]);

  useEffect(() => {
    setLoading(true);
    api.search(params).then(setData).finally(() => setLoading(false));
  }, [params]);

  const submit = (event: FormEvent) => {
    event.preventDefault();
    setSubmittedQuery(query.trim());
  };

  const applyQuickFilter = (suggestion: (typeof suggestions)[number]) => {
    setQuery(suggestion.query);
    setSubmittedQuery(suggestion.query);
    setType(suggestion.type);
    setCategory(suggestion.category);
    setSort("relevance");
  };

  const categories = ["all", "Beleza", "Manutencao", "Alimentos"];

  return (
    <main className="pageShell">
      <section className="searchPanel">
        <div>
          <p className="eyebrow">PertinDetu</p>
          <h1>Encontre servicos e produtos perto de voce</h1>
        </div>

        <form className="searchBar" onSubmit={submit}>
          <Search size={20} />
          <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Busque por servico, produto, bairro ou prestador" />
          <button className="searchButton" type="submit">
            Buscar
            <ArrowRight size={18} />
          </button>
        </form>

        <div className="suggestionRow">
          {suggestions.map((item) => (
            <button
              key={item.label}
              className={submittedQuery === item.query && type === item.type && category === item.category ? "active" : ""}
              type="button"
              onClick={() => applyQuickFilter(item)}
            >
              {item.label}
            </button>
          ))}
        </div>
      </section>

      <section className="filtersBar" aria-label="Filtros de busca">
        <div className="segmented">
          {[
            ["all", "Tudo"],
            ["service", "Servicos"],
            ["product", "Produtos"]
          ].map(([value, label]) => (
            <button key={value} className={type === value ? "active" : ""} onClick={() => setType(value)} type="button">
              {label}
            </button>
          ))}
        </div>

        <label>
          <Filter size={16} />
          <select value={category} onChange={(event) => setCategory(event.target.value)}>
            {categories.map((item) => (
              <option key={item} value={item}>{item === "all" ? "Todas categorias" : item}</option>
            ))}
          </select>
        </label>

        <label>
          <SlidersHorizontal size={16} />
          <select value={sort} onChange={(event) => setSort(event.target.value)}>
            <option value="relevance">Mais relevantes</option>
            <option value="rating">Melhor avaliados</option>
            <option value="price">Menor preco</option>
          </select>
        </label>
      </section>

      {loading && <LoadingState />}

      {!loading && data && (
        <div className="resultsGrid">
          <section>
            <div className="sectionTitleRow">
              <div>
                <p className="eyebrow">Resultados</p>
                <h2>{data.offerings.length} ofertas encontradas</h2>
              </div>
            </div>
            <div className="offeringGrid">
              {data.offerings.map((offering) => <OfferingCard key={offering.id} offering={offering} />)}
            </div>
            {!data.offerings.length && <EmptyState text="Nenhuma oferta encontrada com esses filtros." />}
          </section>

          <aside className="providersAside">
            <div className="sectionTitleRow">
              <div>
                <p className="eyebrow">Prestadores</p>
                <h2>Perfis relacionados</h2>
              </div>
            </div>
            <div className="providerList">
              {data.providers.map((provider) => <ProviderMiniCard key={provider.id} provider={provider} />)}
            </div>
          </aside>
        </div>
      )}
    </main>
  );
}
