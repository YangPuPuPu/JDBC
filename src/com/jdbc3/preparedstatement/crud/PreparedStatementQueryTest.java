package com.jdbc3.preparedstatement.crud;

import com.jdbc3.bean.Customer;
import com.jdbc3.bean.Order;
import com.jdbc3.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PreparedStatementQueryTest
 * @Description TODO 使用PreparedStatement实现针对不同表的查询操作
 * @Author YangPu
 * @Date 2021/7/5 22:39
 * @Version 1.0
 **/
public class PreparedStatementQueryTest {

    public static void main(String[] args) {
        String sql = "select id,name,birth,email from customers where id = ?";
        Customer customer = new PreparedStatementQueryTest().getInstance(Customer.class, sql, 12);
        System.out.println(customer);
        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = new OrderForQuery().queryForOrder(sql1, 1);
        System.out.println(order);
        String sql2 = "select id,name,birth,email from customers where id < ?";
        List<Customer> list = new PreparedStatementQueryTest().getForList(Customer.class, sql2, 12);
        list.forEach(System.out::println);
    }

    public <T> List<T> getForList(Class<T> clazz,String sql,Object ...args){
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
            //创建集合
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()){
                T t = clazz.newInstance();
                //处理结果集中的每一个列：给t对象的属性赋值
                for (int i =0;i<columnCount;i++){
                    //获取每个列的列值，通过ResultSet
                    Object columnValue= rs.getObject(i+1);
                    //获取每个列的列名，通过ResultSetMetaData
                    String columnLabel = psmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性付志伟指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
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
