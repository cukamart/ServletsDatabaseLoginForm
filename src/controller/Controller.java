package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import bean.User;
import database.Account;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DataSource ds;
	private PrintWriter out = null;
	private String email = "";
	private String password = "";
	private String repeatPassword = "";
	private String action = "";

	public Controller() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		try {
			InitialContext initContext = new InitialContext();

			Context env = (Context) initContext.lookup("java:comp/env");

			ds = (DataSource) env.lookup("jdbc/webshop");

		} catch (NamingException e) {
			throw new ServletException();
		}
	}

	/**
	 * Tato metoda sa spusti ako homepage... ked sa nic nepouziva action == null
	 * tak sa spusti index.jsp, alebo sa spostu login pripadne create account
	 * podla toho co user klikol...
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		out = response.getWriter();
		action = request.getParameter("action");

		redirectToPageDoGet(request, response);
	}

	/**
	 * Rozhoduje o tom kde presmeruje uzivatela na zaklade jeho volby a
	 * inpute...
	 */
	private void redirectToPageDoGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (action == null) {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else {
			switch (action) {
			case "login":
				setAtributesToEmptyString(request);
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				break;
			case "createaccount":
				setAtributesToEmptyString(request);
				request.getRequestDispatcher("/createaccount.jsp").forward(request, response);
				break;
			default:
				out.println("unrecognised action");
				break;
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection conn = null;
		out = response.getWriter();
		action = request.getParameter("action");

		if (action == null) {
			out.println("unrecognised action");
			return;
		}

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			out.println("Cannot connect to database !");
		}

		Account account = new Account(conn);

		switch (action) {
		case "dologin":
			doLogin(request, response, account);
			break;
		case "createaccount":
			createAccount(request, response, account);
			break;
		default:
			out.println("unrecognised action");
			break;
		}

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Skusi vytvorit account...
	 */
	private void createAccount(HttpServletRequest request, HttpServletResponse response, Account account)
			throws ServletException, IOException {

		email = request.getParameter("email");
		password = request.getParameter("password");
		repeatPassword = request.getParameter("repeatpassword");
		// mam hesla ulozene takze ich mozem vynulovat...
		setAtributesToEmptyString(request);
		request.setAttribute("email", email);

		validInputOfNewAccount(request, response, account);
	}

	private void validInputOfNewAccount(HttpServletRequest request, HttpServletResponse response, Account account)
			throws ServletException, IOException {
		if (!password.equals(repeatPassword)) {
			request.setAttribute("message", "Passwords do not match.");
			request.getRequestDispatcher("/createaccount.jsp").forward(request, response);
		} else {
			User user = new User(email, password);

			if (!user.validate()) {
				request.setAttribute("message", user.getMessage());
				request.getRequestDispatcher("/createaccount.jsp").forward(request, response);
			} else {
				try {
					if (account.exists(email)) {
						request.setAttribute("message", "An account with this email address already exists");
						request.getRequestDispatcher("/createaccount.jsp").forward(request, response);
					} else {
						account.create(email, password);
						request.getRequestDispatcher("/createsuccess.jsp").forward(request, response);
					}
				} catch (SQLException e) {
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}
			}
		}
	}

	private void doLogin(HttpServletRequest request, HttpServletResponse response, Account account)
			throws ServletException, IOException {

		email = request.getParameter("email");
		password = request.getParameter("password");

		User user = new User(email, password);

		request.setAttribute("email", email);
		request.setAttribute("password", ""); // user musi znovu napisat
												// heslo ak zadal zle...
		/*
		 * request.setAttribute("password", password); - pri zlom vyplneni by
		 * heslo ostalo v tabulke..
		 */

		try {
			if (account.login(email, password)) {
				request.getRequestDispatcher("/loginsuccess.jsp").forward(request, response);
			} else {
				request.setAttribute("message", "email address or password not recognised");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			request.setAttribute("email", "Database error: please try again later.");
		}
	}

	private void setAtributesToEmptyString(HttpServletRequest request) {
		request.setAttribute("email", "");
		request.setAttribute("password", "");
		request.setAttribute("repeatpassword", "");
		request.setAttribute("message", "");
	}

}
