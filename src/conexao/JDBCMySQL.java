package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCMySQL {

	// TODO Passar para parameter.
	private final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private Connection cnx = null;
	private String usuario = "root";
	private String senha = "Qwerty147!";
	private String PathBase = "appfilme";
	private final String URL = "jdbc:mysql://localhost:3306/" + PathBase;

	public Connection conectar() {
		try {
			Class.forName(DRIVER_CLASS);
			cnx = DriverManager.getConnection(URL, usuario, senha);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnx;
	}

	public void fecharCNX() {
		try {
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
