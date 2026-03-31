package go;


public class Facebook {

   private int id;
   private int veranstaltungID;
   private int veranstaltungKategorie;
   private String postTyp;
   private String postText;
   private String fbMessageID;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getVeranstaltungKategorie() {
      return this.veranstaltungKategorie;
   }

   public void setVeranstaltungKategorie(int veranstaltungKategorie) {
      this.veranstaltungKategorie = veranstaltungKategorie;
   }

   public String getFbMessageID() {
      return this.fbMessageID;
   }

   public void setFbMessageID(String fbMessageID) {
      this.fbMessageID = fbMessageID;
   }

   public String getPostTyp() {
      return this.postTyp;
   }

   public void setPostTyp(String postTyp) {
      this.postTyp = postTyp;
   }

   public String getPostText() {
      return this.postText;
   }

   public void setPostText(String postText) {
      this.postText = postText;
   }
}
