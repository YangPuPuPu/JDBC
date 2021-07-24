package com.jdbc3.dao;

import com.jdbc2.bean.Customer;


import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
*此接口用于规范针对Customer表的常用操作
*/
public interface CustomerDAO { 
    /**
     * @Author YangPu
     * @Description //TODO 将cust对象添加到数据库中
     * @Date 13:48 2021/7/11
     * @Param [connection, cust]
     * @return void
     **/
    void insert(Connection conn, Customer cust);
    /**
     * @Author YangPu
     * @Description //TODO 针对于指定的id，删除表中的一条记录
     * @Date 13:49 2021/7/11
     * @Param [conn, id]
     * @return void
     **/
    void deleteById(Connection conn,int id);
    /**
     * @Author YangPu
     * @Description //TODO 针对内存中的cust对象，去修改数据表中指定的数据
     * @Date 13:51 2021/7/11
     * @Param [conn, cust]
     * @return void
     **/
    void update(Connection conn, Customer cust);
    /**
     * @Author YangPu
     * @Description //TODO 针对指定的id查询得到对应的Customer对象
     * @Date 13:52 2021/7/11
     * @Param [conn, id]
     * @return com.jdbc2.bean.Customer
     **/
    Customer getCustmerById(Connection conn, int id);
    /**
     * @Author YangPu
     * @Description //TODO 查询表中所有记录构成的集合
     * @Date 13:54 2021/7/11
     * @Param [conn]
     * @return java.util.List<com.jdbc2.bean.Customer>
     **/
    List<Customer> getAll(Connection conn);
    /**
     * @Author YangPu
     * @Description //TODO 返回数据表中的数据条目数
     * @Date 13:58 2021/7/11
     * @Param [conn]
     * @return java.lang.Long
     **/
    Long getCount(Connection conn);
    /**
     * @Author YangPu
     * @Description //TODO 返回数据表中最大的生日
     * @Date 14:11 2021/7/11
     * @Param [conn]
     * @return java.sql.Date
     **/
    Date getMaxBirth(Connection conn);
}
