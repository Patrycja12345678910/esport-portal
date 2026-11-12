# Esport Portal

Aplikacja webowa do prezentowania i analizowania danych dotyczących
rozgrywek esportowych dla gier Counter-Strike 2 oraz League of Legends.

## Spis treści

- [Technologie](#technologie)
- [Zewnętrzne API](#zewnętrzne-api)
- [Projekt bazy danych](#projekt-bazy-danych)
- [Docker](#docker)
- [Konfiguracja](#konfiguracja)
- [Aktualny stan projektu](#aktualny-stan-projektu)
    - [Etap 1 – analiza i przygotowanie projektu](#etap-1--analiza-i-przygotowanie-projektu)
    - [Etap 2 – integracja i synchronizacja danych](#etap-2--integracja-i-synchronizacja-danych)
- [Dalsze prace](#dalsze-prace)


## Technologie

### Backend
- Kotlin
- Spring Boot
- Gradle
- Java 21

### Baza danych
- PostgreSQL 16

### Frontend
- React (planowany w kolejnych etapach)

### Infrastruktura
- Docker
- Docker Compose
- VPS Mikr.us
- Git / GitHub

## Zewnętrzne API

W projekcie planowane jest wykorzystanie dwóch zewnętrznych źródeł danych:

- **PandaScore API** – główne źródło danych dla CS2 i League of Legends,
- **Riot Games API** – źródło uzupełniające dla League of Legends.

API będą wykorzystywane do pobierania informacji dotyczących m.in.:
- turniejów,
- meczów,
- drużyn,
- zawodników.

Dane pobierane z zewnętrznych źródeł będą normalizowane, deduplikowane
i zapisywane w lokalnej bazie PostgreSQL.

## Projekt bazy danych

Zaprojektowano model danych aplikacji obejmujący m.in.:
- gry,
- turnieje i etapy turniejów,
- mecze,
- mapy,
- drużyny,
- zawodników i składy drużyn,
- statystyki zawodników,
- użytkowników,
- ulubione drużyny i turnieje.

Diagram ERD przedstawiający strukturę bazy danych:

![Diagram ERD](docs/erd.svg?v=2)

## Docker

Projekt wykorzystuje Docker oraz Docker Compose.

Aktualnie uruchamiane są dwa kontenery:

- `esport-backend` – aplikacja Kotlin/Spring Boot,
- `esport-postgres` – baza PostgreSQL.

Backend komunikuje się z bazą PostgreSQL wewnątrz sieci Docker.

Uruchomienie projektu:

```bash
docker compose up --build
```

## Konfiguracja

Konfiguracja bazy danych przekazywana jest do kontenerów za pomocą
zmiennych środowiskowych.

Przykładowa konfiguracja znajduje się w pliku `.env.example`.

Plik `.env` zawierający lokalne dane konfiguracyjne nie jest przechowywany
w repozytorium.

## Aktualny stan projektu

### Etap 1 – analiza i przygotowanie projektu

W ramach pierwszego etapu wykonano:

- [x] analizę wymagań projektu,
- [x] wybór technologii,
- [x] wybór gier: Counter-Strike 2 i League of Legends,
- [x] wybór źródeł danych: PandaScore API oraz Riot Games API,
- [x] zaprojektowanie modelu bazy danych i diagramu ERD,
- [x] utworzenie szkieletu backendu w Kotlin/Spring Boot,
- [x] konfigurację PostgreSQL,
- [x] przygotowanie Dockerfile dla backendu,
- [x] konfigurację Docker Compose dla backendu i PostgreSQL,
- [x] uruchomienie oraz przetestowanie komunikacji backendu z bazą danych,
- [x] przygotowanie VPS i instalację Docker oraz Docker Compose.

### Etap 2 – integracja i synchronizacja danych

W ramach drugiego etapu wykonano:

- integrację z PandaScore API dla gry Counter-Strike 2,
- utworzenie klienta API z wykorzystaniem `RestClient`,
- pobieranie meczów nadchodzących, trwających i zakończonych,
- przygotowanie klas DTO do odbierania danych z API,
- przetwarzanie i zapisywanie informacji o meczach, drużynach, turniejach i etapach turniejów w PostgreSQL,
- dodanie aktualizacji istniejących rekordów na podstawie identyfikatorów zewnętrznych (`externalId`),
- implementację synchronizacji przyrostowej, pobierającej nowe, zmienione i usunięte rekordy,
- zapisywanie czasu ostatniej pomyślnej synchronizacji w bazie danych,
- uruchomienie automatycznej synchronizacji danych za pomocą `@Scheduled`,
- dodanie podstawowego logowania przebiegu synchronizacji,
- obsługę meczów, dla których nie są jeszcze znane drużyny,
- dodanie migracji bazy danych za pomocą Flyway,
- konfigurację timeoutów dla zapytań do API,
- dodanie mechanizmu ponawiania nieudanych zapytań (`retry`),
- obsługę błędu HTTP 429 oraz nagłówka `Retry-After`,
- przygotowanie rate limitera ograniczającego częstotliwość zapytań,
- dodanie mechanizmu circuit breaker z wykorzystaniem biblioteki Resilience4j,
- przygotowanie testów synchronizacji oraz wybranych mechanizmów obsługi błędów API.



**Do dokończenia w ramach drugiego etapu pozostało:**

- rozszerzenie integracji PandaScore API o grę League of Legends,
- dodanie Riot Games API jako drugiego, uzupełniającego źródła danych,
- implementacja normalizacji i deduplikacji wspólnych danych pochodzących z dwóch źródeł,
- przygotowanie mapowania identyfikatorów zewnętrznych na identyfikatory wewnętrzne aplikacji,
- dopracowanie harmonogramu synchronizacji dla różnych rodzajów meczów,


## Kolejny etap

