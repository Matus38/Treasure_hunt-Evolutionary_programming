# Treasure_hunt-Evolutionary_programming
Evolutionary programming

Majme hľadača pokladov, ktorý sa pohybuje vo svete definovanom
dvojrozmernou mriežkou (viď. obrázok) a zbiera poklady, ktoré nájde po ceste.
Začína na políčku označenom písmenom “S” a môže sa pohybovať štyrmi
rôznymi smermi: “hore” H, “dolu” D, “doprava” P a “doľava” L. K dispozícii
má konečný počet krokov. Jeho úlohou je nazbierať čo najviac pokladov. Za
nájdenie pokladu sa považuje len pozícia, pri ktorej je hľadač aj poklad na tom
istom políčku. Susedné políčka sa neberú do úvahy.
Príkladom môže byť nasledovná mapa pokladov (0 = prazdno, 1 = poklad, S = štartovacia pozícia):<br />

0 0 0 0 0 0 0<br />
0 0 0 0 1 0 0<br />
0 0 1 0 0 0 0<br />
0 0 0 0 0 0 1<br />
0 1 0 0 0 0 0<br />
0 0 0 0 1 0 0<br />
0 0 0 S 0 0 0<br />
<br />
Danú úlohu riešte prostredníctvom evolučného programovania nad virtuálnym
strojom, ktorý bude mať 64 pamäťových buniek o veľkosti 1 byte. Bude poznať
štyri inštrukcie: inkrementáciu hodnoty pamäťovej bunky, dekrementáciu
hodnoty pamäťovej bunky, skok na adresu a výpis (H, D, P alebo L) podľa
hodnoty pamäťovej bunky. Inštrukcie majú tvar podľa nasledovnej tabuľky:<br />

<table style="width:100%">
  <tr>
    <th>Inštrukcia</th>
    <th>Tvar</th>
  </tr>
  <tr>
    <td>inkrementácia</td>
    <td>00XXXXXX</td> 
  </tr>
  <tr>
    <td>dekrementácia</td>
    <td>01XXXXXX</td> 
  </tr>
  <tr>
    <td>skok</td>
    <td>10XXXXXX</td> 
  </tr>
  <tr>
    <td>výpis</td>
    <td>11XXXXXX</td> 
  </tr>
</table>

<b>Opis riešenia</b>

Vstupné údaje sú uložené v súbore vstup.txt, ktoré sú formátované nasledovne.
Jednotlivé mapy, sú od seba oddelené jedným riadkom, ktorý môže byť prázdny
alebo v ňom môže byť napísaná ľubovoľná poznámka.
Ako prvé je číslo ktoré udáva veľkosť mapy. Nasleduje dvojica čísiel, ktoré sú
súradnicami začiatočnej pozície hľadača. Ďalej nasleduje počet pokladov za
ktorým sú v jednotlivých riadkoch súradnice daných pokladov oddelené
medzerami. Potom nasleduje číslo, ktoré udáva veľkosť populácie, ďalšie číslo
udávajúce počet generácií a nakoniec spôsob výberu rodičov pre novú
populáciu. Táto hodnota môže byť buď „turnaj“ alebo „ruleta“.

Ako prvé sa teda načítajú potrebné vstupné údaje zo súboru. Následne ich
program spracuje a inicializuje potrebné premenné. Po tomto sa vygeneruje prvá
populácia s náhodnými bunkami.

Následne sa postupne bunky jednotlivcov prekopírujú do virtuálneho stroja a ten
podľa zadania začne vykonávať dané inštrukcie. Výber smeru ktorým sa hľadač
vydá som určil pomocou posledných dvoch bitov kde 00 je hore, 01 dole, 10
doprava a 11 doľava. Tieto kroky sa ukladajú do konkrétneho jedinca. Fitnes
jedincov je vypočítaný na základe nájdených pokladov a táto hodnota je znížená
o jednu tisícinu počtu krokov ktoré jedinec vykonal.

V prípade, že ani jeden jedinec nenájde všetky poklady, program sa spýta
užívateľa či chce vytvoriť novú generáciu. Podľa spôsobu výberu rodičov sa
teda daný rodičia vyberú a pridajú do novej generácie. Následne je 60% šanca že
sa rodič i a rodič i + 1 skrížia a to tak, že sa vyberie náhodný počet buniek, ktoré
si navzájom vymenia.

Po dokončení kríženia je 15% šanca že potomok zmutuje tak, že sa vyberie
náhodná bunka v ktorej sa invertuje náhodný bit.
Nakoniec je 5% šanca na pridanie „novej krvi“ kedy 5päť náhodných jedincov
z populácie nahradia noví Takto to pokračuje až do kým sa nenájdu všetky
poklady alebo užívateľ nerozhodne inak.
