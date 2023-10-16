# Haladási napló

## 6. Hét
Ezen a héten a következőkkel haladtam:

- CameraX beintegrálása az alkalmazásba. Hozzáadtam egy Floating Action Buttont a saját kérdőívek (MySurveys) képernyőre, amit megnyomva felugrik a kérdőív beszkennelésre alkalmas ablak. 
- Elkészítettem a kérdőív beszkennelésre alkalmas ablakot (ScanSurveyScreen). Ha a felhasználó nem adott engedélyt az alkalmazásnak még a kamera használatára, akkor egy engedélyérő ablak jelenik meg, és csak a megadás után lehet használni a szkennelési funkciót.
- Ha a Szkennelés gombra nyomunk, felugrik egy AlertDialog a készített képpel, amit el lehet fogadni, vagy új képet lehet csinálni. Amiután elfogadtunk egy kérdőív képet, a szerkesztés képernyőre érkezünk, ahol a beolvasott kérdőívet szertkeszthetjük.
- A beolvasáshoz egyelőre az MLKit TextRecognizer-ét használom, viszont ez nyomtatott szövegre van elkészítve, nem kézzel írtra. Emiatt különböző követelményeket kellett felállítani a beolvasás sikerességéhez:

  - A kérdőíveket nyomtatott betűvel kell írni, minél szebben
  - A multiple_choice típusú kérdéseknél a válaszlehetőséget O- val kell jelölni
  - A checkbox típusú kérdéseknél a válaszlehetőséget X-szel kell jelölni (OpenCV nélkül nem tudom hogyan lehetne kis dobozkákat használni, nem találtam alakzatokat felismerő funkciót az MLKit-ben)
  - A short_answer típusú kérdéseknél csak a kérdés szövgét kell leírni
  - Minden kérdés meg kell legyen számozva
  - Az első sor mindig a kérdőív címe
  - **Kérdés:** Szükséges-e ezen fejleszteni, akár az OpenCV használatával, vagy a kézírás felismerésre valami megoldást találni, vagy kezdetleges megoldásnak ez is jó?

- Mostmár működik a kérdőív manuális kitöltése, a kötelező mezők kitöltése után aktívvá válik a send gomb, és amint sikeres a válasz mentése, felugrik egy ablak, amley jelzi a művelet sikerességét.
- A user megtekintheti azokat a kérdőíveket, és az adott váálaszokat, amelyeket ő küldött el a FilledOutSurveys képernyőn.



## 5. Hét

Ezen a héten a következőkkel haladtam:

- Fizikai eszközzel dolgozva, nem pedig emulatorral, mostmár sikerült a Firestore-al összekötni az alkalmazást, így már minden adat naprakészen a felhőben található. 
- A kérdőíveket meg lehet tekinteni, szerkeszteni, törölni és hozzáadni kérdéseket.
- Kicsit előre nyúlva az ütemtervben, főleg azzal haladtam, hogy ha már így belejöttem a Firestore-al való munkába, hogy el lehessen küldeni a kérdőíveket másik felhasználónak, és a címzetthez beérkezzen a kérdőív. A beérkezett kérdőívet a felhasználó már el tudja kezdeni kitölteni, és van ellenőrzés arra, hogy minden kötelező mezőt kitölt-e, csak akkor válik kattinthatóvá a kérdőív beküldése.
- Tehát elkészítettem:

  -  a kezdetleges SentSurveys képernyőt, ahol a felhasználó látja a már kiküldött kérdőíveit
  - az InboxSurvey képernyőt, ahol a beérkezett kérdőívek láthatóak. Ha a felhasználó rákattint a kitöltés gombra az egyik kérdőíven, akkor felugrik egy kitöltés (FillOutSurvey) képernyő. Ezen már kattinthatóak a beviteli mezők.
- Amikor egy kérdőívet elküldök, akkor csak a kérdőív id-ját adom meg, ezért fellépett az a probléma, hogy ha elküldtünk egy kérdőívet, majd azt a saját kérdőíveink közt szerkesztettük, akkor a címzett is az új verziót látta, amikor túloldalon ki akarta tölteni a kérdőívet. Ezt úgy orvosoltam, hogy egy kérdőív küldésénél készül egy másolat a kérdőívről, új id-val, és azt küldjük ki. Ennek a másolatnak viszont a creatorId-ja már 'none'-ra van állítva, hogy a MySurveys képernyőn ne jelenjen meg a felhasználónak kétszer ugyanaz a kérdőív, csak a legfrissebb formában.

## 4. Hét

  Ezen a héten a következőkkel haladtam:

  - Kicsit eltérve az ütemtervtől nem teljesen a kamerás beolvasással foglalkoztam, mert az emulatorral kényelmetlen lett volna. Az MLKit és CameraX könyvtárakról kapott videókat megnéztem, kicsit olvasgattam róluk. Mivel kamerahasználat nem állt a rendelkezésemre, ezért a képernyőket hoztam létre az alkalmazásban, a Firestore-t próbáltam működésre bírni.
  - Az alkalmazásban létrehoztam egy **BottomNavigation-t**, amely a főképernyők közti navigálásra szolgál. 
  - Beüzemeltem a Firestore-t, adtam hozzá példaadatokat a console-os felületen, hogy kipróbáljam, hogy működik-e az appommal az olvasás. Sajnos emulatorral nem sikerült az olvasást tesztelni, viszont a legalapabb írási művelet az sikerült. Amikor az olvasást akartam kipróbálni, akkor a hibaüzenet amit kaptam: 

    - [Firestore] Could not reach Cloud Firestore backend. Backend didn't respond within 10 seconds. Reménykedek, hogy ez az emulator miatt van, hiszen az írás volt már, hogy sikerült az appból.
    - Stream closed with status: Status{code=UNAVAILABLE, description=Channel shutdownNow invoked, cause=null}.
  - Mivel a Firestore nem működött, foglalkoztam a képernyőkkel, amelyeket egyelőre beleégetett adatokkal teszteltem. 
  - Elkészítettem a **MySurveys** oldalt, ahol megjelennek a felhasználó kérdőívei egymás alatt listában. Ha a listaelemre kattintunk, akkor megjelenik a **SurveyDetails** képernyő, ahol meg lehet tekinteni a kérdőívet a kérdésekkel.
  - Ha a kis Edit ikonra kattintunk, akkor megjelenik az **EditSurvey** képernyő, amely ugyancsak beégetett adatokon működik. Itt lehetőség van megváltoztatni az egyes kérdőívek kérdéseit. Hiányzik még a kérdés hozzáadás funkció, és egy DONE gomb, aminek hatására majd Firestoreba kellene írni.
  - **Kérdések**:
    - Sikerült-e találni bármilyen telefont, amit tudnék használni emulator helyett?
    - Amikor commitolni akarok az AndroidManifest file-omra errort kapok, pedig nem is szeretném ha TV-n működne az app :) Vajon miért lehet ez?: 
    
      - Error:(2, 2) Expecting an activity to have `android.intent.category.LEANBACK_LAUNCHER` intent filter
      - Error:(2, 2) Expecting `<uses-feature android:name="android.software.leanback" android:required="false" />` tag
      - Error:(2, 2) Hardware feature `android.hardware.touchscreen` not explicitly marked as optional 



## 3.Hét

Ezen a héten a következőkkel haladtam:

- Elkészítettem a fontosabb képernyők **drótvázait** amiről beszéltünk múltkori konzultáción, ezeket betettem a specifikációt tartalmazó doksiba, némi magyarázattal.
- Kiegészítettem pár pontosítással a specifikációt a feladattal kapcsoatosan.
- Megterveztem, hogy hogyan fogom tárolni a kérdőíveket FireStore-ban NoSQL adatbázisban. A megtervezett **sémához** ER diagram is készült, ezt szintén a specifikáció doksiban helyeztem el.
- Utánanéztem a **Bottom Navigation bar** használatának, hiszen majd ezt szeretném használni az alkalmazásomban a képernyők közti navigációra.
- Ismerkedtem a Nested Navigation Graph-al, picit átalakítottam az eddig meglévő navigációt az alkalmazásomban.

## 2. Hét

Ezen a héten a következőkkel haladtam:

- Létrehoztam egy **Firebase** projektet az alkalmazásomhoz, összekapcsoltam őket. Aktiváltam az autentikációt email és jelszó segítségével a projekten belül.
- Ismerkedtem a **Jetpack Compose**-al.
- Létrehoztam az alkalmazáshoz egy Android Studio **projektet**, és elkészítettem egy kezdetleges **autentikációt**, nem figyelve a UI design-ra, úgy gondolom, hogy ha majd marad idő akkor még alakítgatok rajta, de szerettem volna, ha van egy működő autentikáció. Sikerült is tesztelni a regisztráció és bejelentkezés funkciókat, Firebase-ben is láthatóak a userek. Probléma volt viszont, hogy elég lassan működik az autentikáció, sokszor több, mint 5 percbe is beletelik, de gondolom az is befolyásolja, hogy emulator-t használok. Az autentikációhoz segítségül a következő repo-t használtam: https://github.com/alexmamo/FirebaseSignInWithEmailAndPassword/tree/master. Értelmeztem a kódot, és picit átalakítva felhasználtam. Nem tudom itt mekkora gond lehet plágium szempontból, hogy ezt a részt nem teljesen magamtól csináltam, de úgy gondolom mivel nem ez az alkalmazásom lényege nem lehet akkora baj.
- Utánanéztem a Dagger-Hilt Dependency Injection könyvtárnak, ezt használtam is az autentikáció elkészítése során, így sokkal modulárisabb lett a kód. Ehhez főként a következő videót használtam fel: https://www.youtube.com/watch?v=bbMsuI2p1DQ&ab_channel=PhilippLackner

-   Gondolkoztam, hogy hogyan épüljön fel az alkalmazás, milyen képernyők legyenek. Még képernyőtervet nem készítettem, de bejelentkezés után majd egy **Bottom Navigation bar**-t szeretnék használni, amely segítségével lehet váltani majd a különböző fülek közt. A következő lépés, hogy ezt hozzáadjam a projekthez.

- Kiegészítettem a **specifikációt**, valamint készítettem egy kezdetleges **ütemtervet** is, megtalálható a dokumentáció mappában.

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
      
  Röviden ezzel foglalkoztam, valamint elkezdtem írni a specifikációt(még nagyon alapszintű, először szerettem volna, ha a fent említett kérdéseket átbeszéljük és véglegesítem a technológiákat), gondolkozni az alkalmazás kinézetén. Még azért elég sok kérdés van bennem, kicsit bizonyalan vagyok, úgyhogy kíváncsi vagyok te mit gondolsz a fent említett témákról. Ha van valami anyagod ami segíthet elindulni, vagy esetleg valami arról, hogy mikre figyeljek fejlesztés során, mik a best practice-k, hogy minél minőségibb kódot tudjak írni, azt megköszönöm. 
