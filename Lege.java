
public class Lege implements Comparable <Lege> {

    String navn;
    Koe<Resept> utskrevedeResepter = new Koe<>();

    public Lege(String navn) {
        this.navn = navn;
    }

    public Koe<Resept> hentUtskrevedeResepter() {
        return utskrevedeResepter;
    }

    public String hentNavn() {
        return navn;
    }

    public String toString() {
       return "Legens navn er " + navn + ".";
   }



   public HvitResept skrivHvitResept(Legemiddel legemiddel, Pasient pasient, int reit) throws
   UlovligUtskrift{
        //oppretter hvitResept
        HvitResept hvitResept = new HvitResept(legemiddel, this, pasient, reit);
        //legger til i listen
        utskrevedeResepter.leggTil(hvitResept);
        //legger til hos pasienten
        pasient.nyResept(hvitResept);
        //returnerer
        return hvitResept;
  }

  public MilResept skrivMilResept(Legemiddel legemiddel, Pasient pasient) throws
  UlovligUtskrift{
        //oppretter militaerresept
        MilResept militaerresept = new MilResept(legemiddel, this, pasient);
        //legger til i listen
        utskrevedeResepter.leggTil(militaerresept);
        //legger til hos pasienten
        pasient.nyResept(militaerresept);
        //returnerer
        return militaerresept;
  }

  public PResept skrivPResept(Legemiddel legemiddel, Pasient pasient, int reit) throws
  UlovligUtskrift{
        //oppretter pResept
        PResept pResept = new PResept(legemiddel, this, pasient, reit);
        //legger til i listen
        utskrevedeResepter.leggTil(pResept);
        //legger til hos pasienten
        pasient.nyResept(pResept);
        //returnerer
        return pResept;
  }

  public BlaResept skrivBlaResept(Legemiddel legemiddel, Pasient pasient, int reit) throws
  UlovligUtskrift{
      //oppretter blaaResept
      BlaResept blaResept = new BlaResept(legemiddel, this, pasient, reit);
      //legger til i listen
      utskrevedeResepter.leggTil(blaResept);
      //legger til hos pasienten
      pasient.nyResept(blaResept);
      //returnerer
      return blaResept;
  }

   @Override
   public int compareTo(Lege l) {
      return navn.toLowerCase().compareTo(l.hentNavn().toLowerCase());
   }

}
