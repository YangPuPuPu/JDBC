package com.jdnc5.blob;

import com.jdbc3.bean.Customer;
import com.jdbc3.util.JDBCUtils;

import java.io.*;
import java.sql.*;

/**
 * @ClassName BlobTest
 * @Description TODO使用PreparedStatement操作Blob类型的数据
 * @Author YangPu
 * @Date 2021/7/7 10:46
 * @Version 1.0
 **/
public class BlobTest {
    public static void main(String[] args) {
//        testInsert();
        textQuery();
    }
    /**
     * @Author YangPu
     * @Description //TODO 向数据表中插入Blob类型的字段
     * @Date 11:03 2021/7/7
     * @Param []
     * @return void
     **/
    public static void testInsert()  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers(name ,email,birth,photo)values(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,"王冰冰");
            ps.setObject(2,"WangBinBin.email");
            ps.setObject(3,"1998-02-02");
            FileInputStream fis = new FileInputStream(new File("img.png"));
            ps.setBlob(4,fis);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }
    /**
     * @Author YangPu
     * @Description //TODO 查询blob字段
     * @Date 14:11 2021/7/7
     * @Param []
     * @return void
     **/
    public static void textQuery(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select * from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,24);
            rs = ps.executeQuery();
            if(rs.next()){
                //方式一
    //            int id = rs.getInt(1);
    //            String name = rs.getString(2);
    //            String email = rs.getString(3);
    //            Date date = rs.getDate(4);
                //方式二
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                //将Blob类型的字段下载下来，以文件的方式保存在本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("wangbinbin.jpg");
                byte [] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer))!=-1){
                    fos.write(buffer,0,len );
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
            try {
                if (is!=null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos!=null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
