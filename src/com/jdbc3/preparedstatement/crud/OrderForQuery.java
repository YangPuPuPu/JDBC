package com.jdbc3.preparedstatement.crud;

import com.jdbc3.bean.Order;
import com.jdbc3.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @ClassName OrderForQuery
 * @Description TODO 针对Order表的查询操作
 * @Author YangPu
 * @Date 2021/7/5 16:15
 * @Version 1.0
 **/
public class OrderForQuery {
    /*
    *针对于表的字段名和类的属性名不相同的情况
    * 1.必须声明sql时，使用类的属性名来命名字段的别名
    * 2.使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName(),获取列的别名
    *   说明：如果sql中没有给字段其别名，getColumnLabel()获取的就是列名。
    */
    public static void main(String[] args) {
//        new OrderForQuery().testQuery1();
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = new OrderForQuery().queryForOrder(sql, 1);
        System.out.println(order);
    }

    /**
     * @Author YangPu
     * @Description //TODO 针对于order表的通用的查询操作
     * @Date 17:22 2021/7/5
     * @Param [sql, args]
     * @return com.jdbc3.bean.Order
     **/
    public Order queryForOrder(String sql,Object ...args) {
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
                Order order = new Order();
                for (int i =0;i<columnCount;i++){
                    //获取每个列的列值，通过ResultSet
                    Object columnValue= rs.getObject(i+1);
                    //获取每个列的列名，通过ResultSetMetaData
                    //获取列的列名，getColumnName()
                    //获取列的别名，getColumnLabel()
    //                String columnValue = psmd.getColumnName(i+1);
                    String columnLabel = psmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性付志伟指定的值columnValue
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
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
     * @Description //TODO 查询一条语句的方法
     * @Date 16:47 2021/7/5
     * @Param []
     * @return void
     **/
    public void testQuery1(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_Date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
            rs = ps.executeQuery();
            if(rs.next()){
                int id = (int)rs.getObject(1);
                String name = (String)rs.getObject(2);
                Date date = (Date) rs.getObject(3);

                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }

    }
}
