# siw-food
Realizzazione di un sistema informativo che offre informazioni relative a ricette.
Il sistema sarà progettato e realizzato usando SpringBoot e Postgres SQL.

L'obbiettivo del sistema è quello di consentire almeno l'esecuzione di 6 casi d'uso:

*almeno due che abbiamo come attore l’amministratore:

  *inserimento dati
  *aggiornamento dati
  
*almeno due che abbiamo come attore l'utente registrato
  *almeno uno di inserimento
  
*almeno due che abbiano come attore l’utente generico 

##Autenticazione e Registrazione al sistema

Il sistema prevede un diverso funzionamento in base al tipo di utente.

*1.Utente non autenticato : possono consultare tutte le informazioni sulle ricette e sui cuochi, ma non possono apportare nessun tipo di modifica ai dati.
*2.Utente autenticato (chef) : oltre a poter consultare tutte le ricette, possono aggiungere nuovi ingredienti e nuove ricette; inoltre possono modificare e cancellare le proprie ricette.
*3.Utente autenticato (admin) : possono aggiungere, modificare, cancellare ingredienti, cuochi e ricette.

Ogni utente potrà accedere al sistema tramite :

 *Credenziali del proprio account, previa registrazione.
