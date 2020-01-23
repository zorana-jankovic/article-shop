/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import student.JDBC.DB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.BuyerOperations;

/**
 *
 * @author Zorana
 */
public class jz160143_BuyerOperations implements BuyerOperations{

    @Override
    public int createBuyer(String name, int cityId) {
        Connection conn=DB.getInstance().getConnection();
        String insertQuery="SET IDENTITY_INSERT Buyer ON; insert into Buyer(id,nameBuyer,credit,cityId)  values(?,?,?,?)";
        String idQuery="select max(id) from Buyer";
        try(Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(idQuery);
            PreparedStatement ps=conn.prepareStatement(insertQuery);){
            
            int idBuyer=0;
            if(rs.next()){
                idBuyer=rs.getInt(1);
            }
            
            idBuyer++;
            
            ps.setInt(1, idBuyer);
            ps.setString(2, name);
            ps.setInt(3, 0);
            ps.setInt(4, cityId);
            
            if (ps.executeUpdate()>0){
                return idBuyer;
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }

    @Override
    public int setCity(int buyerId, int cityId) {
        Connection conn=DB.getInstance().getConnection();
        String updateQuery="Update Buyer set cityId=? where id=?";
        try(PreparedStatement ps=conn.prepareStatement(updateQuery);){
            
            ps.setInt(1, cityId);
            ps.setInt(2,buyerId);
            
            if(ps.executeUpdate()>0)
                return 1;
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public int getCity(int buyerId) {
        Connection conn = DB.getInstance().getConnection();
        String getQuery = "select cityId from Buyer  where id=?";
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(getQuery);) {
            ps.setInt(1, buyerId);
            rs=ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }

    @Override
    public BigDecimal increaseCredit(int buyerId, BigDecimal credit) {
        Connection conn=DB.getInstance().getConnection();
        String getQuery="select credit from Buyer where id=?";
        String setQuery="update Buyer set credit=? where id=?";
        ResultSet rs=null;
        try (PreparedStatement ps1 = conn.prepareStatement(getQuery);
             PreparedStatement ps2=conn.prepareStatement(setQuery);) {
            
            BigDecimal oldCredit=new BigDecimal(0);
            ps1.setInt(1, buyerId);
            rs=ps1.executeQuery();
            if (rs.next()) {
                oldCredit=rs.getBigDecimal(1);
            }
            
            credit=credit.add(oldCredit);
            
            ps2.setBigDecimal(1, credit);
            ps2.setInt(2, buyerId);
            
            if (ps2.executeUpdate()>0){
                return credit;
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new BigDecimal(0);
    }

    @Override
    public int createOrder(int buyerId) {
        Connection conn = DB.getInstance().getConnection();
        String insertQuery = "SET IDENTITY_INSERT Orders ON; insert into Orders(id,stateOrder,buyerId,dodatniPopust) values (?,?,?,?)";
        String idQuery = "select max(id) from Orders";

        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(idQuery);
                PreparedStatement ps = conn.prepareStatement(insertQuery);) {

            int idOrder = 0;
            if (rs.next()) {
                idOrder = rs.getInt(1);
            }

            idOrder++;

            ps.setInt(1, idOrder);
            ps.setString(2, "created");
            ps.setInt(3, buyerId);
            ps.setInt(4, 0);
            if (ps.executeUpdate() > 0) {
                return idOrder;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public List<Integer> getOrders(int buyerId) {
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from Orders where buyerId=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {
            ps.setInt(1, buyerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                rez.add(rs.getInt(1));
            }

            return rez;
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;

    }

    @Override
    public BigDecimal getCredit(int buyerId) {
        Connection conn=DB.getInstance().getConnection();
        String getQuery="select credit from Buyer where id=?";
        ResultSet rs=null;
        try(PreparedStatement ps=conn.prepareStatement(getQuery);){
            
            ps.setInt(1, buyerId);
            rs=ps.executeQuery();
            if (rs.next()){
                return rs.getBigDecimal(1);
            
            }
            
        
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return new BigDecimal(0);
    }
    
     @Override
     public List<Integer> getAllBuyers(){
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from Buyer";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {
            rs = ps.executeQuery();
            while (rs.next()) {
                rez.add(rs.getInt(1));
            }

            return rez;
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;

     }
}
