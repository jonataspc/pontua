package utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

/**

 * 
 */
public class Conexao {

	// Carrega driver JDBC
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Obtem conexao com banco de dados
	 * 
	 * @return Conex�o obtida
	 * @throws SQLException
	 */
	public static Connection obterConexao() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://192.168.4.135:3307/pontua_bsi", "root", "123");
	}

}
