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
import operations.CityOperations;

/**
 *
 * @author Zorana
 */
public class jz160143_CityOperations implements CityOperations{

    @Override
    public int createCity(String name) {
        Connection conn=DB.getInstance().getConnection();
        String insertQuery=" SET IDENTITY_INSERT City ON ; insert into City(id,nameCity) values(?,?)";
        String maxId="select max(id) from City";
        try(Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(maxId);
                PreparedStatement ps=conn.prepareStatement(insertQuery);){
            
            int nextId=0;
            if (rs.next()){
                nextId=rs.getInt(1);
            }
            nextId++;
            
            ps.setInt(1, nextId);
            ps.setString(2, name);
            
            if(ps.executeUpdate()>0)
                return nextId;
        
        
    }   catch (SQLException ex) {
            Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public List<Integer> getCities() {
        
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from City";
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

    @Override
    public int connectCities(int cityId1, int cityId2, int distance) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from ConnectedCities where idCity1=? and idCity2=?";
        String insertQuery = "SET IDENTITY_INSERT ConnectedCities ON; insert into ConnectedCities(id,idCity1,idCity2,distance) values(?,?,?,?)";
        String maxId = "select max(id) from ConnectedCities";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery);
                PreparedStatement ps2 = conn.prepareStatement(insertQuery);
                Statement st = conn.createStatement();) {

            rs1 = st.executeQuery(maxId);
            int idC = 0;
            if (rs1.next()) {
                idC = rs1.getInt(1);
            }
            idC++;

            ps1.setInt(1, cityId1);
            ps1.setInt(2, cityId2);
            rs2 = ps1.executeQuery();

            if (rs2.next()) {
                return -1;
            }

            ps2.setInt(1, idC);
            ps2.setInt(2, cityId1);
            ps2.setInt(3, cityId2);
            ps2.setInt(4, distance);

            if (ps2.executeUpdate() > 0) {
                return idC;
            }
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (rs1!=null){
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2!=null){
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }

    @Override
    public int disconnectCities(int cityId1, int cityId2) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from ConnectedCities where idCity1=? and idCity2=?";
        String deleteQuery = "delete from ConnectedCities where idCity1=? and idCity2=?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery);
                PreparedStatement ps2 = conn.prepareStatement(deleteQuery);) {

            ps1.setInt(1, cityId1);
            ps1.setInt(2, cityId2);
            rs2 = ps1.executeQuery();

            if (rs2.next()) {
                ps2.setInt(1, cityId1);
                ps2.setInt(2, cityId2);
                if (ps2.executeUpdate()>0)
                    return 1;
            }

           
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (rs1!=null){
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2!=null){
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }
    
    @Override
    public List<Integer> getConnectedCities(int cityId) {
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery1 = "select idCity1 from ConnectedCities where idCity2=?";
        String selectQuery2 = "select idCity2 from ConnectedCities where idCity1=?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery1);
                PreparedStatement ps2 = conn.prepareStatement(selectQuery2);) {

            ps1.setInt(1, cityId);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                rez.add(rs1.getInt(1));
            }

            ps2.setInt(1, cityId);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                rez.add(rs2.getInt(1));
            }

            return rez;

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    @Override
    public List<Integer> getShops(int cityId) {
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery1 = "select nameCity from City where id=?";
        String selectQuery2 = "select id from Shop where nameCity=?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;


        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery1);
                PreparedStatement ps2 = conn.prepareStatement(selectQuery2);) {

            ps1.setInt(1, cityId);
            rs1 = ps1.executeQuery();
            if (rs1.next()){
                ps2.setString(1, rs1.getString(1));
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    rez.add(rs2.getInt(1));
                }
                return rez;
            }
           

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

   
    
}
