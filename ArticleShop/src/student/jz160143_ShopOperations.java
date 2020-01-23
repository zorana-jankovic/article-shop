/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import student.JDBC.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.ShopOperations;

/**
 *
 * @author Zorana
 */
public class jz160143_ShopOperations implements ShopOperations{

    @Override
    public int createShop(String name, String cityName) {
        Connection conn=DB.getInstance().getConnection();
        String selectQuery="select * from Shop where nameShop=?";
        String insertQuery="SET IDENTITY_INSERT Shop ON; insert into Shop(id,nameShop,nameCity,discount) values(?,?,?,?)";
        String idQuery="select max(id) from Shop";
        ResultSet rs1=null;
        
        try(Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(idQuery);
            PreparedStatement ps1=conn.prepareStatement(selectQuery);
            PreparedStatement ps2=conn.prepareStatement(insertQuery);){
            
            ps1.setString(1, name);
            rs1=ps1.executeQuery();
            if (rs1.next())
                return -1;
            
            
            int maxId=0;
            if (rs.next()){
                maxId=rs.getInt(1);
            }
            maxId++;
            
            ps2.setInt(1, maxId);
            ps2.setString(2, name);
            ps2.setString(3, cityName);
            ps2.setInt(4,0);
           
            if (ps2.executeUpdate()>0){
                return maxId;
            }
           
            
        }
       catch (SQLException ex) {
            Logger.getLogger(jz160143_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return -1;
    }

    @Override
    public int setCity(int shopId, String cityName) {
        Connection conn = DB.getInstance().getConnection();
        String updateQuery = "Update Shop set nameCity=? where id=?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery);) {

            
            ps.setString(1, cityName);
            ps.setInt(2,shopId);

            if (ps.executeUpdate() > 0) {
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;

    }

    @Override
    public int getCity(int shopId) {

        Connection conn = DB.getInstance().getConnection();
        String getQuery = "select nameCity from  Shop  where id=?";
        String selectQuery = "select id from  City  where nameCity=?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try (PreparedStatement ps1 = conn.prepareStatement(getQuery);
                PreparedStatement ps2 = conn.prepareStatement(selectQuery);) {
            
            ps1.setInt(1, shopId);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setString(1, rs1.getString(1));
                rs2=ps2.executeQuery();
                if (rs2.next()){
                    return rs2.getInt(1);
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }

    @Override
    public int setDiscount(int shopId, int discountPercentage) {
        Connection conn = DB.getInstance().getConnection();
        String updateQuery = "Update Shop set discount=? where id=?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery);) {

            ps.setInt(1, discountPercentage);
            ps.setInt(2, shopId);

            if (ps.executeUpdate() > 0) {
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public int increaseArticleCount(int articleId, int increment) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery="select num from Artical where id=?";
        String updateQuery = "Update Artical set num=? where id=?";
        ResultSet rs=null;
        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery);
                PreparedStatement ps2 = conn.prepareStatement(updateQuery);) {

            ps1.setInt(1, articleId);
            rs=ps1.executeQuery();
            if (rs.next()){
                int count=rs.getInt(1);
                count=count+increment;
                ps2.setInt(1, count);
                ps2.setInt(2, articleId);
                if (ps2.executeUpdate()>0){
                    return count;
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public int getArticleCount(int articleId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery="select num from Artical where id=?";
        ResultSet rs=null;
        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery);) {

            ps1.setInt(1, articleId);
            rs=ps1.executeQuery();
            if (rs.next()){
                int count=rs.getInt(1);
                return count;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public List<Integer> getArticles(int shopId) {
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery="select id from Artical where shopId=?";
        ResultSet rs = null;
       

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            while (rs.next()) {
               rez.add(rs.getInt(1));
            }

            return rez;
            
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    @Override
    public int getDiscount(int shopId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select discount from Shop where id=?";
        ResultSet rs = null;
        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery);) {

            ps1.setInt(1, shopId);
            rs = ps1.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

}
