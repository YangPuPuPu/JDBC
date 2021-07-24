package com.jdbc3.util;

import com.jdbc.connection.ConnectionTest;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    /**
     * @Author YangPu
     * @Description //TODO 获取连接
     * @Date 22:37 2021/6/30
     * @Param []
     * @return java.sql.Connection
     **/
    public static Connection getConnection() throws Exception{
        //1.读取配置文件的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection conn = DriverManager.getConnection(url,user,password);
        return  conn;
    }
    /**
     * @Author YangPu
     * @Description //TODO 关闭资源操作
     * @Date 22:36 2021/6/30
     * @Param [conn, ps]
     * @return void
     **/
    public static void closeResource(Connection conn, Statement ps){
        try {
            if (ps!=null) {
                ps.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null){
                conn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * @Author YangPu
     * @Description //TODO 关闭资源操作
     * @Date 22:36 2021/6/30
     * @Param [conn, ps, rs]
     * @return void
     **/
    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
        try {
            if (ps!=null) {
                ps.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null){
                conn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (rs!=null) {
                rs.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
