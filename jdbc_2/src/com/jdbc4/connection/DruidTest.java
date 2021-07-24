package com.jdbc4.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.DataSources;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.net.ProxySelector;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName DruidTest
 * @Description TODO
 * @Author YangPu
 * @Date 2021/7/23 16:04
 * @Version 1.0
 **/
public class DruidTest {
    @Test
    public  void getConnection() throws Exception {

        Properties pros = new Properties();

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);

        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
