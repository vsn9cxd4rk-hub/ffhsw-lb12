package utilities;


public class MoneyCalculation {

   public static int parseMoneyVauleForDatabase(String betrag) {
      int komma = betrag.indexOf(",");
      String vorKomma = betrag.substring(0, komma);
      String nachKomma = betrag.substring(komma + 1, betrag.length());
      return Integer.parseInt(vorKomma + nachKomma);
   }

   public static String parseMoneyVauleForGUI(int betrag) {
      String neuerBetrag = Integer.toString(betrag);
      StringBuilder build = new StringBuilder();
      if(neuerBetrag.length() >= 3) {
         for(int i = 0; i < neuerBetrag.length(); ++i) {
            build.append(neuerBetrag.subSequence(i, i + 1));
            if(neuerBetrag.length() - i - 3 == 0) {
               build.append(",");
            }
         }
      } else if(neuerBetrag.length() >= 2) {
         build.append("0,");
         build.append(neuerBetrag);
      } else {
         build.append("0,0");
         build.append(neuerBetrag);
      }

      return build.toString();
   }
}
