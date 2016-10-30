package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vo.UsuarioVO;

public class Utils {

    public static UsuarioVO usuarioCorrente=null;

    public static final String lstPrderByEntidade = "Entidade";
    public static final String lstPrderByArea = "Área";
    public static final String lstPrderByItem = "Item";
    public static final String lstPrderByUsuario = "Usuário";
    public static final String lstPrderByDataHora = "Data/hora";



    /**
     * Retorna double arredondado em 2 casas decimais
     * @param val valor a ser arredondado
     * @return double com o valor arredondado
     */
    public static Double round2(Double val) {
        return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Retorna data no formato brasileiro (dd/MM/yyyy)
     * @param dt data a ser formatada
     * @return String contendo a data
     */
    public static String formatarData(Date dt) {

        SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return (dt1.format(dt));

    }

    public static String formatarHora(Date dt) {

        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
        return (dt1.format(dt));

    }

    public static String formatarDoubleDecimal( Double vlr) {

        return String.format( "%.2f", vlr);

    }

}