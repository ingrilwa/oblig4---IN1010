class Prioritetskoe <T extends Comparable <T>> extends Lenkeliste <T> {

    //legger til elementer i prioritert rekkefølge, minst nærmest 0, høyest lengst fra 0
    @Override
    public void leggTil ( T x ){
        leggTilPrioritert(x);
     }

    public void leggTilPrioritert(T x){
        Node ny = new Node(x);
        Node valgt = fremst;

        if (erListaTom()){
            fremst = ny;
            sist = ny;
            stoerrelse++;
            return;
        }
        if (valgt.data.compareTo(ny.data) >= 0){
            ny.neste = valgt;
            valgt.forrige = ny;
            fremst = ny;
            stoerrelse++;
            return;
        }
        while (valgt != null){
         // Siste node
            if(valgt.neste == null){
                valgt.neste = ny;
                ny.forrige = valgt;
                sist = ny;
                stoerrelse++;
                return;
           //Setter mellom
            }
            else if(valgt.neste.data.compareTo(ny.data) > 0){
                ny.forrige = valgt;
                ny.neste = valgt.neste;
                valgt.neste.forrige = ny;
                valgt.neste = ny;
                stoerrelse++;
                return;
            }
            else {
                valgt = valgt.neste;
            }
        }
    }


    @Override
    public T hent() {
        return super.hent();
    }

    @Override
    //fjerner det elementet med minst prioritet, dvs det første elementet
    public T fjern() {
        return super.fjern();
    }


}
