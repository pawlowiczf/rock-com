# rock-com

## Powiadomienia o błędach podczas uruchamiania testów w GithubActions 
Akcje, które zakończyły się niepowodzeniem, powodują wysłanie ostrzegawczego maila na pocztę. Aby to wyłączyć:
- kliknij w swój avatar w prawym górnym rogu, następnie *Settings*
- po lewej stronie wybierz *Notifications* i zjedź do sekcji *Actions*
- odznacz opcję *Email*

---

## Rockcom database

A guide on how to set up container with postgresql database.

Some instructions might use relative paths.
With that in mind, you might have to adjust them (or change directory).

### Prerequisites
- Docker
- Docker desktop running

### How to set up
- Run `docker compose -f db/docker-compose.yml up --build -d`

Now, you can run it via docker desktop.

Keep in mind, if database schema has been changed, you will need to run above command again.