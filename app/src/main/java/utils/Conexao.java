package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
	public static Connection obterConexao() throws SQLException {

        Context applicationContext = Login.getContextOfApplication();
        SharedPreferences settings = applicationContext.getSharedPreferences("settings", 0);
        String  mySqlserver = settings.getString("ServerIP", "192.168.25.1:3307");

        DriverManager.setLoginTimeout(15);
        return DriverManager.getConnection("jdbc:mysql://" + mySqlserver + "/pontua_bsi?connectTimeout=15000", "root", "123" );

        //ssl: &verifyServerCertificate=false&useSSL=true&requireSSL=true

	}

}
