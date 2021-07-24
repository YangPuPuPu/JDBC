package com.jdbc1.transaction;

import com.jdbc1.util.JDBCUtils;

import java.sql.Connection;

/**
 * @ClassName ConnectionTest
 * @Description TODO 连接测试
 * @Author YangPu
 * @Date 2021/7/7 20:11
 * @Version 1.0
 **/
public class ConnectionTest {
    public static void main(String[] args) throws Exception {
        testGetConnection();
    }
    public static void testGetConnection() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
