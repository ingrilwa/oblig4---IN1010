
public class HvitResept extends Resept {

    String farge = "Hvit";

    public HvitResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient, int reit){
        super(legemiddel, utskrivendeLege, pasient, reit);
    }

    @Override
    public String farge(){
        return farge;
    }

    @Override
    public int prisAaBetale(int pris){
        return pris;
    }

    @Override
    public String toString() {
        return super.toString() + " Farge: " + farge + ".";
    }


}
