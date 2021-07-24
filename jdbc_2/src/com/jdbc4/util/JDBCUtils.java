package com.jdbc4.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @ClassName JDBCUtils
 * @Description TODO 使用数据库连接池获取连接的工具类
 * @Author YangPu
 * @Date 2021/7/23 11:24
 * @Version 1.0
 **/

public class JDBCUtils {

    public static Connection getConnection() throws Exception {
        // 1.读取配置文件中的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        // 2.加载驱动
        Class.forName(driverClass);

        // 3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * @Author YangPu
     * @Description //TODO 使用C3P0的数据库连接池技术
     * @Date 11:27 2021/7/23
     * @Param []
     * @return Connection
     **/
    //数据库连接池只需提供一个即可
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

    public static Connection getConnection1() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }

    /**
     * @Author YangPu
     * @Description //TODO 使用DBCP数据库连接池技术获取数据库连接
     * @Date 16:40 2021/7/23
     * @Param []
     * @return java.sql.Connection
     **/
    //创建一个DBCP数据库连接池
    private static DataSource source;

    static {
        try {
            Properties pros = new Properties();
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection2() throws Exception {

        Connection conn = source.getConnection();

        return conn;
    }

    /**
     * 使用Druid数据库连接池技术
     */
    private static DataSource source1;

    static {
        try {
            Properties pros = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

            pros.load(is);

            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection3() throws SQLException {

        Connection conn = source1.getConnection();
        return conn;
    }


    /**
     * @return void
     * @Author YangPu
     * @Description //TODO 关闭资源操作
     * @Date 22:36 2021/6/30
     * @Param [conn, ps]
     **/
    public static void closeResource(Connection conn, Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * @return void
     * @Author YangPu
     * @Description //TODO 关闭资源操作
     * @Date 22:36 2021/6/30
     * @Param [conn, ps, rs]
     **/
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * @Author YangPu
     * @Description //TODO 使用dbUtils.jar中的DbUtils工具类,实现资源的关闭
     * @Date 13:50 2021/7/24
     * @Param [conn, ps, rs]
     * @return void
     **/
    public static void closeResource1(Connection conn, Statement ps, ResultSet rs) {
//        try {
//            DbUtils.close(conn);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(rs);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);
    }
}


