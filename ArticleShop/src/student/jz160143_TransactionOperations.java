/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import student.JDBC.DB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.TransactionOperations;

/**
 *
 * @author Zorana
 */
public class jz160143_TransactionOperations implements TransactionOperations {

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int buyerId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select sum(amount) from Transactions where idBuyer=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, buyerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal ret = rs.getBigDecimal(1).setScale(3);
                return ret;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int shopId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select sum(amount) from Transactions where idShop=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal ret = rs.getBigDecimal(1);
                if (ret==null){
                    ret = new BigDecimal(0);
                }
                return ret.setScale(3);
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            
        }
        return new BigDecimal(-1);
    }

    @Override
    public List<Integer> getTransationsForBuyer(int buyerId) {
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from Transactions where idBuyer=?";
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
    public int getTransactionForBuyersOrder(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from Transactions where idOrder =? and idShop=null";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
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
        return -1;
    }

    @Override
    public int getTransactionForShopAndOrder(int orderId, int shopId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from Transactions where idOrder =? and idShop=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {
            ps.setInt(1, orderId);
            ps.setInt(2, shopId);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
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
        return -1;
    }

    @Override
    public List<Integer> getTransationsForShop(int shopId) {
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from Transactions where idShop=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {
            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            while (rs.next()) {
                rez.add(rs.getInt(1));
            }

            if (rez.size() == 0){
                return null;
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
    public Calendar getTimeOfExecution(int transactionId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select o.receiveTime,o.sentTime from Transactions t,Orders o where t.id=? and o.id=t.idOrder";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, transactionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Date rez=rs.getDate(1);
                if (rez == null){
                    Calendar r = Calendar.getInstance();
                    rez = rs.getDate(2);
                    if (rez==null){
                        return null;
                    }
                    r.setTime(rez);
                    return r;
                }
                    
                Calendar r = Calendar.getInstance();
                r.setTime(rez);
                return r;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return null;
    }

    @Override
    public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select sum(amount) from Transactions where idOrder=? and idShop=null";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select sum(amount) from Transactions where idOrder=? and idShop=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, orderId);
            ps.setInt(2, shopId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getTransactionAmount(int transactionId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select amount from Transactions where id=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, transactionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal ret = rs.getBigDecimal(1);
                if (ret == null){
                    ret = new BigDecimal(0);
                }
                return ret.setScale(3);
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getSystemProfit() {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select amount from Profit where id = 1";
        ResultSet rs = null;
        String initProfit = "SET IDENTITY_INSERT Profit ON; insert into Profit(id,amount) values(1,?)";
        
        
        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {
            PreparedStatement ps1 = conn.prepareStatement(initProfit);
            rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal ret = rs.getBigDecimal(1);
                
                return ret.setScale(3);
            }
            ps1.setBigDecimal(1, new BigDecimal(0).setScale(3));
            ps1.executeUpdate();
            return new BigDecimal(0).setScale(3);

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return new BigDecimal(-1);

    }
}
