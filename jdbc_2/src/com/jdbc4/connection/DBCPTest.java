package com.jdbc4.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @ClassName DBCPTest
 * @Description TODO测试DBCP的数据库连接池技术
 * @Author YangPu
 * @Date 2021/7/23 11:36
 * @Version 1.0
 **/
public class DBCPTest {
    @Test
    public void testGetConnection() throws SQLException {
        //创建了DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("root");

        //还可以设置其他涉及数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);


        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
