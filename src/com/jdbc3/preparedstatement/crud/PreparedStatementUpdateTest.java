package com.jdbc3.preparedstatement.crud;

import com.jdbc.connection.ConnectionTest;
import com.jdbc3.util.JDBCUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
/*
        使用PreparedStatement来替换Statement，实现对数据表的增删改操作

        增删改，查
 */
public class PreparedStatementUpdateTest {
    public static void main(String[] args) throws SQLException, IOException, ParseException, ClassNotFoundException {
//        new PreparedStatementUpdateTest().testInsert();
//        new PreparedStatementUpdateTest().testUpdate();

//        String sql = "delete from customers where id = ?";
//        new PreparedStatementUpdateTest().update(sql,3);

        String sql = "update `order` set order_name = ? where order_id = ?";
        new PreparedStatementUpdateTest().update(sql,"DD","2");
    }

    /**
     * @Author YangPu
     * @Description //TODO 通用的增删改操作
     * @Date 15:19 2021/6/29
     * @Param [sql, args]
     * @return void
     **/
    public void  update(String sql,Object ...args){//sql中占位符的个数应该与可变形参的长度是一致的
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0;i < args.length;i++){
                ps.setObject(i+1,args[i]);//小心形参声明错误！！
            }
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
    }


    /**
     * @Author YangPu
     * @Description //TODO 修改customers表的一条记录
     * @Date 15:20 2021/6/29
     * @Param []
     * @return void
     **/
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            String sql = "Update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1,"莫扎特");
            ps.setObject(2,18);
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
    }

    /**
     * @Author YangPu
     * @Description //TODO 向customers表中添加一条记录
     * @Date 15:21 2021/6/29
     * @Param []
     * @return void
     **/
    public void testInsert()  {
        //1.读取配置文件的4个基本信息
        Connection conn = null;
        PreparedStatement ps = null;
        try {
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
            conn = DriverManager.getConnection(url,user,password);
//        System.out.println(conn);

            //4.预编译sql语句，返回preparedStatement的实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";//?：占位符
            ps = conn.prepareStatement(sql);
            //5.填充占位符
            ps.setString(1,"哪吒");
            ps.setString(2,"nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("1000-01-01");
            ps.setDate(3,new java.sql.Date(date.getTime()));

            //6.执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.资源关闭
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
