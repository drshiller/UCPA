package org.ucnj.paccess.dataaccess.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.ucnj.paccess.UCPA;
import org.ucnj.utilities.DbUtil;

public class MortgageCancellationHelper {
	
	public static int getNumberOfPagesFromMortgage(String book, String page) throws Exception 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String fBook = UCPA.formatSpace(book, 5, true);
		String fPage = UCPA.formatSpace(page, 4, true);
			
		String sql =
			"SELECT WIMGPG "
		  + "FROM C573WEB.WALF01C "
	      + "WHERE WDOCTP = ? "
		  +  "AND WBOOK = ? "
		  +  "AND WPAGE = ?";

		int nPages = 0;
		try {
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, UCPA.DOC_TYPE_MORTGAGE);
			ps.setString(2, fBook);
			ps.setString(3, fPage);
			rs = ps.executeQuery();
			if (rs.next()) {
				nPages = rs.getInt("WIMGPG");
			}
		}
		catch (Exception ex) {
			UCPA.throwExMsg("MortgageCancellationHelper", "getNumberOfPagesFromMortgage", ex);
		}
		finally {
			DbUtil.close(conn, ps, rs);
		}
		
		return nPages;
	}

}
