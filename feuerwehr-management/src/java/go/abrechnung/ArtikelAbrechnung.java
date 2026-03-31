package go.abrechnung;


public class ArtikelAbrechnung {

   private int id;
   private String name;
   private int klasse;
   private int buchungskonto;
   private int zahlungsart;
   private int wert;
   private int rabattwert;
   private int mwst;
   private int berechnungsart;
   private int berechnungsart2;
   private int rabattart;
   private int aktiv;
   private String von;
   private String bis;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getKlasse() {
      return this.klasse;
   }

   public void setKlasse(int klasse) {
      this.klasse = klasse;
   }

   public int getWert() {
      return this.wert;
   }

   public void setWert(int wert) {
      this.wert = wert;
   }

   public int getBerechnungsart() {
      return this.berechnungsart;
   }

   public void setBerechnungsart(int berechnungsart) {
      this.berechnungsart = berechnungsart;
   }

   public int getAktiv() {
      return this.aktiv;
   }

   public void setAktiv(int aktiv) {
      this.aktiv = aktiv;
   }

   public int getBerechnungsart2() {
      return this.berechnungsart2;
   }

   public void setBerechnungsart2(int berechnungsart2) {
      this.berechnungsart2 = berechnungsart2;
   }

   public int getBuchungskonto() {
      return this.buchungskonto;
   }

   public void setBuchungskonto(int buchungskonto) {
      this.buchungskonto = buchungskonto;
   }

   public int getZahlungsart() {
      return this.zahlungsart;
   }

   public void setZahlungsart(int zahlungsart) {
      this.zahlungsart = zahlungsart;
   }

   public int getRabattwert() {
      return this.rabattwert;
   }

   public void setRabattwert(int rabattwert) {
      this.rabattwert = rabattwert;
   }

   public int getRabattart() {
      return this.rabattart;
   }

   public void setRabattart(int rabattart) {
      this.rabattart = rabattart;
   }

   public int getMwst() {
      return this.mwst;
   }

   public void setMwst(int mwst) {
      this.mwst = mwst;
   }

   public String getVon() {
      return this.von;
   }

   public void setVon(String von) {
      this.von = von;
   }

   public String getBis() {
      return this.bis;
   }

   public void setBis(String bis) {
      this.bis = bis;
   }
}
