package boxing.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import boxing.beans.LoginBean;
import boxing.utils.TextUtils;

/**
 * @author Liuyuli
 * @date 2018/6/22.
 */

public class DBHelper {
    public static final String url = "jdbc:mysql://127.0.0.1:8888/im?characterEncoding=utf8&useSSL=false" +
            "&serverTimezone=GMT%2B8";
    public static final String name = "com.mysql.cj.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123456";

    public Connection conn = null;
    public PreparedStatement pst = null;

    private static Log log = LogFactory.getLog(DBHelper.class);

    public DBHelper(String sql) {
        try {
            //指定连接类型
            Class.forName(name);
            //获取连接
            conn = DriverManager.getConnection(url, user, password);
            //准备执行语句
            pst = conn.prepareStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param name
     * @param psd
     */
    public static LoginBean login(String name, String psd) {
        LoginBean loginBean = new LoginBean();
        String sql = "select id from t_user where name='" + name + "' " + " and psd='" + psd + "';";
        DBHelper dbHelper = new DBHelper(sql);
        try {
            ResultSet resultSet = dbHelper.pst.executeQuery();
            log.debug(resultSet);
            System.out.println(resultSet.toString());
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                log.debug(id);
            }
            loginBean.setUserId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return loginBean;
    }
}
