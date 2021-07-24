package com.jdbc3.preparedstatement.crud;

import com.jdbc3.bean.Customer;
import com.jdbc3.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

//针对Customers表的查询操作

public class CustomerForQuery {
    public static void main(String[] args) {
//        new CustomerForQuery().testQuery1();
        String sql = "select id,name,birth,email from customers where id = ?";
        Customer customer = new CustomerForQuery().queryForCustomers(sql,13);
        System.out.println(customer);
    }
    /**
     * @Author YangPu
     * @Description //TODO 针对于customers表的通用的查询操作
     * @Date 23:13 2021/6/30
     * @Param [sql, args]
     * @return com.jdbc3.bean.Customer
     **/
    public  Customer queryForCustomers(String sql,Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i =0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()){
                Customer cust = new Customer();
                //处理结果集一行数据的每一列
                for (int i =0; i<columnCount;i++){
                    //获取列值
                    Object columnvalue = rs.getObject(i+1);

                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //给cust对象指定的columnName属性，付志伟columnvalue，通过反射
                    Field field = Customer.class.getDeclaredField(columnName);
                    //设置私有成员变量可操作
                    field.setAccessible(true);
                    field.set(cust,columnvalue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
    /**
     * @Author YangPu
     * @Description //TODO 对于customers表的一条语句的查询
     * @Date 23:36 2021/6/30
     * @Param []
     * @return void
     **/
    public  void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);

            //执行
            resultSet = ps.executeQuery();
            //处理结果集
            if (resultSet.next()){//next():判断结果集的吓一跳是否有数据，如果有数据返回true，并将指针下移，如果返回false，则不会下移

                //获取这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //方式三：将数据封装为一个对象：（推荐）
                Customer customer = new Customer(id,name,email,birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //关闭资源
            JDBCUtils.closeResource(conn,ps,resultSet);
        }
    }
}
