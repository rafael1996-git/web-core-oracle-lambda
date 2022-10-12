package com.talentport.core.db.oracle;

import java.sql.Connection;
import java.sql.DriverManager;


public class OracleDBPool {
    private static Connection singletonConnection = null;
    private static String databaseUrl = null;
    private static String databaseUser = null;
    private static String databasePwd = null;

    public static boolean validateConnection(String databaseUrl,
                                             String databaseUser,
                                             String databasePwd,
                                             int timeOutValidationConn) {
        Connection conn = null;
        try {
            conn = getConnectionStatic(databaseUrl, databaseUser, databasePwd);
            if (!conn.isClosed() && conn.isValid(timeOutValidationConn)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    public static Connection getConnectionStatic(String databaseUrl,
                                                 String databaseUser,
                                                 String databasePwd) throws Exception {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection conn = DriverManager.getConnection(databaseUrl,
                                                          databaseUser,
                                                          databasePwd);
            conn.setAutoCommit(false);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection(String databaseUrl,
                                    String databaseUser,
                                    String databasePwd) throws Exception {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection conn = DriverManager.getConnection(databaseUrl,
                                                          databaseUser,
                                                          databasePwd);
            conn.setAutoCommit(false);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void initSingletonConnectionCredentials(String databaseUrlVal,
                                                          String databaseUserVal,
                                                          String databasePwdVal) throws Exception {
        databaseUrl = databaseUrlVal;
        databaseUser = databaseUserVal;
        databasePwd = databasePwdVal;
    }

    private static void startSingletonConnection() throws Exception {
        if (databaseUrl == null || databaseUrl.isEmpty() ||
            databaseUser == null || databaseUser.isEmpty() ||
            databasePwd == null || databasePwd.isEmpty()) {
            throw new Exception("It's a must to initialize db keys invoking initSingletonConnection at least once");
        }
        singletonConnection = getConnectionStatic(databaseUrl, databaseUser, databasePwd);
    }
    public static Connection getSingletonConnectionJDBC() throws Exception {
        if (singletonConnection == null ||
            singletonConnection.isClosed()) {
            if (singletonConnection != null) {
                try { singletonConnection.close(); } catch (Exception e) { e.printStackTrace(); }
            }
            startSingletonConnection();
        }

        return singletonConnection;
    }
    public static Connection getSingletonConnection(int timeOutValidationConn,String functionName) throws Exception {
        System.out.println("LOGS FUNCTION DB:" + functionName);
        if (singletonConnection == null ||
            singletonConnection.isClosed() ||
            !singletonConnection.isValid(timeOutValidationConn)) {
            if (singletonConnection != null) {
                try { singletonConnection.close(); } catch (Exception e) { e.printStackTrace(); }
            }
            startSingletonConnection();
        }

        return singletonConnection;
    }

    public static void closeSingletonConnection() {
        if (singletonConnection != null) {
            try {
                singletonConnection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                singletonConnection = null;
            }
        }
    }
}
