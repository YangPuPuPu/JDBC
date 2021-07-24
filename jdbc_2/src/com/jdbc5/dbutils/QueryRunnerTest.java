package com.jdbc5.dbutils;

/*
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 *
 */

import com.jdbc2.bean.Customer;
import com.jdbc4.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * @ClassName QueryRunnerTest
 * @Description TODO 测试QueryRunner
 * @Author YangPu
 * @Date 2021/7/23 17:10
 * @Version 1.0
 **/
public class QueryRunnerTest {
    //测试插入
    @Test
    public void testInsert() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            int insertCount = runner.update(conn, sql, "李杰杰", "987654321@qq.com", "2001-06-06");
            System.out.println(insertCount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    //测试查询
    /*
    * BeanHander:是ResultSetHandler接口的实现类，用于封装表中的一条记录
    */
    @Test
    public void testQuery1() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 19);
            System.out.println(customer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }


    }

    /*
     * BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合。
     */
    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id < ?";

            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);

            List<Customer> list = runner.query(conn, sql, handler, 19);
            list.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /*
     * MapHander:是ResultSetHandler接口的实现类，对应封装表中的一条记录(键值对)
     * 将字段及相应字段的值作为map中key和value
     */
    @Test
    public void testQuery3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 19);
            System.out.println(map);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }


    }

    /*
     * MapListHander:是ResultSetHandler接口的实现类，对应封装表中的多条记录(键值对)
     * 将字段及相应字段的值作为map中key和value，将map添加到list中
     */
    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id < ?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> list = runner.query(conn, sql, handler, 19);
            list.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
    /*
    * ScalarHandler:用于查询特殊值
    */
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            String sql = "select COUNT(*) from customers";

            ScalarHandler handler = new ScalarHandler();

            Long count = (Long) runner.query(conn, sql, handler);

            System.out.println(count);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @Test
    public void testQuery6() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            String sql = "select max(birth) from customers";

            ScalarHandler handler = new ScalarHandler();

           Date maxBirth = (Date) runner.query(conn, sql, handler);

            System.out.println(maxBirth);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
    /*
    * 自定义一个ResultSetHandler类
    */
    @Test
    public void testQuery7() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            String sql = "select id,name,email,birth from customers where id = ?";

            ResultSetHandler<Customer> handler =  new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
                    System.out.println("handle");
                    return null;
                }
            };

            Customer cust = runner.query(conn, sql, handler, 18);

            System.out.println(cust);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
}
