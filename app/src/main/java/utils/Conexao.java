package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bsi.pontua.Login;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;

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
	 */

	public static int qtdConns = 0;

	public static Connection obterConexao() throws SQLException {

		Log.d("obterConexao", String.valueOf(qtdConns));
		qtdConns++;

        Context applicationContext = Login.getContextOfApplication();
        SharedPreferences settings = applicationContext.getSharedPreferences("settings", 0);
        String  mySqlserver = settings.getString("ServerIP", "mysql.infosgi.com.br:3306");
		Boolean usarSSL = settings.getBoolean("UsarSSL", false);

        DriverManager.setLoginTimeout(10);

		String connStr;
		String databaseName = "infosgi03";
		String userName = "infosgi03";
		String password = "santacruz";

		connStr = "jdbc:mysql://" + mySqlserver + "/" + databaseName + "?connectTimeout=10000";

		if(usarSSL){
			connStr += "&verifyServerCertificate=false&useSSL=true&requireSSL=true";
		}

		return DriverManager.getConnection(connStr, userName, password);

	}

	public static void validarConn(Connection conn){

		try {
			if(conn.isClosed()){
                conn = Conexao.obterConexao();
            }

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
