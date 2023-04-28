
public abstract class Legemiddel {

    String navn;
    int pris;
    double virkestoff;
    int id;
    static int teller = 1;

    public Legemiddel(String navn, int pris, double virkestoff){
        this.navn = navn;
        this.pris = pris;
        this.virkestoff = virkestoff;
        id = teller;
        teller += 1;
    }

    public int hentId(){
        return id;
    }

    public String hentNavn(){
        return navn;
    }

    public int hentPris(){
        return pris;
    }

    public double hentVirkestoff(){
        return virkestoff;
    }

    public void settNyPris(int nyPris){
        pris = nyPris;
    }

    public void settLegemiddelId(int nyId) {
        id = nyId;
    }

    @Override
    public String toString() {
        return "Informasjon om Legemiddel. ID: " + id + ", navn: " + navn + ", pris: " + pris + " kr, virkestoff: " + virkestoff + " mg.";
    }

}
