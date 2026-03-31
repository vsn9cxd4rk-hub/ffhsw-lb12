package service;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import run.runApplication;

public class DirectionParser {

   private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");


   public static String DirectionParsers(String locationToGetTo) throws IOException, DocumentException {
      DirectionsRoute[] routes = getRouteList(locationToGetTo);
      String gpsKoordinaten = parseRouteStepsIntoString(routes);
      return gpsKoordinaten;
   }

   private static DirectionsRoute[] getRouteList(String location) {
      GeoApiContext context = (new GeoApiContext()).setApiKey((String)runApplication.EINSTELLUNGEN.get("google_api_code"));
      DirectionsApiRequest request = DirectionsApi.getDirections(context, (String)runApplication.EINSTELLUNGEN.get("default_location"), location);
      request.language("de");
      DirectionsRoute[] routes = (DirectionsRoute[])request.awaitIgnoreError();
      return routes;
   }

   private static String parseRouteStepsIntoString(DirectionsRoute[] routes) {
      StringBuilder sb_html = new StringBuilder();
      StringBuilder sb_pdf = new StringBuilder();
      String gpsKoordinatenZiel = null;
      int count = routes[0].legs[0].steps.length;
      DirectionsStep[] var8 = routes[0].legs[0].steps;
      int var7 = routes[0].legs[0].steps.length;

      for(int var6 = 0; var6 < var7; ++var6) {
         DirectionsStep step = var8[var6];
         if(count == routes[0].legs[0].steps.length) {
            gpsKoordinatenZiel = step.endLocation.toString();
         } else if(count == 1) {
            gpsKoordinatenZiel = step.endLocation.toString();
         } else if(count < routes[0].legs[0].steps.length) {
            gpsKoordinatenZiel = step.endLocation.toString();
            if(count == 0) {
               sb_html.append("<br>");
               sb_pdf.append("\n");
            }
         }

         --count;
      }

      return gpsKoordinatenZiel;
   }

   public static String removeTags(String string) {
      if(string != null && string.length() != 0) {
         if(string.contains("<div style=")) {
            string = string.replace("<div style=", ". <div style=");
         }

         Matcher m = REMOVE_TAGS.matcher(string);
         return m.replaceAll("");
      } else {
         return string;
      }
   }
}
