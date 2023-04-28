import java.util.Scanner;
import java.io.*;

public class Legesystem {
    private Koe<Pasient> pasientListe = new Koe<Pasient>();
    private Koe<Lege> legeListe = new Koe<Lege>();
    private Koe<Legemiddel> legemiddelListe = new Koe<Legemiddel>();
    private Scanner innDataLeser = new Scanner(System.in);
    int linjeNr = 0;
    // Metode som leser inn resept, lege, legemiddel og pasient-data fra fil.
    public void lesFraFil(String filnavn) {
        // Kaster FileNotFoundException om den ikke finner en fil.
        try {
            File filobjekt = new File(filnavn);
            Scanner fil = new Scanner(filobjekt);
            //Lager string currentType som sier hvilken type vi skal lese inn
            String currentType = "";
            // Leser inn fra fil
            while (fil.hasNextLine()) {
                String linje = fil.nextLine();
                linjeNr++;
                String[] dataList = linje.split(" ");
                // Sjekker om linja vi landet på er en kommentarlinje eller data
                if (dataList[0].equals("#")) {
                    currentType = dataList[1];
                    linje = fil.nextLine();
                    linjeNr++;
                }
                if (currentType.equals("Leger")) {
                    leggTilLege(linje);
                } else if (currentType.equals("Legemidler")) {
                      leggTilLegemiddel(linje);
                  } else if (currentType.equals("Resepter")) {
                        nyResept(linje);
                    } else if (currentType.equals("Pasienter")) {
                          leggTilPasient(linje);
                      }
            }
            // Lukker filen
            fil.close();

      // Kaller på ryddListe-funksjonen for å rydde opp i eventuelle null-verdier.
      pasientListe = pasientListe.ryddListe();
      legemiddelListe = legemiddelListe.ryddListe();
      fiksLegemiddelNummere();

      } catch (FileNotFoundException e) {
            System.out.println("Filen " + filnavn + " eksisterer ikke.");
        }
  }

  // Metode som legger til resepter fra linjedata
  private void nyResept(String linje) {
      String[] data = linje.split(",");
      int legePos = finnLegePos(data[1]);
      Lege lege = null;
      Legemiddel legemiddel = null;
      Pasient pasient = null;
      if (!sjekkReseptFormat(data)) {
          System.out.println("Reseptformat er ugyldig " + "linje: " + linje);
          return;
      }

      try {
          lege = legeListe.hent();
          legemiddel = legemiddelListe.hent();
          pasient = pasientListe.hent();
      } catch (UgyldigListeindeks e) {
            System.out.println(e.getMessage() + " (leggTilResept)" + " linje: " + linje);
            return;
        }
      if (legemiddel == null) {
          System.out.println("Legemiddel finnes ikke, legemiddelnummeret: " + Integer.parseInt(data[0]) + " linje: "+linje);
          return;
      }

      // Sjekker hvilken resepttype som skal legges til, tester det og skriver ut feilmelding om det ikke funker.
      if (data[3].equals("hvit")) {
          try{
              // Lager ny resept av riktig type, legger den til i pasientens reseptinventory. Fanger opp ulovlig utskrift og skriver dette ut
              Resept resept = lege.skrivHvitResept(legemiddel, pasient,Integer.parseInt(data[4]));
          } catch (UlovligUtskrift e) {
                System.out.println(e.getMessage() +" (skrivHvitResept) " + "linje: " + linje);
            }

      } else if (data[3].equals("blaa")) {
            try{
                Resept resept = lege.skrivBlaResept(legemiddel, pasient, Integer.parseInt(data[4]));
            } catch (UlovligUtskrift e) {
                  System.out.println(e.getMessage() + " (skrivBlaaResept) " + "linje: " + linje);
              }

      } else if (data[3].equals("millitaer")) {
            try{
                Resept resept = lege.skrivMilResept(legemiddel, pasient);
            } catch (UlovligUtskrift e) {
                  System.out.println(e.getMessage() +" (skrivMilitaerresept) " + "linje: " + linje);
              }

      } else if (data[3].equals("p")) {
            try{
                Resept resept = lege.skrivPResept(legemiddel, pasient, Integer.parseInt(data[4]));
            } catch (UlovligUtskrift e) {
                  System.out.println(e.getMessage() + " (skrivPResept) " + "linje: " + linje);
              }

        }
  }

  // Metode som legger til leger fra linjedata
  private void leggTilLege(String linje) {
      // Deler opp linja mellom komma, setter de ulike delene til variabler
      String[] data = linje.split(",");
      String navn = data[0];
      String kontrollId = data[1];
      if (!sjekkLegeFormat(data)) {
          System.out.println("Formatet på legestrengen er feil" + " linje: " + linje);
          return;
      }

      // Sjekker om kontrollId er 0, hvis ja: legen får type Lege. hvis nei: legen er Spesialist.
      if (!kontrollId.equals("0")) {
          legeListe.leggTil(new Spesialist(navn, kontrollId));
      }
      else {
      legeListe.leggTil(new Lege(navn));
      }
  }

  // Metode som legger til legemiddel fra linjedata
  private void leggTilLegemiddel(String linje) {
      String[] data = linje.split(",");
      String navn = data[0];
      String type = data[1];
      // Sjekker om formatet til legemiddelet er riktig. Hvis ikke, legges det til et legemiddel l som er null
      if (!sjekkLegemiddelFormat(data)) {
          Legemiddel l = null;
          legemiddelListe.leggTil(l);
          System.out.println("Formatet på legemiddelstrengen er feil" + " linje: " + linje);
          return;
      }
      int pris = (int)Math.round(Double.parseDouble(data[2]));
      double virkestoff = Double.parseDouble(data[3]);

      // Sjekker også typen til legemiddelet og lager tilsvarende.
      if (type.equals("narkotisk")) {
          // gjør om verdien til en double, runder av og gjør til en int.
          int styrke = (int)Math.round(Double.parseDouble(data[4]));
          legemiddelListe.leggTil(new Narkotisk(navn, pris, virkestoff, styrke));
      }
      else if (type.equals("vanedannende")) {
          int styrke = (int)Math.round(Double.parseDouble(data[4]));
          legemiddelListe.leggTil(new Vanedannende(navn, pris, virkestoff, styrke));
      }
      else if (type.equals("vanlig")) {
          legemiddelListe.leggTil(new VanligLegemiddel(navn, pris, virkestoff));
      }
  }

  // Metode som legger til pasienter fra linjedata
  private void leggTilPasient(String linje) {
      String[] data = linje.split(",");
      Pasient pasient = new Pasient(data[0],data[1]);
      if (!sjekkPasientFormat(data)) {
          System.out.println("Formatet på pasientstrengen er feil" + " linje: " + linje);
          return;
      }
      pasientListe.leggTil(pasient);
  }

  // Finner leger ved en gitt posisjon utifra et navn
  private int finnLegePos(String navn) {
      int teller = 0;
      for (Lege lege : legeListe) {
          if (lege.hentNavn().equals(navn)) {
              return teller;
          }
          teller++;
      }
      return -1;
  }

  // Sjekker om strengen er formatert riktig
  private boolean sjekkLegemiddelFormat(String[] data) {
      if (data.length == 5 || data.length == 4) {
          try {
              int a = (int)Math.round(Double.parseDouble(data[2]));
              double b = Double.parseDouble(data[3]);
              return true;
          } catch (NumberFormatException e) {
                return false;
            }
      }
      if (data.length == 5) {
          try {
              int c = Integer.parseInt(data[4]);
              return true;
          } catch (NumberFormatException e) {
                return false;
            }
      }
      return false;
  }

  // Sjekker om strengen er formatert riktig
  private boolean sjekkReseptFormat(String[] data) {
      if (data.length == 5 || data.length == 4) {
          try {
              int a = Integer.parseInt(data[0]);
              int b = Integer.parseInt(data[2]);
              return true;
          } catch (NumberFormatException e) {
                return false;
            }
      }
      if (data.length == 4) {
          return (data[3].equals("p"));
      }
      return false;
  }

  // Sjekker formatering
  private boolean sjekkLegeFormat(String[] data) {
      if (data.length == 2) {
          try {
              int a = Integer.parseInt(data[1]);
              return true;
          } catch (NumberFormatException e) {
                return false;
            }
      }
      return false;
  }

  // Sjekker formatering
  private boolean sjekkPasientFormat(String[] data) {
      return (data.length == 2);
  }

  // Funksjonen for innholdet i løkken. Kalles når vi går tilbake til hovedmeny.
  private void loekkeInnhold() {
      // Skriver ut hovedmeny og bruker velger neste trekk
      System.out.println("'q' for å avslutte.\n'b' for å gå tilbake til hovedmeny."+
      "\n'hjelp' for å finne oversikt over elementer\n'stats' for å skrive ut statistikk\n");
      System.out.println("Hvert innskrivningsvindu tar inn ett element.");
      System.out.println("Hovedmeny:\n");
      System.out.println("(1): Lag ny Pasient.\n(2): Lag nytt legemiddel.\n(3): "+
      "Lag ny lege.\n(4): Lag ny resept.\n(5): Bruk resepter.\n(6): Les fra fil.");
      System.out.println("Hva vil du gjoere?");
      String svar = nesteLinje();
      // Pasientlager
      if (svar.equals("1")) {
          lagPasientLinje();
      }
      // Legemiddellager
      else if (svar.equals("2")) {
          lagLegemiddelLinje();
      }
      // Legelager
      else if (svar.equals("3")) {
          lagLegeLinje();
      }
      // reseptLager
      else if (svar.equals("4")) {
          lagReseptLinje();
      }
      // bruk resept
      else if (svar.equals("5")) {
          brukReseptLoekke();
      }
      else if (svar.equals("6")) {
          System.out.print("Skriv filnavn her:\n");
          String fil = nesteLinje();
          lesFraFil(fil);
      }
  }

  // Hovedløkken kjører til q er trykket eller programmet møter på en uventa exception
  public void hovedLoekke() {
      while (true) {
          loekkeInnhold();
      }
  }


  // Tar inn data fra bruker. Returnerer en linje som kan tolkes av funksjonene over
  private void lagPasientLinje() {
      String svar;
      String linje ="";
      while (!sjekkPasientFormat(linje.split(","))) {
          linje = "";
          skrivStandard("Pasient(navn, foedselsnr):\n");
          svar = nesteLinje();
          linje += svar + ",";
          svar = nesteLinje();
          linje += svar;
          if (svar.equals("")) {
              return;
          }
      }
      leggTilPasient(linje);
  }

  // Tar inn data fra bruker. Returnerer en linje som tolkes av funksjonene over
  private void lagLegemiddelLinje() {
      String svar;
      String linje ="";
      while (!sjekkLegemiddelFormat(linje.split(","))) {
          skrivStandard("Hvilken vil du legge til? \n(1) Vanlig\n(2) Narkotisk\n (3) Vanedannende ");
          linje = "";
          svar = nesteLinje();
          if (svar.equals("1")) {
              skrivStandard("Vanlig(String navn, int pris, double virkestoff)");
              for (int i=0; i<3 ; i++) {
                  if (i == 1) {
                      linje += "vanlig,";
                  }
                  svar = nesteLinje();
                  linje += svar + ",";
              }
              leggTilLegemiddel(linje.substring(0, linje.length()-1));
              System.out.println(linje.substring(0, linje.length()-1));
              return;
          }
          else if (svar.equals("2")) {
              skrivStandard("Narkotisk(String navn, int pris, double virkestoff, int narkotiskStyrke)");
              for (int i=0; i<4 ; i++) {
                  if (i == 1) {
                      linje += "narkotisk,";
                  }
                  svar = nesteLinje();
                  linje += svar + ",";
              }
              leggTilLegemiddel(linje.substring(0, linje.length()-1));
          }
          else if (svar.equals("3")) {
              skrivStandard("Vanedannende(String navn, int pris, double virkestoff, int vanedannendeStyrke)");
              for (int i=0; i<4 ; i++) {
                  if (i == 1) {
                      linje += "vanedannende,";
                  }
                  svar = nesteLinje();
                  linje += svar + ",";
              }
              leggTilLegemiddel(linje.substring(0, linje.length()-1));
          }
          else if (svar.equals("")) {
              return;
          }
      }
  }

  // Tar inn data fra bruker og returnerer en linje som tolkes av funksjonene over
  private void lagLegeLinje() {
      String svar;
      String linje = "";
      while (!sjekkLegeFormat(linje.split(","))) {
          linje = "";
          skrivStandard("Lege(String navn, String kontrollID(0 for vanlig lege)):");
          svar = nesteLinje();
          linje += svar + ",";
          svar = nesteLinje();
          linje += svar;
          if (svar.equals("")) {
              return;
          }
      }
      leggTilLege(linje);
  }

  // Tar inn data fra bruker og returnerer en linje som tolkes av funksjonene over
  private void lagReseptLinje() {
      String svar;
      String linje = "";
      while (!sjekkReseptFormat(linje.split(","))) {
          linje = "";
          skrivStandard("Hvilken vil du legge til? \n(1) HvitResept\n(2) BlaaResept\n(3) Militaerresept \n(4) PResept");
          svar = nesteLinje();
          if (svar.equals("1")) {
              skrivStandard("HvitResept(int legemiddelNummer, String legeNavn, int pasientID, int reit)");
              for (int i=0; i<4 ; i++) {
                  if (i == 3) {
                      linje+="hvit,";
                  }
                  svar = nesteLinje();
                  linje += svar + ",";
              }
              nyResept(linje.substring(0, linje.length()-1));
          }
          else if (svar.equals("2")) {
              skrivStandard("BlaaResept(int legemiddelNummer, String legeNavn, int pasientID, int reit)");
              for (int i=0; i<4 ; i++) {
                  if (i == 3) {
                      linje+="blaa,";
                  }
                  svar = nesteLinje();
                  linje += svar + ",";
              }
              nyResept(linje.substring(0, linje.length()-1));
          }
          else if (svar.equals("3")) {
              skrivStandard("Militaerresept(int legemiddelNummer, String legeNavn, int pasientID, int reit)");
              for (int i=0; i<4 ; i++) {
                  if (i == 3) {
                      linje+="millitaer,";
                  }
                  svar = nesteLinje();
                  linje += svar + ",";
              }
              nyResept(linje.substring(0, linje.length()-1));
          }
          else if (svar.equals("4")) {
              skrivStandard("PResept(int legemiddelNummer, String legeNavn, int pasientID)");
              for (int i=0; i<3 ; i++) {
                  svar = nesteLinje();
                  linje += svar + ",";
                  linje += "p"+",";
              }
              nyResept(linje.substring(0, linje.length()-1));
          }
          else if (svar.equals("")) {
              return;
          }
      }
  }

  // Returnerer en liste av alle reseptene.
  private Koe<Resept> reseptListeLager() {
      Koe<Resept> reseptListe = new Koe<Resept>();
      for (int i = 0 ; i < pasientListe.stoerrelse() ; i++ ) {
          for (int j = 0 ; j < pasientListe.hent().hentResepter().stoerrelse() ; j++ ) {
              reseptListe.leggTil(pasientListe.hent().hentResepter().hent());
          }
      }
      return reseptListe;
  }

  private void fiksLegemiddelNummere() {
      // Itererer gjennom legemidler, finner ut om legemiddelet er det samme, setter id til legemiddelet lik den plassen den tilhører
      Koe<Resept> reseptListe = reseptListeLager();
      for (int j = 0 ; j < legemiddelListe.stoerrelse() ; j++) {
          for (int i = 0 ; i < reseptListe.stoerrelse() ; i++) {
              if (reseptListe.hent().hentLegemiddel().hentNavn().equals(legemiddelListe.hent().hentNavn())) {
                  legemiddelListe.hent().settLegemiddelId(j++);
                  reseptListe.hent().hentLegemiddel().settLegemiddelId(j++);
              }
          }
      }
    Koe<Lege> inf = new Koe<Lege>();
        for (Lege lege : legeListe) {
            inf.leggTil(lege);
        }
        legeListe = inf;
  }

  // En løkke for å bruke reseptene.
  private void brukReseptLoekke() {
      // kjører til bruker, returnerer blank, q eller b
      while (true) {
          System.out.println("Pasienter:");
          // Skriver ut en penStreng
          System.out.println(pasientListe.penStreng());
          System.out.println("Hvem skal bruke Resepten?(pasientId)");
          String svar = nesteLinje();
          if (svar.equals("")) {
              return;
          }
          int a = -1;
          try {
              a = Integer.parseInt(svar);
          } catch (NumberFormatException e) {
                System.out.println("dette er ikke et tall");continue;
            }
          // Itererer over pasienter og finner den med riktig id
          for (Pasient p : pasientListe) {
              if (p.hentId()==a) {
                  while (true) {
                      System.out.println("Resepter:");
                      System.out.println(p.hentResepter().penStreng());
                      System.out.println("Hvilken resept skal du bruke?");

                      svar = nesteLinje();
                      if (svar.equals("")) {
                          return;
                      }
                      a = -1;
                      try {
                          a = Integer.parseInt(svar);
                      } catch (NumberFormatException e) {
                            System.out.println("dette er ikke et tall");continue;
                        }
                      int i = 0;
                      // Itererer over resepter i p sin stabel. Sjekker om teller i er lik resept-nummeret vi ba om.
                      for (Resept r : p.hentResepter()) {
                          if (i == a) {
                              if (r.bruk()) {
                                  System.out.println("Resept brukt. " + r.hentReit() + " reit igjen");
                              }
                              else {
                                  System.out.println("Ingen flere reit igjen");
                              }
                          }
                          i++;
                      }
                  }
              }
          }
      }
  }

  // Avslutter programmet
  private void avslutt(int status) {
      System.out.println("hadet fint");
      innDataLeser.close();
      System.exit(status);
  }

  private String nesteLinje() {
      String svar;
      System.out.print(">>> ");
      svar = innDataLeser.nextLine();
      if (svar.equals("q")) {
          innDataLeser.close();
          avslutt(0);
      }

      if (svar.equals("b")) {
          // fortsetter lokken ved å kalle på innholdet i hovedmenyen
          System.out.println("Returner til hovedmeny...");
          loekkeInnhold();
      }

      // Kaller på hjelpemetoden
      if (svar.equals("hjelp")) {
          hjelpMetode();
          // Returnerer nestelinje for å kunne fortsette den eventuelle loekken.
          return nesteLinje();
      }
      // Kaller på statistikkmetoden
      if (svar.equals("stats")) {
          statistikk();
          return nesteLinje();
      }
      return svar;
  }

  // funksjonen tar inn en streng og skriver den ut sammen med standardmenyen
  private void skrivStandard(String streng) {
      System.out.println("'q' for å avslutte.\n'b' for tilbake til hovedmeny."+
      "\n'hjelp' for å finne oversikt over elementer\n'stats' for å skrive ut statistikk\n");
      System.out.println(streng+"\n");
  }

  // skriver ut statistikk over utskrevede narkotiske og vanedannende legemidler.
  private void statistikk() {
      int antVaneRes = 0;
      int antNarkoRes = 0;
      // Denne blir sortert da legeListe allerede er sortert.
      Koe<String[]> listeNarkoLeger = new Koe<String[]>();
      Koe<String[]> listeNarkoPasienter = new Koe<String[]>();
      // Itererer over alle legene i legeliste, finner ut hvilke som har resepter på narkotiske legemidler

      for (Lege lege : legeListe) {
          int antNarkoLege = 0;
          for (Resept r : lege.hentUtskrevedeResepter()) {
              if (r.hentLegemiddel() instanceof Vanedannende) {
                  antVaneRes += 1;
              }
              else if (r.hentLegemiddel() instanceof Narkotisk) {
                  antNarkoRes += 1;
              }
          }
          String[] inf = {lege.hentNavn(), "" + antNarkoLege};
          listeNarkoLeger.leggTil(inf);
      }
      // Itererer over alle pasientene i pasientListe, finner ut hvilke som har resepter på narkotiske legemidler.
      for (Pasient p : pasientListe) {
          int narkotiskReseptPasient = 0;
          for (Resept re : p.hentResepter()) {
              if (re.hentLegemiddel() instanceof Narkotisk) {
                  narkotiskReseptPasient++;
              }
          }
          // array for å legge til i lenkelisten som printes ut.
          String[] inf = {p.hentNavn(), "" + narkotiskReseptPasient};
          listeNarkoPasienter.leggTil(inf);
      }
      // Skriver ut antallet vanedannende resepter og antallet narkotiske resepter
      System.out.println("Antall resepter på vanedannende legemidler: " + antVaneRes + "\n" +
      "Antall resepter på narkotiske legemidler: " + antNarkoRes + "\n");
      // skriver ut hvilke leger som har skrevet ut narkotiske legemidler
      System.out.println("Disse har skrevet ut narkotiske legemidler:");
      for (String[] s : listeNarkoLeger) {
          if (Integer.parseInt(s[1]) != 0) {
              System.out.println(s[0] + " Antall: "+s[1]);
          }
      }
      // Skriver ut pasientene som har narkotiske resepter og antallet resepter
      System.out.println("Disse har mottatt narkotiske resepter:");
      for (String[] s : listeNarkoPasienter) {
          if (Integer.parseInt(s[1]) != 0) {
              System.out.println(s[0] + " Antall: " + s[1]);
          }
      }
  }


  // Forskjellige printe-ut-funksjoner for å hjelpe underveis.
  private void hjelpMetode() {
      skrivStandard("(1) for å skrive ut legemidler.\n(2) for å skrive ut leger.\n(3) for aa skrive ut pasienter.\n(4) for aa skrive ut resepter \n(5) for aa skrive alle 3.\n (ENTER for aa returnere)");
      String svar = nesteLinje();

      if (svar.equals("1")) {
          // PenStreng returnerer en litt mer lesbar streng enn standarden.
          for (Legemiddel legemiddel : legemiddelListe) {
              System.out.println(legemiddel.hentId() + " " + legemiddel.toString());
          }
      }
      else if (svar.equals("2")) {
          System.out.println(legeListe.penStreng());
      }
      else if (svar.equals("3")) {
          for (Pasient p : pasientListe) {
              System.out.println(p.hentId() + " " + p.toString());
          }
      }
      // spør hvilke resepter bruker vil se.
      else if (svar.equals("4")){
          for (Pasient p : pasientListe) {
              System.out.println(p.hentId() + " " + p.toString());
          }
          System.out.println("Hvilken pasient sine resepter vil du se? (skriv inn Id her)");
          svar = nesteLinje();
          // Sjekker at bruker har skrevet inn en integer
          try {
              for (Pasient p : pasientListe) {
                  int a = Integer.parseInt(svar);
                  if (p.hentId() == a) {
                      System.out.println(p.hentResepter().penStreng());
                      return;
                  }
              }
          // Fanger eventuelle numberformatexception-feil og returnerer
          } catch (NumberFormatException e) {
                System.out.println("Det er ikke et tall!!");
                return;
            }
          System.out.println("Pasienten finnes ikke.");
          // Skriver ut en grei representasjon av alle elementene i systemet.
      }
      else if (svar.equals("5")) {
          System.out.println("Legemidler:\n");
          System.out.println(legemiddelListe.penStreng());
          System.out.println("Leger: \n");
          System.out.println(legeListe.penStreng());
          System.out.println("Pasienter:\n");
          System.out.println(pasientListe.penStreng());
      }
  }
}
