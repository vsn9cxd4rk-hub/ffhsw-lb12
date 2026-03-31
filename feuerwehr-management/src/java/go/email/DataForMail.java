package go.email;


public class DataForMail {

   private int status;
   private String an;
   private String cc;
   private String bcc;
   private String betreff;
   private String anhang;
   private String eMailText;


   public String getAn() {
      return this.an;
   }

   public void setAn(String an) {
      this.an = an;
   }

   public String getCc() {
      return this.cc;
   }

   public void setCc(String cc) {
      this.cc = cc;
   }

   public String getBcc() {
      return this.bcc;
   }

   public void setBcc(String bcc) {
      this.bcc = bcc;
   }

   public String getBetreff() {
      return this.betreff;
   }

   public void setBetreff(String betreff) {
      this.betreff = betreff;
   }

   public String getAnhang() {
      return this.anhang;
   }

   public void setAnhang(String anhang) {
      this.anhang = anhang;
   }

   public String geteMailText() {
      return this.eMailText;
   }

   public void seteMailText(String eMailText) {
      this.eMailText = eMailText;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }
}
