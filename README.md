# Powerfy 🔌

A native Android e-commerce app for electronics, designed in Figma and built from scratch in Kotlin with Jetpack Compose, with a real authentication, database, and payment backend behind it. 

Built as a portfolio to demonstrate a complete, production-shaped mobile android app.

**The concept, name, branding, and UI/UX design are original work by the author**, designed in Figma before any code was written.

##  Screenshots

<!--
Organize screenshots by flow, in this order. Suggested folder: docs/screenshots/
Recommended: 2-3 phone-width images per row using an HTML table or side-by-side markdown.
-->

**Onboarding — Splash, Intro, Login, Sign Up**
<!-- ![Splash](docs/screenshots/splash.png) ![Intro](docs/screenshots/intro.png) ![Login](docs/screenshots/login.png) ![Sign Up](docs/screenshots/signup.png) -->

**Shopping — Home, Product Detail, Favorites, Cart**
<!-- ![Home](docs/screenshots/home.png) ![Product Detail](docs/screenshots/product-detail.png) ![Favorites](docs/screenshots/favorites.png) ![Cart](docs/screenshots/cart.png) -->

**Checkout — Checkout, Stripe Payment Sheet, Confirmation**
<!-- ![Checkout](docs/screenshots/checkout.png) ![Stripe Payment](docs/screenshots/payment-sheet.png) ![Confirmation](docs/screenshots/confirmation.png) -->

**Full flow (GIF)**
<!-- ![Powerfy demo](docs/screenshots/demo.gif) -->

##  Objective

Powerfy demonstrates my ability to design and develop a professional Android application using industry relevant technologies and practices.

The project translates a Figma design into a functional application with Kotlin and Jetpack Compose, implementing Firebase Authentication, local data persistence, backend integration, and Stripe test mode payments.

It is designed as a portfolio project to demonstrate the ability to build complete application that can be applied to real world products.

## Overview

Powerfy lets users browse electronics (smartphones, laptops, tablets, and mobile accessories) pulled live from a public product API, view product details, favorite and add items to a persistent cart, and complete a full checkout → payment → confirmation flow with a real Stripe integration in test mode.

- **Platform:** Android (min SDK 26 / Android 8.0)
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Product data:** [DummyJSON](https://dummyjson.com/) (public REST API, product catalog and images)
- **Auth & user data:** Firebase Authentication (email/password, Google Sign-In, anonymous/guest) + Cloud Firestore
- **Local persistence:** Room (favorites, cart — survives app restarts)
- **Payments:** Stripe, test mode, via a dedicated Cloud Functions backend (see Payments backend, below)
- **Status:** All 11 screens implemented, full user flow working end to end

## 📱 Screens

| # | Screen | What it does |
|---|--------|---------------|
| 00 | Splash | Checks for an existing session and routes to Home or Intro |
| 00b | Intro | Entry point — Log in, Sign up, or Continue as Guest |
| 01 | Login | Email/password + Google Sign-In, both backed by Firebase Auth |
| 02 | Sign Up | Account creation (Firebase Auth + Firestore profile), also supports Google |
| 03 | Home | Product grid from a live API, category ads carousel, search bar |
| 04 | Product Detail | Full product info, "frequently viewed" from the same category |
| 05 | Favorites | Persisted locally with Room, reflected live across the app |
| 06 | Cart | Persisted locally with Room, quantity stepper, running total |
| 07 | Checkout | Delivery method selection, VAT calculation, order summary |
| 08 | Payment | Stripe's native `PaymentSheet` — a real payment integration in test mode |
| 09 | Confirmation | Order confirmation screen after a successful payment |
| — | Profile | Logged-in user info + logout |

## Architecture

The app follows **MVVM** with a clean separation between data, domain, and UI layers:

```
se.tattooink.powerfy/
├── data/
│   ├── remote/          # Retrofit API interfaces + DTOs (DummyJSON — products only)
│   ├── firebase/        # Firebase Auth + Firestore data sources (users)
│   ├── local/            # Room database, DAOs, entities, and hand-written migrations
│   └── repository/      # Repository implementations
├── domain/
│   ├── model/            # Clean domain models (no DTOs leaking into UI)
│   └── repository/       # Repository interfaces (Product, Auth, Cart, Favorite)
├── di/                    # Hilt modules (network, Firebase, Room, Stripe)
├── navigation/            # Navigation Compose graph, routes, and the app-level Scaffold
└── ui/
    ├── theme/             # Design tokens (color, type, shape, spacing) — sourced from Figma
    ├── components/        # Reusable composables (ProductCard, TopBar, PrimaryButton, ...)
    └── <feature>/          # One package per screen (splash, home, cart, checkout, ...)
```

**Why this structure:** it keeps networking and business logic independent of Compose, makes each screen testable in isolation, and mirrors the layering expected in professional Android codebases. Product data (DummyJSON), user/auth data (Firebase), local persistence (Room), and payments (Stripe) are four separate data sources, each behind its own repository interface — swapping any one of them later wouldn't touch the others.

**A pattern worth calling out:** the `TopBar` (logo, favorites icon, cart icon with a live item-count badge, profile icon) is declared **once**, at the `Scaffold` level in the navigation graph — not duplicated per screen. Each screen only exposes plain `() -> Unit` callbacks; navigation and shared UI state (login status, cart count) are resolved centrally. This keeps the `TopBar` reusable and fully decoupled from navigation, and means adding a new screen that needs it costs one line, not a rebuild of the component.

## Tech stack

| Purpose | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose (single `Scaffold`, centralized top bar) |
| Dependency injection | Hilt |
| Networking (products) | Retrofit + kotlinx.serialization |
| Auth | Firebase Authentication (email/password, Google, anonymous) |
| User data | Cloud Firestore |
| Local persistence | Room, with hand-written `Migration`s (see below) |
| Payments | Stripe Android SDK (`PaymentSheet`) + a dedicated Firebase Cloud Functions backend |
| Image loading | Coil |
| Async | Kotlin Coroutines + Flow |
| Testing | JUnit + Compose UI Testing *(planned)* |

### A note on login and payment

Login/sign-up and payment are **real integrations**, not mocked:

- **Login / Sign up** use Firebase Authentication — email/password, Google Sign-In (via Credential Manager), and an anonymous guest mode. User profile data is stored in Cloud Firestore.
- **Payment** uses the real Stripe Android SDK (`PaymentSheet`) talking to a real backend (see below), in Stripe's **test mode** — the integration, validation, and error handling behave exactly like production, but no real card is charged. This is standard practice for demoing a payment flow safely.
- **Product catalog only** comes from DummyJSON — everything about the user, their cart, and the transaction is real infrastructure the author built and owns.

Because of this, running the project requires a Firebase project (`google-services.json`), a Stripe test-mode publishable key, and the companion backend deployed 

## 💳 Payments backend

Stripe requires the actual charge (`PaymentIntent`) to be created **server-side**, with a secret API key that must never exist inside an Android app — if it did, anyone could decompile the APK and extract it. So Powerfy ships with a small, dedicated backend: **[powerfy-backend](https://github.com/PatriciaGea/powerfy-backend)**, a Firebase Cloud Function (**TypeScript**) that creates the Stripe `PaymentIntent` and returns only the temporary `client_secret` the app needs to open Stripe's payment sheet.

Security measures in that backend:
- **Requires a valid, logged-in Firebase user.** The function checks `request.auth` before doing anything — an unauthenticated request is rejected before it ever reaches Stripe, which also protects against anyone spamming the endpoint to run up API usage.
- **The Stripe secret key lives in Google Cloud Secret Manager**, injected into the function at runtime — it's never committed to source control or bundled into the app.
- **A billing spend cap is configured** on the Firebase project (Blaze plan), as a safety net against unexpected usage costs while the project is in development.

## Design system

Design tokens are defined in `ui/theme/` and sourced directly from the Figma file ("Powerfy - Rebuilt (Dev Ready)"), which the author designed from scratch:
| Token | Value |
|---|---|
| Primary | <span style="color:#159AD3">■</span> `#159AD3` |
| Accent | <span style="color:#FF8026">■</span> `#FF8026` |
| Background | <span style="color:#FFFFFF">■</span> `#FFFFFF` |
| Surface / cards | <span style="color:#F5F7FA">■</span> `#F5F7FA` |
| Secondary text | <span style="color:#7A7A7A">■</span> `#7A7A7A` |
| Border | <span style="color:#E5E8EC">■</span> `#E5E8EC` |

## Local storage: Room, with real migrations

Favorites and cart items are persisted locally with Room. Schema changes use **real, hand-written `Migration` objects** (see `data/local/Migrations.kt`) rather than `fallbackToDestructiveMigration()`.

This is a deliberate choice, not an oversight: `fallbackToDestructiveMigration()` is a common — and perfectly reasonable — shortcut *during active development*, when the schema is still changing often and no real user data exists yet to protect. It simply wipes and recreates the database on any version bump, which is fast to iterate with but **destroys existing user data** on every schema change. That trade-off stops being acceptable the moment real users have data to lose, so Powerfy uses proper migrations from early on, even pre-launch — partly to build the habit of doing it the production way, partly so a schema change never has to be revisited later under more pressure.

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

### Users — Firebase
- **Authentication:** email/password, Google Sign-In, and anonymous guest mode via Firebase Auth.
- **Firestore collection:** `users/{uid}` (name, email).

### Payments — Stripe (test mode), via [powerfy-backend](https://github.com/PatriciaGea/powerfy-backend)
Checkout calls a Firebase Cloud Function to create a `PaymentIntent`, then opens Stripe's `PaymentSheet` with the returned client secret. Test card numbers (e.g. `4242 4242 4242 4242`, any future expiry, any CVC) simulate successful and failed payments without moving real money.

### A note on currency
Product prices come straight from the DummyJSON API, which returns them in **USD**. Powerfy displays prices in USD as-is, and calculates VAT and delivery fees on top of that in the same currency — there's no hidden conversion happening. This was a deliberate scope choice for an API-driven MVP with test data, not a limitation of the app: because currency formatting is centralized in one place and the payment amount is always passed to Stripe as an explicit `amount` + `currency` pair, both the **displayed currency** and the **currency the payment is actually processed in** could be swapped to any ISO currency (SEK, EUR, GBP, ...) by changing that one value — the same is true of the **payment recipient**, since that's controlled by whichever Stripe account's keys are configured on the backend, not hardcoded in the app.

## Configuration

Running this project requires a Firebase project (Authentication + Firestore enabled, Blaze plan for the Cloud Functions backend), a Stripe account with test-mode keys, and the companion [powerfy-backend](https://github.com/PatriciaGea/powerfy-backend) deployed to that same Firebase project.

- `app/google-services.json` — Firebase config for the Android app (package `se.tattooink.powerfy`)
- `local.properties` → `STRIPE_PUBLISHABLE_KEY` — Stripe test-mode publishable key, kept out of version control
- `powerfy-backend`'s `createPaymentIntent` Cloud Function must be live for the payment flow to work

DummyJSON requires no key or config, it's a public, unauthenticated API used only for product data.

##  Concepts practiced

- **MVVM + repository pattern**, with domain models kept separate from network DTOs and Room entities
- **Dependency injection with Hilt**, across ViewModels, repositories, and third-party SDKs (Firebase, Stripe)
- **Unidirectional data flow** with `StateFlow` and `collectAsState`, no UI state living outside a ViewModel
- **Local persistence with Room**, including a real `Migration` (not `fallbackToDestructiveMigration()`) — see below
- **Centralized navigation state**: a single `Scaffold` + `TopBar` shared across screens via the nav graph, instead of prop-drilling navigation callbacks through every screen
- **Secure client-server architecture for payments**: the app never holds a payment secret; a dedicated authenticated backend does
- **Design-to-code fidelity**: pulling exact colors, spacing, and typography from Figma via its API rather than eyeballing a screenshot

##  Learnings & challenges

A few real problems hit while building this, and how they were solved — the parts that don't show up in a demo GIF:

- **Room + KSP2 compiler bug.** `Room 2.6.1` threw an obscure `unexpected jvm signature V` error on `suspend fun` DAO methods with no return value, caused by a known KSP2 incompatibility — fixed by upgrading to `Room 2.7.1`, not by changing the (correct) code.
- **A silent Windows clipboard bug broke Stripe auth for hours.** Piping a secret into `firebase functions:secrets:set` via PowerShell's `|` silently appended a trailing newline, so the exact same key kept getting rejected by Stripe as "invalid" on every retry. Confirmed the key itself was valid with a direct `curl` call to Stripe's API, then fixed the actual cause by writing the secret with `Set-Content -NoNewline` instead of a pipe.
- **`PaymentSheet` crashed the Checkout screen on first composition.** Creating it with a raw `remember { PaymentSheet(...) }` registered an `ActivityResultLauncher` after the activity's lifecycle had already moved past `CREATED`, throwing an `IllegalStateException`. Fixed by switching to Compose's own `rememberPaymentSheet()`, which registers the launcher at the correct lifecycle point.
- **Why payments need a backend at all.** Stripe's secret key can never live in an Android app (anyone can decompile an APK), so creating a `PaymentIntent` has to happen server-side. That's the reason `powerfy-backend` exists as a separate, small Firebase Cloud Function repo rather than everything living in one place.
- **Vector Asset Studio can silently produce broken drawables.** Importing photographic banner images as Android *Vector* Assets failed the build with `fillColor is incompatible with <pattern>` — vector drawables only support solid/gradient fills, not embedded raster images. Fixed by importing them as plain raster PNGs instead.

## Roadmap

- [ ] Firestore-backed order history (linking completed Stripe payments to a `orders/{orderId}` collection)
- [ ] Unit tests for ViewModels and repositories
- [ ] UI tests for critical flows (checkout)
- [ ] Real delivery address management (currently a fixed placeholder on Checkout)

## Author

**Patricia Gea** — Web / Mobile developer
Hyper Island - Yrkeshogskolan (YH) - Higher Vocational Education (HVE) - Stockholm 
The Powerfy concept, brand, art design , and UI/UX design are original work, designed by Patricia Gea
[GitHub](https://github.com/PatriciaGea)

## License

This project is for portfolio purposes. Product data is provided by DummyJSON and does not represent a real store. All payments run in Stripe test mode — no real transactions occur.





                                   -.                          .-                                   
                                   :=.                        .=:                                   
                                    :=.                      .=:                                    
                                     :=. ..:----=--=----:.. .=:                                     
                                     .-====================-=-.                                     
                                   :-==========================-:                                   
                                 :================================:                                 
                               .====================================.                               
                              -======:  .==================.  :======-                              
                             -=======:   ==================   :=======-                             
                            -=========--====================--=========-                            
                           .============================================.                           
                           -============================================-                           
                          .==============================================.                          
                          .----------------------------------------------.                          
                                                     