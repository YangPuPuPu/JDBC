package com.jdbc2.statement.crud;

import com.jdbc3.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @ClassName PreparedStatementTest
 * @Description TODO 演示使用PreparedStatement替换Statement，解决SQL注入问题
 * @Author YangPu
 * @Date 2021/7/6 10:53
 * @Version 1.0
 *
 * 除了解决Statement的拼串，sql问题之外，PreparedStatement还有哪些好处呢？
 * 1.PrepareStatement操作Blob的数据，而Statement 做不到
 * 2.PrepareStatement可以实现更高效的批量操作
 *
 **/
public class PreparedStatementTest {

    public static void main(String[] args) {
        new PreparedStatementTest().testLogin();
    }

    public void testLogin() {
        Scanner scan = new Scanner(System.in);

        System.out.print("用户名：");
        String userName = scan.nextLine();
        System.out.print("密   码：");
        String password = scan.nextLine();

        // SELECT user,password FROM user_table WHERE USER = '1' or ' AND PASSWORD = '
        // ='1' or '1' = '1';
        String sql = "SELECT user,password FROM user_table WHERE USER = ? AND PASSWORD = ?";
        User user = getInstance(User.class,sql,userName,password);
        if (user != null) {
            System.out.println("登陆成功!");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }
    /**
     * @Author YangPu
     * @Description //TODO 针对于不同表的查询操作，返回表中的一条记录
     * @Date 10:24 2021/7/6
     * @Param [clazz, sql, args]
     * @return T
     **/
    public <T> T getInstance(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
}
