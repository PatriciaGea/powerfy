# Powerfy 🔌

Powerfy is a native Android e-commerce app for electronics, built as an MVP with Jetpack Compose and modern Android architecture. It's a portfolio project demonstrating a full design-to-code workflow, from Figma to a working, API-driven app.

<!-- Add a screenshot or GIF of the app here once you have one -->
<!-- ![Powerfy screenshot](docs/screenshot.png) -->

## Overview

Powerfy lets users browse electronics (smartphones, laptops, tablets, and mobile accessories), view product details, manage favorites, and go through a full cart → checkout → payment → confirmation flow.

- **Platform:** Android (min SDK 26 / Android 8.0)
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Product data:** [DummyJSON](https://dummyjson.com/) (public REST API, used for product catalog and images only)
- **Auth & user data:** Firebase Authentication + Cloud Firestore (real accounts, real user data)
- **Payments:** Stripe (test mode — real SDK integration, no real money moves)
- **Status:** MVP in active development

## Screens

| # | Screen | Status |
|---|--------|--------|
| 00 | Splash | ✅ Implemented |
| 00b | Intro | 🔜 Planned |
| 01 | Login | 🔜 Planned |
| 02 | Sign In | 🔜 Planned |
| 03 | Home | 🔜 Planned |
| 04 | Product Detail | 🔜 Planned |
| 05 | Favorites | 🔜 Planned |
| 06 | Cart | 🔜 Planned |
| 07 | Checkout | 🔜 Planned |
| 08 | Payment | 🔜 Planned |
| 09 | Confirmation | 🔜 Planned |

## Architecture

The app follows **MVVM** with a clean separation between data, domain, and UI layers:

```
com.patriciagea.powerfy/
├── data/
│   ├── remote/          # Retrofit API interfaces + DTOs (DummyJSON — products only)
│   ├── firebase/        # Firebase Auth + Firestore data sources (users, orders)
│   ├── payment/         # Stripe SDK wrapper (PaymentSheet integration)
│   └── repository/      # Repository implementations
├── domain/
│   ├── model/            # Clean domain models (no DTOs leaking into UI)
│   └── repository/       # Repository interfaces (ProductRepository, AuthRepository, PaymentRepository)
├── di/                    # Hilt modules (network, Firebase, Stripe, repositories)
├── navigation/            # Navigation Compose graph and routes
└── ui/
    ├── theme/             # Design tokens (color, type, shape, spacing)
    ├── components/        # Reusable composables (ProductCard, TopBar, etc.)
    └── <feature>/          # One package per screen (splash, home, cart, ...)
```

**Why this structure:** it keeps networking and business logic independent of Compose, makes each screen testable in isolation, and mirrors the layering expected in professional Android codebases. Product data (DummyJSON), user/auth data (Firebase), and payments (Stripe) are three separate data sources, each behind its own repository interface — so, for example, swapping DummyJSON for a real product backend later wouldn't touch the auth or payment code at all.

## Tech stack

| Purpose | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Dependency injection | Hilt |
| Networking (products) | Retrofit + kotlinx.serialization |
| Auth | Firebase Authentication |
| User / order data | Cloud Firestore |
| Payments | Stripe Android SDK (`PaymentSheet`), **test mode** |
| Image loading | Coil |
| Async | Kotlin Coroutines + Flow |
| Local persistence (cart, favorites) | Room *(planned)* |
| Testing | JUnit + Compose UI Testing *(planned)* |

### A note on login and payment

Login/sign-up and payment are **real integrations**, not mocked:

- **Login / Sign in** use Firebase Authentication (email/password). User profile data (name, addresses, order history) is stored in Cloud Firestore.
- **Payment** uses the real Stripe Android SDK, but Stripe's **test mode** (test API keys, test card numbers) — the integration, validation, and error handling all behave exactly like production, but no real card is charged and no real money moves. This is standard practice for demoing a payment flow safely.
- **Product catalog only** comes from DummyJSON, since it's just for demo images and product info — everything about the user and the transaction is real infrastructure.

Because of this, running the project requires your own Firebase project (`google-services.json`) and a Stripe test-mode publishable key — see [Getting started](#getting-started).

## Design system

Design tokens are defined in `ui/theme/` and sourced directly from the Figma file ("Powerfy - Rebuilt (Dev Ready)"):

| Token | Value |
|---|---|
| Primary | `#159AD3` |
| Background | `#FFFFFF` |
| Surface / cards | `#F5F7FA` |
| Secondary text | `#7A7A7A` |
| Border | `#E5E8EC` |
| Font | Roboto (Regular / Medium / Bold) |
| Button radius | 12dp |
| Spacing scale | 4 / 8 / 16 / 24 / 32 dp |

## Data sources

### Products — DummyJSON
Product catalog and images only. [DummyJSON](https://dummyjson.com/) is a free REST API for prototyping:

```
GET /products                          # all products
GET /products/category/smartphones     # smartphones
GET /products/category/laptops         # laptops
GET /products/category/tablets         # tablets
GET /products/category/mobile-accessories  # accessories
GET /products/search?q={query}         # search
GET /products/{id}                     # product details
```

> **Note:** DummyJSON is a test/prototyping API. Write operations (create, update, delete) are simulated and not actually persisted server-side — irrelevant here since Powerfy only ever reads product data from it.

### Users & orders — Firebase
- **Authentication:** email/password sign-up and login via Firebase Auth.
- **Firestore collections:** `users/{uid}` (profile, addresses), `orders/{orderId}` (order history, linked to a user).

### Payments — Stripe (test mode)
Checkout uses Stripe's `PaymentSheet` with a **test-mode** publishable key. Test card numbers (e.g. `4242 4242 4242 4242`) simulate successful and failed payments without moving real money.

## Getting started

### Prerequisites
- Android Studio (latest stable)
- JDK 17
- An Android device or emulator running API 26+
- A [Firebase project](https://console.firebase.google.com/) with Authentication (email/password) and Firestore enabled
- A [Stripe](https://dashboard.stripe.com/) account with **test-mode** keys

### Setup
1. Clone the repo:
   ```bash
   git clone https://github.com/PatriciaGea/Powerfy.git
   ```
2. Add your Firebase config: download `google-services.json` from your Firebase project (Project settings → Your apps → Android) and place it in `app/`.
3. Add your Stripe **test** publishable key to `local.properties` (not committed to git):
   ```
   STRIPE_PUBLISHABLE_KEY=pk_test_xxxxxxxxxxxx
   ```
4. Open the project in Android Studio and let Gradle sync.
5. Run the app on an emulator or physical device (`Shift + F10`).

> DummyJSON needs no key or config — it's a public, unauthenticated API used only for product data.

## Roadmap

- [ ] Implement remaining 10 screens
- [ ] Firebase Authentication (email/password sign up + login)
- [ ] Firestore: user profiles and order history
- [ ] Stripe `PaymentSheet` integration (test mode)
- [ ] Persist cart/favorites locally (Room)
- [ ] Unit tests for ViewModels and repositories
- [ ] UI tests for critical flows (checkout)
- [ ] Publish to Google Play (portfolio release)

## Author

**Patricia Gea** — Frontend/Android developer transitioning into tech, currently studying at Hyper Island (Stockholm).
[GitHub](https://github.com/PatriciaGea)

## License

This project is for portfolio purposes. Product data is provided by DummyJSON and does not represent a real store.