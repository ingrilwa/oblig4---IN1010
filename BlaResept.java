
public class BlaResept extends Resept {

    String farge = "Blå";

    public BlaResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient, int reit){
        super(legemiddel, utskrivendeLege, pasient, reit);
    }

    @Override
    public String farge(){
        return farge;
    }

    @Override
    public int prisAaBetale(int pris){
        return (int)(pris*0.25);
    }

    @Override
    public String toString() {
        return super.toString() + " Farge: " + farge + ", rabatt: 75%.";
    }



}
