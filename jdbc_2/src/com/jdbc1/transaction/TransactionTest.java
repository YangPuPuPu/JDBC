package com.jdbc1.transaction;

import com.jdbc1.util.JDBCUtils;
import sun.security.jca.GetInstance;

import java.lang.reflect.Field;
import java.sql.*;

/*
 * 1.什么叫数据库事务？
 * 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 * 		> 一组逻辑操作单元：一个或多个DML操作。
 *
 * 2.事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
 * 下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 * 3.数据一旦提交，就不可回滚
 *
 * 4.哪些操作会导致数据的自动提交？
 * 		>DDL操作一旦执行，都会自动提交。
 * 			>set autocommit = false 对DDL操作失效
 * 		>DML默认情况下，一旦执行，就会自动提交。
 * 			>我们可以通过set autocommit = false的方式取消DML操作的自动提交。
 * 		>默认在关闭连接时，会自动的提交数据
 */



/**
 * @ClassName TransactionTest
 * @Description TODO 事务测试
 * @Author YangPu
 * @Date 2021/7/7 20:22
 * @Version 1.0
 **/
public class TransactionTest {
    /*
     *针对于数据表user_table来说，
     * AA用户给BB用户转账100
     *
     * update user_table set balance = balance-100 where user = "AA";
     * update user_table set balance = balance+100 where user = "BB";
     */
    public static void main(String[] args) throws Exception {
//        String sql1 = "update user_table set balance = balance-100 where user = ?";
//        update(sql1, "AA");
//
//        //模拟网络异常
//        System.out.println(10/0);
//
//        String sql2 = "update user_table set balance = balance+100 where user = ?";
//        update(sql2, "BB");
//
//        System.out.println("转账成功");

//       testUpdate();

        testQuery();
    }
    /**********************************没有考虑数据库事务的增删改操作***************************************/
    /**
     * @Author YangPu
     * @Description //TODO 通用的增删改操作 Version 1.0
     * @Date 15:19 2021/6/29
     * @Param [sql, args]
     * @return void
     **/
    public static int  update(String sql,Object ...args){//sql中占位符的个数应该与可变形参的长度是一致的
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
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }


    /**********************************有考虑数据库事务的增删改操作***************************************/
    /**
     * @Author YangPu
     * @Description //TODO 通用的增删改操作 Version 2.0
     * @Date 15:19 2021/6/29
     * @Param [sql, args]
     * @return void
     **/
    public static int  update(Connection conn,String sql,Object ...args){//sql中占位符的个数应该与可变形参的长度是一致的
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0;i < args.length;i++){
                ps.setObject(i+1,args[i]);//小心形参声明错误！！
            }
            //3.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //4.资源的关闭
            JDBCUtils.closeResource(null,ps);
        }
        return 0;
    }
    /**
     * @Author YangPu
     * @Description //TODO 用于测试事务更改
     * @Date 23:41 2021/7/9
     * @Param []
     * @return void
     **/
    public static void testUpdate(){
    Connection conn = null;
    try {
        conn = JDBCUtils.getConnection();
        System.out.println(conn.getAutoCommit());
        //取消事务的自动提交
        conn.setAutoCommit(false);
        String sql1 = "update user_table set balance = balance-100 where user = ?";
        update(conn,sql1, "AA");

        //模拟网络异常
        System.out.println(10/0);

        String sql2 = "update user_table set balance = balance+100 where user = ?";
        update(conn,sql2, "BB");

        System.out.println("转账成功");
        conn.commit();
    } catch (Exception e) {
        e.printStackTrace();
        try {
            conn.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }finally {
        //设置为自动提交（为后面数据库连接池铺垫）
        try {
            conn.setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JDBCUtils.closeResource(conn,null);
    }
}

    /**
     * @Author YangPu
     * @Description //TODO 针对于不同表的查询操作，返回表中的一条记录(Version 2.0,考虑上事务)
     * @Date 10:24 2021/7/6
     * @Param [clazz, sql, args]
     * @return T
     **/
    public static <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //获取结果集
            rs = ps.executeQuery();
            //获取元数据
            ResultSetMetaData psmd = rs.getMetaData();
            //获取列数
            int columnCount = psmd.getColumnCount();
            if (rs.next()){
                T t = clazz.newInstance();
                for (int i =0;i<columnCount;i++){
                    //获取每个列的列值，通过ResultSet
                    Object columnValue= rs.getObject(i+1);
                    //获取每个列的列名，通过ResultSetMetaData
                    String columnLabel = psmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性赋值给指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }

    public static void testQuery() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        //获取当前的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //取消自动提交
        conn.setAutoCommit(false);
        //设置当前的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        String sql = "select * from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");

        System.out.println(user);
    }
}
