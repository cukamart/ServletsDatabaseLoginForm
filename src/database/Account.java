package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private Connection conn;

	public Account(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Skusi sa prihlasit pod menom a heslom... Pouzivam wild cards pretoze
	 * NamedParameters je len v NamedTemplateJDBC ktora je sucastou springu...
	 * SELECT statement sa spusta pomocou executeQuery (INSERT DELETE ma
	 * updateQuery) Nesmiem zabudnut uzatvorit ResultSet!
	 */
	public boolean login(String email, String password) throws SQLException {

		String sql = "select count(*) as count from User where email=? and password=?";

		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setString(1, email);
		stmt.setString(2, password);

		ResultSet rs = stmt.executeQuery();

		int count = 0;

		if (rs.next()) {
			count = rs.getInt("count");
		}

		rs.close();

		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean exists(String email) throws SQLException {

		String sql = "select count(*) as count from User where email=?";

		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setString(1, email);

		ResultSet rs = stmt.executeQuery();

		int count = 0;

		if (rs.next()) {
			count = rs.getInt("count");
		}

		rs.close();

		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Vlozi do databazy noveho usera, pouziva metodu executeUpdate - ta moze
	 * manipulovat s databazov
	 */
	public void create(String email, String password) throws SQLException {
		String sql = "insert into User (email, password) values (?, ?)";

		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setString(1, email);
		stmt.setString(2, password);

		stmt.executeUpdate();

		stmt.close();
	}
}
