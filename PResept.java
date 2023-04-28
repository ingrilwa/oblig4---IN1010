
public class PResept extends HvitResept {

    public PResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient, int reit){
        super(legemiddel, utskrivendeLege, pasient, reit);
    }

    public int prisAaBetale(int pris){
        pris -= 108;
        if (pris <= 0){
            return 0;
        }
        else {
            return pris;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " Type resept: P-Resept, rabatt: 108kr.";
    }
}
