package org.ucnj.paccess.dataaccess.helpers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;

import org.ucnj.paccess.UCPA;
import org.ucnj.paccess.dataaccess.beans.Order;
import org.ucnj.paccess.dataaccess.beans.OrderItem;
import org.ucnj.paccess.dataaccess.beans.Request;
import org.ucnj.utilities.DbUtil;

public class OrderHelper {
	
	private static String LIBRARY = "C573WEB";
	private static String PFILE_ORDER = "WAPF30";
	private static String PFILE_ITEM = "WAPF31";	
	private static String TABLE_ORDER = LIBRARY + "." + PFILE_ORDER;	
	private static String TABLE_ITEM = LIBRARY + "." + PFILE_ITEM;	
	
	@SuppressWarnings("unchecked")
	public long create(Request request)
	throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		
		long invoiceNumber = -1;
		try {
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			
			// set up a transaction
			conn.setAutoCommit(false);

			// create the main order; get the cost for the order,
			// transforming it from a string to a float, remembering to strip
			// off the initial dollar sign
			Order order = new Order(request.cost());
			order.setInvoiceNumber(getInvoiceNumber(conn));
			createOrder(stmt, order);
			
			// create the associated items
			Hashtable requestList = request.getList();
			Enumeration listKeys = requestList.keys();
			int i = 1;
			while (listKeys.hasMoreElements()) {
				String itemKey = (String)listKeys.nextElement();
				createItem(stmt, order.getInvoiceNumber(), i++,	(OrderItem)requestList.get(itemKey));
			}
			
			// we made it so commit the transaction
			conn.commit();
			
			invoiceNumber = order.getInvoiceNumber();
		}
		catch (Exception ex) {
			// failed so roll back the transaction
			conn.rollback();
			UCPA.throwExMsg("OrderHelper", "create", ex);
		}
		finally {
			DbUtil.close(conn, stmt, null);
		}
		
		return invoiceNumber;
	}
	
	public Order readOrder(long invoiceId) throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Order order = null;
		
		String qryStr =
			"SELECT " +
				"OINV#, OAMT, ODATE, OTIME, OLDATE, OLTIME, " +
				"OSTAT, OLTRID, OLTTYP, OLTAPP, OLTRST, " +
				"OVCOMP, OVNAM1, OVNAM2, OVADD1, OVADD2, " +
				"OVCITY, OVST, OVZIP, OVCNTY, " +
				"OBCOMP, OBNAM1, OBNAM2, OBADD1, OBADD2, " +
				"OBCITY, OBST, OBZIP, OBCNTY " +
			"FROM " + TABLE_ORDER + " " +
			"WHERE OINV# = " + invoiceId;
		
		try {
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(qryStr);
			
			if (rs.next() == true) {
				order = new Order();
				order.setInvoiceNumber(rs.getLong("OINV#"));
				order.setAmount(rs.getFloat("OAMT")); 
				order.setOrderDate(rs.getInt("ODATE")); 
				order.setOrderTime(rs.getInt("OTIME")); 
				order.setLastActitivityDate(rs.getInt("OLDATE")); 
				order.setLastActitivityTime(rs.getInt("OLTIME")); 
				order.setStatus(rs.getString("OSTAT").charAt(0));
				order.setTxId(rs.getString("OLTRID").trim());
				order.setTxType(rs.getString("OLTTYP").trim());
				order.setTxApprovalCode(rs.getString("OLTAPP").trim());
				order.setTxResult(rs.getString("OLTRST").trim());
				order.getShipTo().setCompany(rs.getString("OVCOMP").trim());
				order.getShipTo().setFirstName(rs.getString("OVNAM1").trim());
				order.getShipTo().setLastName(rs.getString("OVNAM2").trim());
				order.getShipTo().setAddress1(rs.getString("OVADD1").trim());
				order.getShipTo().setAddress2(rs.getString("OVADD2").trim());
				order.getShipTo().setCity(rs.getString("OVCITY").trim());
				order.getShipTo().setState(rs.getString("OVST").trim());
				order.getShipTo().setPostCode(rs.getString("OVZIP").trim());
				order.getShipTo().setCountry(rs.getString("OVCNTY").trim());
				order.getBillTo().setCompany(rs.getString("OBCOMP").trim());
				order.getBillTo().setFirstName(rs.getString("OBNAM1").trim());
				order.getBillTo().setLastName(rs.getString("OBNAM2").trim());
				order.getBillTo().setAddress1(rs.getString("OBADD1").trim());
				order.getBillTo().setAddress2(rs.getString("OBADD2").trim());
				order.getBillTo().setCity(rs.getString("OBCITY").trim());
				order.getBillTo().setState(rs.getString("OBST").trim());
				order.getBillTo().setPostCode(rs.getString("OBZIP").trim());
				order.getBillTo().setCountry(rs.getString("OBCNTY").trim());
			}
		}
		catch (Exception ex) {
			UCPA.throwExMsg("OrderHelper", "read", ex);
		}
		finally {
			DbUtil.close(conn, stmt, rs);
		}
		
		return order;
	}
	
	public void updateOrder(Order order) throws Exception {
	
		Connection conn = null;
		Statement stmt = null;
		
		String qryStr =
			"UPDATE " + TABLE_ORDER + " " +
			"SET " +
				"OAMT=" + order.getAmount() + ", " +
				"ODATE=" + order.getOrderDate() + ", " +
				"OTIME=" + order.getOrderTime() + ", " +
				"OLDATE=" + order.getLastActitivityDate() + ", " +
				"OLTIME=" + order.getLastActitivityTime() + ", " +
				"OSTAT='" + order.getStatus() + "', " +
				"OLTRID='" + order.getTxId() + "', " +
				"OLTTYP='" + order.getTxType() + "', " +
				"OLTAPP='" + order.getTxApprovalCode() + "', " +
				"OLTRST='" + order.getTxResult() + "', " +
				"OVCOMP='" + order.getShipTo().getCompany() + "', " +
				"OVNAM1='" + order.getShipTo().getFirstName() + "', " +
				"OVNAM2='" + order.getShipTo().getLastName() + "', " +
				"OVADD1='" + order.getShipTo().getAddress1() + "', " +
				"OVADD2='" + order.getShipTo().getAddress2() + "', " +
				"OVCITY='" + order.getShipTo().getCity() + "', " +
				"OVST='" + order.getShipTo().getState() + "', " +
				"OVZIP='" + order.getShipTo().getPostCode() + "', " +
				"OVCNTY='" + order.getShipTo().getCountry() + "', " +
				"OBCOMP='" + order.getBillTo().getCompany() + "', " +
				"OBNAM1='" + order.getBillTo().getFirstName() + "', " +
				"OBNAM2='" + order.getBillTo().getLastName() + "', " +
				"OBADD1='" + order.getBillTo().getAddress1() + "', " +
				"OBADD2='" + order.getBillTo().getAddress2() + "', " +
				"OBCITY='" + order.getBillTo().getCity() + "', " +
				"OBST='" + order.getBillTo().getState() + "', " +
				"OBZIP='" + order.getBillTo().getPostCode() + "', " +
				"OBCNTY='" + order.getBillTo().getCountry() + "', " +
				"OBEMAIL='" + order.getEmail() + "', " +
				"OBPHONE='" + order.getPhone() + "' " +
			"WHERE OINV# = " + order.getInvoiceNumber();
		
		try {
			conn = DbUtil.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(qryStr);
		}
		catch (Exception ex) {
			UCPA.throwExMsg("OrderHelper", "updateOrder", ex);
		}
		finally {
			DbUtil.close(conn, stmt, null);
		}
		
	}
	
	private void createOrder(Statement stmt, Order order)
	throws SQLException {
		String qryStr =
			"INSERT INTO " + TABLE_ORDER + " " +
				"(OINV#, OAMT, ODATE, OTIME, OLDATE, OLTIME, " +
				"OSTAT, OLTRID, OLTTYP, OLTAPP, OLTRST, " +
				"OVCOMP, OVNAM1, OVNAM2, OVADD1, OVADD2, " +
				"OVCITY, OVST, OVZIP, OVCNTY, " +
				"OBCOMP, OBNAM1, OBNAM2, OBADD1, OBADD2, " +
				"OBCITY, OBST, OBZIP, OBCNTY) " +
			"VALUES(" +
				order.getInvoiceNumber() + ", " +
				order.getAmount() + ", " +
				order.getOrderDate() + ", " +
				order.getOrderTime() + ", " +
				order.getLastActitivityDate() + ", " +
				order.getLastActitivityTime() + ", '" +
				order.getStatus() + "', '" +
				((order.getTxId() != null) ? order.getTxId() : "") + "', '" +
				((order.getTxType() != null) ? order.getTxType() : "") + "', '" +
				((order.getTxApprovalCode() != null) ? order.getTxApprovalCode() : "") + "', '" +
				((order.getTxResult() != null) ? order.getTxResult() : "") + "', '" +
				((order.getShipTo().getCompany() != null) ? order.getShipTo().getCompany() : "") + "', '" +
				((order.getShipTo().getFirstName() != null) ? order.getShipTo().getFirstName() : "") + "', '" +
				((order.getShipTo().getLastName() != null) ? order.getShipTo().getLastName() : "") + "', '" +
				((order.getShipTo().getAddress1() != null) ? order.getShipTo().getAddress1() : "") + "', '" +
				((order.getShipTo().getAddress2() != null) ? order.getShipTo().getAddress2() : "") + "', '" +
				((order.getShipTo().getCity() != null) ? order.getShipTo().getCity() : "") + "', '" +
				((order.getShipTo().getState() != null) ? order.getShipTo().getState() : "") + "', '" +
				((order.getShipTo().getPostCode() != null) ? order.getShipTo().getPostCode() : "") + "', '" +
				((order.getShipTo().getCountry() != null) ? order.getShipTo().getCountry() : "") + "', '" +
				((order.getBillTo().getCompany() != null) ? order.getBillTo().getCompany() : "") + "', '" +
				((order.getBillTo().getFirstName() != null) ? order.getBillTo().getFirstName() : "") + "', '" +
				((order.getBillTo().getLastName() != null) ? order.getBillTo().getLastName() : "") + "', '" +
				((order.getBillTo().getAddress1() != null) ? order.getBillTo().getAddress1() : "") + "', '" +
				((order.getBillTo().getAddress2() != null) ? order.getBillTo().getAddress2() : "") + "', '" +
				((order.getBillTo().getCity() != null) ? order.getBillTo().getCity() : "") + "', '" +
				((order.getBillTo().getState() != null) ? order.getBillTo().getState() : "") + "', '" +
				((order.getBillTo().getPostCode() != null) ? order.getBillTo().getPostCode() : "") + "', '" +
				((order.getBillTo().getCountry() != null) ? order.getBillTo().getCountry() : "") +
			"')";
		stmt.executeUpdate(qryStr);
	}

	private void createItem(Statement stmt, long invoiceNumber, int itemNumber, OrderItem orderItem)
	throws SQLException {
		String qryStr =
			"INSERT INTO " + TABLE_ITEM + " " +
				"(OINV#, OITEM#, ODOCTP, OSTPYY, " +
				"OINT#P, OINT#M, OINT#S, " +
				"OPCOPY, OCCOPY) " +
			"VALUES(" +
				invoiceNumber + ", " +
				itemNumber + ", " +
				orderItem.getResult().getDocType() + ", " +
				orderItem.getResult().getStampDate().substring(6) + ", '" +
				orderItem.getResult().getInstrPrefix() + "', '" +
				UCPA.formatSpace(orderItem.getResult().getInstrMiddle(), 6, true) + "', '" +
				orderItem.getResult().getInstrSuffix() + "', " +
				orderItem.getNumPlain() + ", " +
				orderItem.getNumCertified() +
			")";
		stmt.executeUpdate(qryStr);
	}

	private long getInvoiceNumber(Connection conn)
	throws Exception {
		String invoiceNumber = null;
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall("{call C573WEB.WAR300G(?)}");
			cs.registerOutParameter(1, Types.CHAR);
			cs.execute();
			invoiceNumber = cs.getString(1);
		}
		catch (SQLException ex) {
			UCPA.throwExMsg("OrderHelper", "getInvoiceNumber", ex);
		}
		finally {
			try {
				if (cs != null)
					cs.close();
			}
			catch (Exception ex) {} 
		}
		return Long.parseLong(invoiceNumber);
	}
	
}
