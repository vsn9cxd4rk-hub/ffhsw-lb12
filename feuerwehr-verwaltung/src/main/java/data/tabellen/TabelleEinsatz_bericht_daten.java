/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.EinsatzBerichtDaten;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleEinsatz_bericht_daten {
    public void insert(EinsatzBerichtDaten berichtDaten) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO einsatz_bericht_daten (`id`, `veranstaltungID`, `einsatzID`, `jahr`, `einsatzArt`, `stelle`, `objekt`, `eigentuemerName`, `eigentuemerAnschrift`, `eigentuemerTelefon`, `verursacherName`, `verursacherAnschrift`, `verursacherTelefon`, `alamierung`, `meldenderName`, `meldenderAnschrift`, `meldenderTelefon`, `lage`, `verlauf`, `eingesetzteGeraete`, `verbrauchWasser`, `verbrauchSchaum`, `verbrauchPulver`, `verbrauchBindemittel`, `vorEintreffenGeloescht`, `schnellangriff`, `crohr`, `brohr`, `kleinloeschgeraet`, `tragbareLeitern`, `atemschutzgeraet`, `fluchthauben`, `belueftungsgeraet`, `rettungsgeraet`, `ausdehnung`, `entstehungsursache`, `verletzte`, `gerettete`, `tote`, `schadenhoehe`, `brandwacheFahrzeug`, `staerke`, `dauer`, `mandantID`) VALUES ('" + berichtDaten.getId() + "', '" + berichtDaten.getVeranstaltungID() + "', '" + berichtDaten.getEinsatzID() + "', '" + berichtDaten.getJahr() + "', '" + berichtDaten.getEinsatzArt() + "', '" + berichtDaten.getStelle() + "', '" + berichtDaten.getObjekt() + "', '" + berichtDaten.getEigentuemerName() + "', '" + berichtDaten.getEigentuemerAnschrift() + "', '" + berichtDaten.getEigentuemerTelefon() + "', '" + berichtDaten.getVerursacherName() + "', '" + berichtDaten.getVerursacherAnschrift() + "', '" + berichtDaten.getVerursacherTelefon() + "', '" + berichtDaten.getAlamierung() + "', '" + berichtDaten.getMeldenderName() + "', '" + berichtDaten.getMeldenderAnschrift() + "', '" + berichtDaten.getMeldenderTelefon() + "', '" + berichtDaten.getLage() + "', '" + berichtDaten.getVerlauf() + "', '" + berichtDaten.getEingesetzteGeraete() + "', '" + berichtDaten.getVerbrauchWasser() + "', '" + berichtDaten.getVerbrauchSchaum() + "', '" + berichtDaten.getVerbrauchPulver() + "', '" + berichtDaten.getVerbrauchBindemittel() + "', '" + berichtDaten.getVorEintreffenGeloescht() + "', '" + berichtDaten.getSchnellangriff() + "', '" + berichtDaten.getcRohr() + "', '" + berichtDaten.getbRohr() + "', '" + berichtDaten.getKleinLoeschgeraet() + "', '" + berichtDaten.getTragbareLeitern() + "', '" + berichtDaten.getAtemschutzgeraet() + "', '" + berichtDaten.getFluchthauben() + "', '" + berichtDaten.getBelueftungsgeraet() + "', '" + berichtDaten.getRettungsgeraet() + "', '" + berichtDaten.getAusdehnung() + "', '" + berichtDaten.getEntstehungsursache() + "', '" + berichtDaten.getVerletzte() + "', '" + berichtDaten.getGerettete() + "', '" + berichtDaten.getTote() + "', '" + berichtDaten.getSchadenhoehe() + "', '" + berichtDaten.getBrandwacheFahrzeug() + "', '" + berichtDaten.getStaerke() + "', '" + berichtDaten.getDauer() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(EinsatzBerichtDaten berichtDaten) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update einsatz_bericht_daten set einsatzArt = '" + berichtDaten.getEinsatzArt() + "', stelle = '" + berichtDaten.getStelle() + "', objekt = '" + berichtDaten.getObjekt() + "', eigentuemerName = '" + berichtDaten.getEigentuemerName() + "', eigentuemerAnschrift = '" + berichtDaten.getEigentuemerAnschrift() + "', eigentuemerTelefon = '" + berichtDaten.getEigentuemerTelefon() + "', verursacherName = '" + berichtDaten.getVerursacherName() + "', verursacherAnschrift = '" + berichtDaten.getVerursacherAnschrift() + "', verursacherTelefon = '" + berichtDaten.getVerursacherTelefon() + "', alamierung = '" + berichtDaten.getAlamierung() + "', meldenderName = '" + berichtDaten.getMeldenderName() + "', meldenderAnschrift = '" + berichtDaten.getMeldenderAnschrift() + "', meldenderTelefon = '" + berichtDaten.getMeldenderTelefon() + "', lage = '" + berichtDaten.getLage() + "', verlauf = '" + berichtDaten.getVerlauf() + "', eingesetzteGeraete = '" + berichtDaten.getEingesetzteGeraete() + "', verbrauchWasser = '" + berichtDaten.getVerbrauchWasser() + "', verbrauchSchaum = '" + berichtDaten.getVerbrauchSchaum() + "', verbrauchPulver = '" + berichtDaten.getVerbrauchPulver() + "', verbrauchBindemittel = '" + berichtDaten.getVerbrauchBindemittel() + "', vorEintreffenGeloescht = '" + berichtDaten.getVorEintreffenGeloescht() + "', schnellangriff = '" + berichtDaten.getSchnellangriff() + "', cRohr = '" + berichtDaten.getcRohr() + "', bRohr = '" + berichtDaten.getbRohr() + "', kleinloeschgeraet = '" + berichtDaten.getKleinLoeschgeraet() + "', tragbareLeitern = '" + berichtDaten.getTragbareLeitern() + "', atemschutzgeraet = '" + berichtDaten.getAtemschutzgeraet() + "', fluchthauben = '" + berichtDaten.getFluchthauben() + "', belueftungsgeraet = '" + berichtDaten.getBelueftungsgeraet() + "', rettungsgeraet = '" + berichtDaten.getRettungsgeraet() + "', ausdehnung = '" + berichtDaten.getAusdehnung() + "', entstehungsursache = '" + berichtDaten.getEntstehungsursache() + "', verletzte = '" + berichtDaten.getVerletzte() + "', gerettete = '" + berichtDaten.getGerettete() + "', tote = '" + berichtDaten.getTote() + "', schadenhoehe = '" + berichtDaten.getSchadenhoehe() + "', brandwacheFahrzeug = '" + berichtDaten.getBrandwacheFahrzeug() + "', staerke = '" + berichtDaten.getStaerke() + "', dauer = '" + berichtDaten.getDauer() + "' where veranstaltungID = " + berichtDaten.getVeranstaltungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandatID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from einsatz_bericht_daten where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public EinsatzBerichtDaten getBerichtData(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT * FROM einsatz_bericht_daten where veranstaltungId = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT * FROM einsatz_bericht_daten where veranstaltungId = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        EinsatzBerichtDaten daten = new EinsatzBerichtDaten();
        if (result.next()) {
            daten.setId(result.getInt("id"));
            daten.setVeranstaltungID(result.getInt("veranstaltungID"));
            daten.setEinsatzID(result.getInt("einsatzID"));
            daten.setJahr(result.getInt("jahr"));
            daten.setEinsatzArt(result.getInt("einsatzArt"));
            daten.setStelle(result.getInt("stelle"));
            daten.setObjekt(result.getInt("objekt"));
            daten.setEigentuemerName(result.getString("eigentuemerName"));
            daten.setEigentuemerAnschrift(result.getString("eigentuemerAnschrift"));
            daten.setEigentuemerTelefon(result.getString("eigentuemerTelefon"));
            daten.setVerursacherName(result.getString("VerursacherName"));
            daten.setVerursacherAnschrift(result.getString("VerursacherAnschrift"));
            daten.setVerursacherTelefon(result.getString("VerursacherTelefon"));
            daten.setAlamierung(result.getInt("alamierung"));
            daten.setMeldenderName(result.getString("MeldenderName"));
            daten.setMeldenderAnschrift(result.getString("MeldenderAnschrift"));
            daten.setMeldenderTelefon(result.getString("MeldenderTelefon"));
            daten.setLage(result.getString("lage"));
            daten.setVerlauf(result.getString("verlauf"));
            daten.setEingesetzteGeraete(result.getString("eingesetzteGeraete"));
            daten.setVerbrauchWasser(result.getString("verbrauchWasser"));
            daten.setVerbrauchSchaum(result.getString("verbrauchSchaum"));
            daten.setVerbrauchPulver(result.getString("verbrauchPulver"));
            daten.setVerbrauchBindemittel(result.getString("verbrauchBindemittel"));
            daten.setVorEintreffenGeloescht(result.getInt("vorEintreffenGeloescht"));
            daten.setSchnellangriff(result.getInt("schnellangriff"));
            daten.setcRohr(result.getString("crohr"));
            daten.setbRohr(result.getString("brohr"));
            daten.setKleinLoeschgeraet(result.getString("kleinloeschgeraet"));
            daten.setTragbareLeitern(result.getInt("tragbareLeitern"));
            daten.setAtemschutzgeraet(result.getString("atemschutzgeraet"));
            daten.setFluchthauben(result.getString("fluchthauben"));
            daten.setBelueftungsgeraet(result.getString("belueftungsgeraet"));
            daten.setRettungsgeraet(result.getInt("rettungsgeraet"));
            daten.setAusdehnung(result.getInt("ausdehnung"));
            daten.setEntstehungsursache(result.getString("entstehungsursache"));
            daten.setVerletzte(result.getString("verletzte"));
            daten.setGerettete(result.getString("gerettete"));
            daten.setTote(result.getString("tote"));
            daten.setSchadenhoehe(result.getString("schadenhoehe"));
            daten.setBrandwacheFahrzeug(result.getInt("brandwacheFahrzeug"));
            daten.setStaerke(result.getString("staerke"));
            daten.setDauer(result.getString("dauer"));
            return daten;
        }
        return null;
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM einsatz_bericht_daten;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM einsatz_bericht_daten;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCountByVerasnatltungID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM einsatz_bericht_daten where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM einsatz_bericht_daten where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

