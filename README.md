# Zadanie_prenos_dat

Semestrálna práca 4. ročník inžinierske štúdium, predmet prenos dát.   Sklada sa z dvoch častí-> Tvorba klienta, tvorba servera. Programované v jazyku Java použité inteliJ štúdio. 
Na testovanie klienta sa využíva docker, kde beží lokálna kópia serveru. Klient komunikuje s týmto serverom cez retrofit. Na začiatku treba tento retrofit zbuildovať cez Builder. a vytvoriť komunikačné rozhranie
nad triedou ktorá využíva retrofit @GET... notácie.  Retrofit si tieto metódy implementuje a vysiela a príjma potrebné dáta na server.  S dokerom komunikuje cez localhost:9000. 
Doker beží v časovom pásme Bratislavy.  Klient komunikuje so serverom cez execute() príkaz, ktorý ponúka retrofit nad našími vytvorenými komunikačnými metódami interfejsu. Vie ich nastaviť a potom vykonať cez tento
execute. po vykonaní je prístup k odozve response ako návratová hodnota execute().
