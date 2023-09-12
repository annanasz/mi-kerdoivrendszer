# Haladási napló
## 1. Hét
Ezen a héten a következőkkel haladtam:

- Kicsit átgondoltam, hogy mik a célok az alkalmazással, miket szeretnék alapszinten megvalósítani. Mindenképpen szeretném ha az
alkalmazás képes lenne kép alapján felismerni a kérdőíveket, akár kamera használatával, akár telefon galériából feltöltve. Ezeket a
kérdőíveket a felhasználó később megtalálja az alkalmazásban, szerkeszteni tudja őket, és kitölteni akár beszédvezérléssel. A kitöltött kérdőíveket is meg lehet tekinteni. Azonban ez még nekem kevésnek tűnik, mert akkor ezeket a kérdőívek csak a felhasználónál lesznek.
- Gondolkoztam azon is, hogy hogyan lehetne egy Doodle-t, vagy egy Google Formsot kitölteni az alkalmazás segítségével, ha a felhasználó csak megadja a linket.
Nem lenne rossz, ha lenne ilyen funkció is, és kéz nélkül be lehetne küldeni ezeket a kérdőíveket, azonban nem tudom ezt mennyire lehet bonyolult megvalósítani. **Kérdés: Neked erre mi lenne a javaslatod?**
- Elsődlegesen *angol* nyelven szeretném ha működne az alkalmazás 
- Az app képes lesz felismerni a kérdőív címét, hogy kötelezőek-e a kérdések, és a kérdéstípusokat. A kérdéstípusok, amiket képes lesz felismerni az alkalmazás:
  -  multiple choice
  -  checkbox
  -  short answer/paragraph
  -  dátum megadás
- Bejelentkezést is lehetne tenni az alkalmazásba, de nem akarom túlvállalni sem magam. **Kérdés: Te mit javasolsz?**
- Az alkalmazás fejlesztése során MVVM mintára gondoltam, mert ahogy keresgéltem úgy láttam, hogy széleskörűen használják mobilfejlesztésben, és Kliensoldali Technológiák háziban is használtam már.
- **Technológiák amiket használni szeretnék:**
    - **Frontend:** Amikor hallgattam az Andorid tárgyat még nem Composet tanultunk, szóval ha még nem túl outdated XML alapú UI-t készítenék, mert abban van már gyakorlatom
    - **Backend:** Itt kérnék egy kis segítséget, hogy mi lehet a legjobb választás. Én arra gondoltam, hogy nem FireBase-t használnálnék, hanem kipróbálnám a SpringBoot-ot, vagy esetleg NodeJS-t (alapszinten használtam már). Mi a véleményed, mennyire megvalósítható egy custom backend, ha még nincs nagy tapasztalatom ebben.
    - **Adatbázis:** Mindenképpen valami NoSQL adatbázist használnék a kérdőívek eltárolására, én a MongoDB-re gondoltam. Nem tudom, hogy lokális tárolásra szükség van-e, de a Room-ot használtam már.
    - **Kérdőív felismerés:** MLKit
    - **Kérdőív felolvasás, kitöltés beszéd segítségével:** beépített SpeechRecognizer és TextToSpeech. **Kérdés: ehhez van más javalatod?**
      
  Röviden ezzel foglalkoztam, valamint elkezdtem írni a specifikációt(még nagyon alapszintű, először szerettem volna, ha a fent említett kérdéseket átbeszéljük és véglegesítem a technológiákat), gondolkozni az alkalmazás kinézetén. Még azért elég sok kérdés van bennem, úgy hogy kíváncsi vagyok te mit gondolsz a fent említett témákról. Ha van valami anyagod ami segíthet elindulni, vagy esetleg valami arról, hogy mikre figyeljek fejlesztés során, mik a best practice-k, hogy minél minőségibb kódot tudjak írni, azt megköszönöm. 
