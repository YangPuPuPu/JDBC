package com.jdbc2.dao;

import com.jdbc1.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DAO
 * @Description TODO 封装了对数据表的通用的操作
 * @Author YangPu
 * @Date 2021/7/9 23:58
 * @Version 1.0
 **/
public abstract class BaseDAO {
    /**
     * @return void
     * @Author YangPu
     * @Description //TODO 通用的增删改操作 Version 2.0(考虑上事务的)
     * @Date 15:19 2021/6/29
     * @Param [sql, args]
     **/
    public int update(Connection conn, String sql, Object... args) {//sql中占位符的个数应该与可变形参的长度是一致的
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//小心形参声明错误！！
            }
            //3.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4.资源的关闭
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    /**
     * @return T
     * @Author YangPu
     * @Description //TODO 通用的查询操作，针对于不同表的查询操作，返回表中的一条记录(Version 2.0,考虑上事务)
     * @Date 10:24 2021/7/6
     * @Param [clazz, sql, args]
     **/
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //获取结果集
            rs = ps.executeQuery();
            //获取元数据
            ResultSetMetaData psmd = rs.getMetaData();
            //获取列数
            int columnCount = psmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值，通过ResultSet
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名，通过ResultSetMetaData
                    String columnLabel = psmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性赋值给指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    /**
     * @return java.util.List<T>
     * @Author YangPu
     * @Description //TODO 通用的查询操作，针对于不同表的查询操作，返回表中的多条记录(Version 2.0,考虑上事务)
     * @Date 0:08 2021/7/10
     * @Param [conn, clazz, sql, args]
     **/
    public <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //获取结果集
            rs = ps.executeQuery();
            //获取元数据
            ResultSetMetaData psmd = rs.getMetaData();
            //获取列数
            int columnCount = psmd.getColumnCount();
            //创建集合
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();
                //处理结果集中的每一个列：给t对象的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值，通过ResultSet
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名，通过ResultSetMetaData
                    String columnLabel = psmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性付志伟指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    /**
     * @return E
     * @Author YangPu
     * @Description //TODO 用于查询特殊值的方法
     * @Date 0:18 2021/7/10
     * @Param [conn, sql, args]
     **/
    public <E> E getValue(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
