import { NavLink, Route, Routes } from "react-router-dom";
import { MessageCircle, Search } from "lucide-react";
import { SearchPage } from "./pages/SearchPage";
import { OfferingDetailPage } from "./pages/OfferingDetailPage";
import { ProviderProfilePage } from "./pages/ProviderProfilePage";
import { MessagesPage } from "./pages/MessagesPage";

export function App() {
  return (
    <>
      <header className="appHeader">
        <NavLink to="/" className="brand">PertinDetu</NavLink>
        <nav>
          <NavLink to="/"><Search size={18} /> Buscar</NavLink>
          <NavLink to="/mensagens"><MessageCircle size={18} /> Mensagens</NavLink>
        </nav>
      </header>

      <Routes>
        <Route path="/" element={<SearchPage />} />
        <Route path="/ofertas/:id" element={<OfferingDetailPage />} />
        <Route path="/prestadores/:id" element={<ProviderProfilePage />} />
        <Route path="/mensagens" element={<MessagesPage />} />
      </Routes>
    </>
  );
}
