package movierecsys.dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLDAO {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static boolean checkConfiguration() {
        if (DAOConfiguration.SQL_HOST == null) {
            System.out.println("DAOConfiguration SQL_HOST not set.");
            return false;
        }
        if (DAOConfiguration.SQL_PASSWORD == null) {
            System.out.println("DAOConfiguration SQL_PASSWORD not set.");
            return false;
        }
        if (DAOConfiguration.SQL_PORT == null) {
            System.out.println("DAOConfiguration SQL_PORT not set.");
            return false;
        }
        if (DAOConfiguration.SQL_USERNAME == null) {
            System.out.println("DAOConfiguration SQL_USERNAME not set.");
            return false;
        }
        if (DAOConfiguration.SQL_DB == null) {
            System.out.println("DAOConfiguration SQL_DB not set.");
            return false;
        }
        return true;
    }

    static List<String> selectToStringList(String source, String selects) {
        List<String> returnList = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection("jdbc:mysql://"+DAOConfiguration.SQL_HOST+":"+DAOConfiguration.SQL_PORT+"/"+DAOConfiguration.SQL_DB,DAOConfiguration.SQL_USERNAME,DAOConfiguration.SQL_PASSWORD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + selects + " FROM " + source);
            while (rs.next()) {
                returnList.add(rs.toString());
            }
            con.close();
        } catch (Exception e) {

        }
        return returnList;
    }
    static List<String> selectToStringList(String source, String selects, String additional) {
        List<String> returnList = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection("jdbc:mysql://"+DAOConfiguration.SQL_HOST+":"+DAOConfiguration.SQL_PORT+"/"+DAOConfiguration.SQL_DB,DAOConfiguration.SQL_USERNAME,DAOConfiguration.SQL_PASSWORD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + selects + " FROM " + source + " " + additional);
            while (rs.next()) {
                returnList.add(rs.toString());
            }
            con.close();
        } catch (Exception e) {

        }
        return returnList;
    }

    static void deleteFromTable(String source, String whereCondition){
        executeUpdate("DELETE FROM "+source+" WHERE "+whereCondition);
    }

    static void updateToTable(String source,String setQuery, String whereCondition){
        executeUpdate("UPDATE "+source+" SET "+setQuery+" WHERE "+whereCondition);
    }

    static void insertToTable(String source, String insertColumns, String insertData) {
        executeUpdate("INSERT INTO "+source+" ("+insertColumns+") VALUES ("+insertData+")");
    }

    private static void executeUpdate(String query){
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection("jdbc:mysql://"+DAOConfiguration.SQL_HOST+":"+DAOConfiguration.SQL_PORT+"/"+DAOConfiguration.SQL_DB,DAOConfiguration.SQL_USERNAME,DAOConfiguration.SQL_PASSWORD);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {

        }
    }

}
