class Koe <T> extends Lenkeliste <T> {

  @Override
  public T fjern() {
      Node node = fremst;

      if (erListaTom()){
          throw new UgyldigListeindeks(-1);
      }
      fremst = fremst.neste;
      if (fremst != null){
          fremst.forrige = null;
      }
      stoerrelse--;
      return node.data;
  }

}
