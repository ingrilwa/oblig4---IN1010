class IndeksertListe <T> extends Lenkeliste <T> {

      public void leggTil(int pos, T x) throws UgyldigListeindeks {
          Node ny = new Node(x);
          Node peker = peker(pos); //henter peker til posisjon
          if (pos < 0) { //hvis ikke gyldig posisjon
              throw new UgyldigListeindeks(pos);
          }
          else if (pos == stoerrelse) { //hvis posisjon er sist i lista
              leggTil(x);
              return;
          }
          else if (pos == 0) { //hvis posisjon er starten av lista
              ny.neste = fremst;
              fremst.forrige = ny;
              fremst = ny;
          }
          else {
              ny.neste = peker;
              ny.forrige = peker.forrige;
              peker.forrige.neste = ny;
              peker.forrige = ny;
          }
          stoerrelse += 1;
      }


      public void sett(int pos, T x) throws UgyldigListeindeks{
          Node ny = new Node(x);
          Node peker = peker(pos); //henter peker til posisjon
          if (pos > stoerrelse - 1 || pos < 0) { //hvis ikke gyldig indeks
              throw new UgyldigListeindeks(pos);
          }
          else if (stoerrelse == 1) { //hvis lista kun har ett element
              fremst = ny;
              sist = ny;
          }
          else if (peker.neste == null) { //hvis ingen element bak
              ny.forrige = sist.forrige;
              sist.forrige.neste = ny;
              sist = ny;
          }
          else if (peker.forrige == null) { //hvis ingen element foran
              ny.neste = fremst.neste;
              fremst.neste.forrige = ny;
              fremst = ny;
          }
          else {
              ny.neste = peker.neste;
              ny.forrige = peker.forrige;
              peker.forrige.neste = ny;
              peker.neste.forrige = ny;
          }
      }


      public T hent (int pos) throws UgyldigListeindeks {
          if (pos > stoerrelse - 1 || pos < 0) { //hvis ikke gyldig posisjon
              throw new UgyldigListeindeks(pos);
          }
          Node peker = peker(pos); //henter peker til posisjon
          return peker.data;
      }

      public T fjern (int pos) throws UgyldigListeindeks {
          Node peker = peker(pos); //henter peker til posisjon
          if (pos > (stoerrelse - 1) || pos < 0 || stoerrelse == 0)  { //hvis ikke gyldig posisjon
              throw new UgyldigListeindeks(pos);
          }
          else if (stoerrelse == 1) { //hvis det kun er et element i lista
              fremst = null;
              sist = null;
          }
          else if (peker.neste == null) { //hvis ingen element bak
              sist.forrige.neste = null;
              sist = sist.forrige;
          }
          else if (peker.forrige == null) { //hvis ingen element foran
              fremst.neste.forrige = null;
              fremst = fremst.neste;
          }
          else {
              peker.neste.forrige = peker.forrige;
              peker.forrige.neste = peker.neste;
          }
          stoerrelse -= 1;
          return peker.data;
      }

      public Node peker(int pos) {
          if (pos < 0) { //hvis ikke gyldig posisjon
              return null;
          }
          else if (pos == 0) {
              return fremst;
          }
          else if (pos == stoerrelse) {
              return sist;
          }
          else {
              Node peker = fremst;
              for (int i = 0; i < pos; i ++) {
                  peker = peker.neste;
              }
              return peker;
          }
        }

}
