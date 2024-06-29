# SIW-Food: Sistema Informativo per Ricette

SIW-Food è un sistema informativo progettato per offrire informazioni relative a ricette culinarie. Questo sistema è stato sviluppato utilizzando Spring Boot per la parte backend e PostgreSQL come database.

## Obiettivi del Sistema

Il sistema SIW-Food consente l'esecuzione di almeno sei casi d'uso, suddivisi tra amministratori, utenti registrati e utenti generici.

### Casi d'Uso

#### Amministratore

1. **Inserimento Dati:** L'amministratore può aggiungere nuovi ingredienti, cuochi e ricette al sistema.
2. **Aggiornamento Dati:** L'amministratore può modificare o cancellare ingredienti, cuochi e ricette esistenti.

#### Utente Registrato (Chef)

1. **Inserimento Nuovi Ingredienti:** L'utente registrato può aggiungere nuovi ingredienti al sistema.
2. **Inserimento Nuove Ricette:** L'utente registrato può creare nuove ricette.
3. **Modifica e Cancellazione delle Proprie Ricette:** L'utente registrato può modificare o cancellare le proprie ricette.

#### Utente Generico

1. **Consultazione Ricette:** L'utente generico può consultare tutte le informazioni sulle ricette disponibili.
2. **Consultazione Cuochi:** L'utente generico può visualizzare le informazioni relative ai cuochi.

## Autenticazione e Registrazione al Sistema

Il sistema prevede un diverso funzionamento in base al tipo di utente. Gli utenti possono registrarsi e accedere al sistema utilizzando le credenziali del proprio account.

### Tipi di Utente

1. **Utente non Autenticato:** Gli utenti non autenticati possono consultare tutte le informazioni sulle ricette e sui cuochi, ma non possono apportare nessun tipo di modifica ai dati.
2. **Utente Autenticato (Chef):** Gli chef, oltre a poter consultare tutte le ricette, possono aggiungere nuovi ingredienti e nuove ricette; inoltre possono modificare e cancellare le proprie ricette.
3. **Utente Autenticato (Admin):** Gli amministratori possono aggiungere, modificare e cancellare ingredienti, cuochi e ricette.

## Tecnologie Utilizzate

- **Backend:** Spring Boot
- **Database:** PostgreSQL

## Istruzioni per l'Installazione

1. **Clona il repository:**
   ```bash
   git clone https://github.com/tuo-repo/siw-food.git
   cd siw-food
