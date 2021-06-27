package org.ucnj.paccess.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ucnj.paccess.UCPA;
import org.ucnj.paccess.dataaccess.beans.Order;
import org.ucnj.paccess.dataaccess.helpers.OrderHelper;

public class Receipting extends UCServlet {
	
	private static final long serialVersionUID = 1L;

	public void performTask(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String nextURL = UCPA.REDIRECT_PAGE;
		
		// pick up the top level application object from the session object
		HttpSession session = request.getSession(false);
		if (session != null) {
			
			UCPA userUCPA = (UCPA)session.getAttribute("userUCPA");

			if (request.getParameter("ssl_result_message").trim().equals("APPROVAL")) {
				long invoiceNumber = Long.parseLong(request.getParameter("invoicenumber").trim());
				OrderHelper orderHelper = new OrderHelper();
				Order order = orderHelper.readOrder(invoiceNumber);
				setPaymentDetails(request, order);
				orderHelper.updateOrder(order);
				userUCPA.setLastOrder(order);
				nextURL = "/DocIndex";
			}
			else {
				nextURL = "/COWarning.jsp";
			}
			
			userUCPA.getCurrentRequest().getList().clear();
			
			// store data required by JSP that will display results	
			request.setAttribute("userUCPArq", userUCPA);
		}
	
		// hand onto JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}
	
	private void setPaymentDetails(HttpServletRequest request, Order order) {
		String txType = request.getParameter("ssl_transaction_type");
		String txId = request.getParameter("ssl_txn_id");
		String txTimestamp = request.getParameter("ssl_txn_time");
		String txApprovalCode = request.getParameter("ssl_approval_code");
		String txResult = request.getParameter("ssl_result_message");

		String shipToCompany = request.getParameter("ssl_ship_to_company");
		String shipToFirstName = request.getParameter("ssl_ship_to_first_name");
		String shipToLastName = request.getParameter("ssl_ship_to_last_name");
		String shipToAddress1 = request.getParameter("ssl_ship_to_address1");
		String shipToAddress2 = request.getParameter("ssl_ship_to_address2");
		String shipToCity = request.getParameter("ssl_ship_to_city");
		String shipToState = request.getParameter("ssl_ship_to_state");
		String shipToPostCode = request.getParameter("ssl_ship_to_zip");
		String shipToCountry = request.getParameter("ssl_ship_to_country");

		String billToCompany = request.getParameter("ssl_company");
		String billToFirstName = request.getParameter("ssl_first_name");
		String billToLastName = request.getParameter("ssl_last_name");
		String billToAddress1 = request.getParameter("ssl_avs_address");
		String billToAddress2 = request.getParameter("ssl_address2");
		String billToCity = request.getParameter("ssl_city");
		String billToState = request.getParameter("ssl_state");
		String billToPostCode = request.getParameter("ssl_avs_zip");
		String billToCountry = request.getParameter("ssl_country");
		
		String email = request.getParameter("ssl_email");
		String phone = request.getParameter("ssl_phone");

		order.setPaymentDetails(txType, txId, txTimestamp, txApprovalCode, txResult,
			shipToCompany, shipToFirstName, shipToLastName, shipToAddress1, 
			shipToAddress2, shipToCity, shipToState, shipToPostCode, shipToCountry,
			billToCompany, billToFirstName, billToLastName, billToAddress1, 
			billToAddress2, billToCity, billToState, billToPostCode, billToCountry,
			email, phone);
	}

}
