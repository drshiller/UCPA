package org.ucnj.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;


public class DbUtil {
	
	private static final String lookupDataSource = "java:comp/env/jdbc/UCNJDatasource";
 
    public static final Connection getConnection() {
        Context init = null;
        try {
            init = new InitialContext();
            return getConnection(init);
        }
        catch (Exception e) {
            logSQLException(e);
            throw new RuntimeException(e);
        }
        finally {
            if (init != null) {
                try {
                    init.close();
                }
                catch (Exception e) {
                	System.err.println("Exception!\n");
                	e.printStackTrace();
                }
            }
        }
    }

    public static Connection getConnection(Context init) {
        try {
            Object obj = init.lookup(lookupDataSource);
            DataSource ds =
                    (DataSource) PortableRemoteObject.narrow(obj, DataSource.class);
            return ds.getConnection();
        }
        catch (Exception e) {
            logSQLException(e);
            throw new RuntimeException(e);
        }
    }

    public static final void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (Exception e) {
                logSQLException(e);
            }
        }
    }

    public static final void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (Exception e) {
                logSQLException(e);
            }
        }
    }

    public static final void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (Exception e) {
                logSQLException(e);
            }
        }
    }

    public static final void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                close(rs);
            }
        }
        finally {
            try {
                if (stmt != null) {
                    close(stmt);
                }
            }
            finally {
                if (conn != null) {
                    close(conn);
                }
            }
        }
    }

    public static void logSQLException(Exception e) {
        if (e != null) {
            if (e instanceof SQLException) {
                SQLException se = (SQLException) e;
                System.err.println("SQLException! - ERROR CODE = " + se.getErrorCode() + ", SQLSTATE = " + se.getSQLState() + ": " + se);
                logSQLException(se.getNextException());
            }
            else {
            	System.err.println("Exception!\n");
            	e.printStackTrace();
            }
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            }
            catch (Exception e) {
                DbUtil.logSQLException(e);
            }
        }
    }
}
