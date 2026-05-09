/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.hash
 */
package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import go.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import logging.logging;
import run.runApplication;
import utilities.hash;

public class TabelleUser {
    public List<User> getAll() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("Select count(*) from User where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("Select count(*) from User where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<User> liste = new ArrayList<User>();
        while (result.next()) {
            liste.add(this.mapResultSetToUser(result));
        }
        return liste;
    }

    public int getRechte(String id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("Select admin from user where userid ='" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("Select admin from user where userid ='" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getUserGruppe(String id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("Select usergruppe from user where userid ='" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("Select usergruppe from user where userid ='" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public User get(String id) throws SQLException {
        String sql;
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery(sql = "SELECT * FROM user WHERE UserId = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return this.mapResultSetToUser(result);
        }
        return null;
    }

    public void insert(User user) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO `user`(`UserId`, `Passwort`, `usergruppe`, `Admin`, `deaktiv`, `loeschkenner` , `mandantID`) VALUES ('" + user.getUser() + "','" + user.getPasswort() + "','" + user.getUsergruppe() + "','" + user.getAdmin() + "','" + user.getDeaktiv() + "','" + user.getLoeschkenner() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void updatepasswort(String id, String passwort) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "UPDATE `user`SET passwort = '" + passwort + "' where userid = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public void updateDeaktiv(String id, int status) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "UPDATE `user` SET deaktiv = '" + status + "' where userid = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public void updateLoeschkenner(String id, int status) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "UPDATE `user`SET loeschkenner = 1 where userid = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public int getBenutzerExist(String id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT count(*) FROM user WHERE UserId = '" + id + "' where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getDeaktivStatus(String id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT deaktiv FROM user WHERE userid = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getAltesPasswort(String id, String passwort) throws SQLException {
        String sql;
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery(sql = "SELECT count(*) FROM user WHERE UserId = '" + id + "' and passwort = '" + passwort + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getPasswort(String id) throws SQLException {
        String sql;
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery(sql = "SELECT passwort FROM user WHERE UserId = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getPasswortForMASTERUSER() throws SQLException {
        String sql;
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery(sql = "SELECT passwort FROM user WHERE UserId = 'admin' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return hash.decodeHashCode((String)result.getString(1));
        }
        return null;
    }

    public ArrayList<String> getUserListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT userid from user where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID"));
        logging.logSQL((Object)("SELECT userid from user where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID")));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    private User mapResultSetToUser(ResultSet result) throws SQLException {
        User user = new User();
        user.setUser(result.getString("UserId"));
        user.setPasswort(result.getString("Passwort"));
        user.setUsergruppe(result.getString("UserGruppe"));
        user.setAdmin(result.getInt("Admin"));
        user.setDeaktiv(result.getInt("Deaktiv"));
        return user;
    }
}

