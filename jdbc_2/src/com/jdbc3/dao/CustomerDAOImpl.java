package com.jdbc3.dao;

import com.jdbc2.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @ClassName CustomerDAOImpl
 * @Description TODO 关于Customer表实现Customer接口继承BaseDAO的实现类
 * @Author YangPu
 * @Date 2021/7/22 16:03
 * @Version 1.0
 **/
    public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO {
        @Override
        public void insert(Connection conn, com.jdbc2.bean.Customer cust) {
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            update(conn,sql,cust.getName(),cust.getEmail(),cust.getBirth());
        }

        @Override
        public void deleteById(Connection conn, int id) {
            String sql = "delete from customers where id = ?";
            update(conn,sql,id);
        }

        @Override
        public void update(Connection conn, com.jdbc2.bean.Customer cust) {
            String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
            update(conn,sql,cust.getName(),cust.getEmail(),cust.getBirth(),cust.getId());
        }

        @Override
        public Customer getCustmerById(Connection conn, int id) {
            String sql = "select id,name,email,birth from customers where id= ?";
            Customer customer = getInstance(conn, sql, id);
            return customer;
        }

        @Override
        public List<Customer> getAll(Connection conn) {
            String sql = "select id,name,email,birth from customers";
            List<Customer> list = getForList(conn, sql);
            return list;
        }

        @Override
        public Long getCount(Connection conn) {
            String sql = "select count(*) from customers";
            return getValue(conn,sql);
        }

        @Override
        public Date getMaxBirth(Connection conn) {
            String sql = "select max(birth) from customers";
            return getValue(conn,sql);
        }
    }
