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
    - [Etap 1 - analiza i przygotowanie projektu](#etap-1---analiza-i-przygotowanie-projektu)
    - [Etap 2 - integracja i synchronizacja danych](#etap-2---integracja-i-synchronizacja-danych)
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

W projekcie wykorzystywane są dwa zewnętrzne źródła danych:

- **PandaScore API** - główne źródło danych dla Counter-Strike 2 i League of Legends, obejmujące mecze, turnieje, drużyny oraz zawodników.
- **Cito API** - dodatkowe źródło danych o drużynach, wykorzystywane do ich dopasowywania i identyfikacji między źródłami.

Dane pobierane z zewnętrznych API są przetwarzane i zapisywane
w lokalnej bazie PostgreSQL.

W projekcie zastosowano synchronizację przyrostową, deduplikację rekordów
oraz mapowanie identyfikatorów zewnętrznych drużyn.

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

Wstępny diagram ERD przygotowany podczas pierwszego etapu projektu.
Diagram zostanie zaktualizowany po zakończeniu prac nad strukturą bazy danych:

![Diagram ERD](docs/erd.svg?v=2)

## Docker

Projekt wykorzystuje Docker oraz Docker Compose.

Aktualnie uruchamiane są dwa kontenery:

- `esport-backend` - aplikacja Kotlin/Spring Boot,
- `esport-postgres` - baza PostgreSQL.

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

### Etap 1 - analiza i przygotowanie projektu

W ramach pierwszego etapu wykonano:

- [x] analizę wymagań projektu,
- [x] wybór technologii,
- [x] wybór gier: Counter-Strike 2 i League of Legends,
- [x] wybór źródeł danych: PandaScore API oraz Cito API,
- [x] zaprojektowanie modelu bazy danych i diagramu ERD,
- [x] utworzenie szkieletu backendu w Kotlin/Spring Boot,
- [x] konfigurację PostgreSQL,
- [x] przygotowanie Dockerfile dla backendu,
- [x] konfigurację Docker Compose dla backendu i PostgreSQL,
- [x] uruchomienie oraz przetestowanie komunikacji backendu z bazą danych,
- [x] przygotowanie VPS i instalację Docker oraz Docker Compose.

### Etap 2 - integracja i synchronizacja danych

W ramach drugiego etapu wykonano:

#### Integracja PandaScore API

- integrację z PandaScore API dla Counter-Strike 2 i League of Legends,
- utworzenie klienta API z wykorzystaniem `RestClient`,
- pobieranie meczów nadchodzących, trwających i zakończonych,
- pobieranie danych zawodników,
- przygotowanie klas DTO do odbierania danych z API,
- przetwarzanie i zapisywanie meczów, turniejów, etapów turniejów,
  drużyn i zawodników w PostgreSQL,
- obsługę meczów, dla których nie są jeszcze znane drużyny.

#### Synchronizacja i deduplikacja danych

- implementację importu początkowego meczów,
- implementację synchronizacji przyrostowej obejmującej nowe,
  zmienione i usunięte rekordy,
- zapisywanie czasu ostatniej pomyślnej synchronizacji,
- aktualizację istniejących rekordów na podstawie `externalId`,
- ograniczanie powstawania duplikatów w bazie danych,
- zapisywanie zawodników i ich powiązań z drużynami,
- przechowywanie historii przynależności zawodników do drużyn,
- przygotowanie automatycznej synchronizacji za pomocą `@Scheduled`.

#### Integracja Cito API

- dodanie drugiego źródła danych - Cito API,
- pobieranie danych drużyn z Cito,
- implementację mechanizmu dopasowywania drużyn do rekordów
  zapisanych w bazie danych,
- przygotowanie mapowania identyfikatorów zewnętrznych drużyn,
- zapisywanie powiązań między drużynami pochodzącymi
  z różnych źródeł,
- ograniczanie liczby zapytań do API,
- przygotowanie harmonogramu synchronizacji danych Cito.

#### Obsługa błędów i audyt

- konfigurację timeoutów dla zapytań do API,
- dodanie mechanizmu ponawiania nieudanych zapytań (`retry`),
- obsługę błędu HTTP 429 i nagłówka `Retry-After`,
- przygotowanie rate limitera ograniczającego częstotliwość zapytań,
- dodanie mechanizmu circuit breaker z wykorzystaniem Resilience4j,
- zapisywanie historii importów i synchronizacji w PostgreSQL,
- rejestrowanie statusów `RUNNING`, `SUCCESS` i `FAILED`,
- zapisywanie liczby pobranych i pominiętych rekordów
  oraz komunikatów błędów,
- rejestrowanie wykorzystania zapytań do Cito API,
- dodanie migracji bazy danych za pomocą Flyway.

#### Weryfikacja działania

- przetestowano pobieranie i zapisywanie danych CS2 i LoL,
- przetestowano ręczne uruchamianie synchronizacji obu gier,
- sprawdzono brak zduplikowanych identyfikatorów meczów,
- przetestowano import zawodników,
- sprawdzono zapisywanie historii importów i synchronizacji,
- przetestowano integrację Cito i dopasowywanie drużyn.


Póki co import początkowy meczów wykorzystuje ograniczoną liczbę stron API
i nie obejmuje pełnej historii rozgrywek.


## Kolejny etap

