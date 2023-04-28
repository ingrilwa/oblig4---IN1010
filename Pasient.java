class Pasient {
    String navn;
    String foedselsnummer;
    int id;

    static int id_teller = 1;
    Stabel<Resept> resepter = new Stabel<>();

    public Pasient(String navn, String foedselsnummer) {
        this.navn = navn;
        this.foedselsnummer = foedselsnummer;
        id = id_teller;
        id_teller += 1;
    }


    public void nyResept(Resept resept) {
        resepter.leggTil(resept);
    }

    public Stabel<Resept> hentResepter() {
        return resepter;
    }

    public int hentId() {
        return id;
    }

    public String hentNavn() {
        return navn;
    }

    public String hentFoedselsnummer() {
        return foedselsnummer;
    }

    public String toString() {
        return "Pasient med navn: " + navn + ", foedselsnummer: " + foedselsnummer + ", id: " + id + " og reseptene: " + resepter;
    }

}
