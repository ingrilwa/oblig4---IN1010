
public class MilResept extends HvitResept {

    public MilResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient){
        super(legemiddel, utskrivendeLege, pasient, 3);
    }

    public int prisAaBetale(int pris){
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " Type resept: MilitærResept, rabatt: 100%.";
    }
}
