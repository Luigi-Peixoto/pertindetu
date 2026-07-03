import type { Conversation, Offering, Provider } from "../types";

export const providers: Provider[] = [
  {
    id: "p-ana",
    name: "Ana Beatriz",
    headline: "Designer de sobrancelhas e maquiagem social",
    avatarUrl: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=240&q=80",
    coverUrl: "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=1400&q=80",
    city: "Natal",
    neighborhood: "Ponta Negra",
    verified: true,
    responseTime: "responde em ate 20 min",
    completedJobs: 128,
    bio: "Atendimento com hora marcada, materiais esterilizados e foco em realcar a beleza natural de cada cliente.",
    specialties: ["sobrancelhas", "maquiagem", "eventos", "noivas"],
    portfolio: [
      {
        id: "pf-ana-1",
        title: "Make para formatura",
        imageUrl: "https://images.unsplash.com/photo-1522338242992-e1a54906a8da?auto=format&fit=crop&w=900&q=80",
        uploadedAt: "2026-06-20"
      },
      {
        id: "pf-ana-2",
        title: "Sobrancelha natural",
        imageUrl: "https://images.unsplash.com/photo-1487412947147-5cebf100ffc2?auto=format&fit=crop&w=900&q=80",
        uploadedAt: "2026-06-11"
      }
    ],
    reviews: [
      { id: "r-1", author: "Marina", rating: 5, comment: "Atendimento pontual e resultado impecavel.", createdAt: "2026-06-18" },
      { id: "r-2", author: "Camila", rating: 4, comment: "Gostei muito do cuidado e da explicacao.", createdAt: "2026-05-30" }
    ]
  },
  {
    id: "p-joao",
    name: "Joao Miguel",
    headline: "Manutencao residencial e eletrica",
    avatarUrl: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=240&q=80",
    coverUrl: "https://images.unsplash.com/photo-1621905252507-b35492cc74b4?auto=format&fit=crop&w=1400&q=80",
    city: "Parnamirim",
    neighborhood: "Nova Parnamirim",
    verified: true,
    responseTime: "responde em ate 1 h",
    completedJobs: 214,
    bio: "Servicos rapidos para pequenos reparos, instalacoes eletricas e manutencao preventiva em casas e apartamentos.",
    specialties: ["eletrica", "chuveiro", "tomadas", "manutencao"],
    portfolio: [
      {
        id: "pf-joao-1",
        title: "Quadro revisado",
        imageUrl: "https://images.unsplash.com/photo-1621905252507-b35492cc74b4?auto=format&fit=crop&w=900&q=80",
        uploadedAt: "2026-06-14"
      },
      {
        id: "pf-joao-2",
        title: "Luminaria instalada",
        imageUrl: "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?auto=format&fit=crop&w=900&q=80",
        uploadedAt: "2026-05-28"
      }
    ],
    reviews: [
      { id: "r-3", author: "Rafael", rating: 5, comment: "Resolveu a instalacao no mesmo dia.", createdAt: "2026-06-02" },
      { id: "r-4", author: "Lorena", rating: 5, comment: "Muito organizado e transparente no preco.", createdAt: "2026-04-21" }
    ]
  },
  {
    id: "p-luiza",
    name: "Luiza Castro",
    headline: "Doces artesanais sob encomenda",
    avatarUrl: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=240&q=80",
    coverUrl: "https://images.unsplash.com/photo-1486427944299-d1955d23e34d?auto=format&fit=crop&w=1400&q=80",
    city: "Natal",
    neighborhood: "Tirol",
    verified: false,
    responseTime: "responde no mesmo dia",
    completedJobs: 76,
    bio: "Bolos, brownies e caixas de doces feitos com ingredientes frescos para presentes, festas e eventos pequenos.",
    specialties: ["bolos", "brownies", "presentes", "festas"],
    portfolio: [
      {
        id: "pf-luiza-1",
        title: "Caixa presente",
        imageUrl: "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=900&q=80",
        uploadedAt: "2026-06-22"
      },
      {
        id: "pf-luiza-2",
        title: "Mesa de doces",
        imageUrl: "https://images.unsplash.com/photo-1486427944299-d1955d23e34d?auto=format&fit=crop&w=900&q=80",
        uploadedAt: "2026-06-07"
      }
    ],
    reviews: [
      { id: "r-5", author: "Patricia", rating: 5, comment: "O brownie chegou lindo e muito saboroso.", createdAt: "2026-06-12" }
    ]
  }
];

export const offerings: Offering[] = [
  {
    id: "o-make",
    type: "service",
    title: "Maquiagem social em domicilio",
    description: "Preparacao de pele, olhos e acabamento para formaturas, casamentos, aniversarios e fotos profissionais.",
    price: 120,
    unit: "servico",
    imageUrl: "https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=1000&q=80",
    category: "Beleza",
    tags: ["maquiagem", "domicilio", "eventos"],
    providerId: "p-ana",
    providerName: "Ana Beatriz",
    providerAvatarUrl: providers[0].avatarUrl,
    city: "Natal",
    neighborhood: "Ponta Negra",
    available: true,
    reviews: [
      { id: "r-6", author: "Helena", rating: 5, comment: "Durou a festa inteira.", createdAt: "2026-06-22" },
      { id: "r-7", author: "Sofia", rating: 4, comment: "Resultado lindo e natural.", createdAt: "2026-06-08" }
    ]
  },
  {
    id: "o-eletrica",
    type: "service",
    title: "Instalacao de tomadas e luminarias",
    description: "Instalacao, troca e revisao de pontos eletricos residenciais com materiais indicados antes da visita.",
    price: 90,
    unit: "visita",
    imageUrl: "https://images.unsplash.com/photo-1581092160562-40aa08e78837?auto=format&fit=crop&w=1000&q=80",
    category: "Manutencao",
    tags: ["eletrica", "tomadas", "luminarias"],
    providerId: "p-joao",
    providerName: "Joao Miguel",
    providerAvatarUrl: providers[1].avatarUrl,
    city: "Parnamirim",
    neighborhood: "Nova Parnamirim",
    available: true,
    reviews: [
      { id: "r-8", author: "Igor", rating: 5, comment: "Servico limpo e rapido.", createdAt: "2026-05-26" }
    ]
  },
  {
    id: "o-brownie",
    type: "product",
    title: "Caixa com 12 brownies artesanais",
    description: "Brownies de chocolate meio amargo com opcoes de recheio de doce de leite, ninho e brigadeiro.",
    price: 58,
    unit: "caixa",
    imageUrl: "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=1000&q=80",
    category: "Alimentos",
    tags: ["brownie", "doces", "presente"],
    providerId: "p-luiza",
    providerName: "Luiza Castro",
    providerAvatarUrl: providers[2].avatarUrl,
    city: "Natal",
    neighborhood: "Tirol",
    available: true,
    reviews: [
      { id: "r-9", author: "Bia", rating: 5, comment: "Embalagem caprichada e sabor perfeito.", createdAt: "2026-06-01" }
    ]
  }
];

export const conversations: Conversation[] = [
  {
    id: "c-ana",
    providerId: "p-ana",
    providerName: "Ana Beatriz",
    providerAvatarUrl: providers[0].avatarUrl,
    offeringId: "o-make",
    offeringTitle: "Maquiagem social em domicilio",
    updatedAt: "2026-07-02T13:22:00",
    status: "active",
    unread: 1,
    messages: [
      { id: "m-1", sender: "client", text: "Oi, voce tem horario para sabado a tarde?", createdAt: "2026-07-02T13:15:00" },
      { id: "m-2", sender: "provider", text: "Tenho sim. Seria maquiagem para qual evento?", createdAt: "2026-07-02T13:22:00" }
    ]
  },
  {
    id: "c-joao",
    providerId: "p-joao",
    providerName: "Joao Miguel",
    providerAvatarUrl: providers[1].avatarUrl,
    offeringId: "o-eletrica",
    offeringTitle: "Instalacao de tomadas e luminarias",
    updatedAt: "2026-07-01T09:18:00",
    status: "active",
    unread: 0,
    messages: [
      { id: "m-3", sender: "client", text: "Preciso trocar duas tomadas no apartamento.", createdAt: "2026-07-01T09:10:00" },
      { id: "m-4", sender: "provider", text: "Consigo avaliar hoje no fim da tarde.", createdAt: "2026-07-01T09:18:00" }
    ]
  },
  {
    id: "c-luiza",
    providerId: "p-luiza",
    providerName: "Luiza Castro",
    providerAvatarUrl: providers[2].avatarUrl,
    offeringId: "o-brownie",
    offeringTitle: "Caixa com 12 brownies artesanais",
    updatedAt: "2026-06-29T17:42:00",
    status: "archived",
    unread: 0,
    messages: [
      { id: "m-5", sender: "client", text: "A caixa com 12 brownies ainda esta disponivel?", createdAt: "2026-06-29T17:35:00" },
      { id: "m-6", sender: "provider", text: "Esta sim. Posso entregar amanha no Tirol.", createdAt: "2026-06-29T17:42:00" }
    ]
  }
];
