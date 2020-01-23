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
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.ArticleOperations;

/**
 *
 * @author Zorana
 */
public class jz160143_ArticleOperations implements ArticleOperations{

    @Override
    public int createArticle(int shopId, String articleName, int articlePrice) {
        Connection conn=DB.getInstance().getConnection();
        String insertQuery="SET IDENTITY_INSERT Artical ON; insert into Artical(id,nameArtical,price,num,shopId) values(?,?,?,?,?)";
        String idQuery="select max(id) from Artical";
        
        try(Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(idQuery);
            PreparedStatement ps=conn.prepareStatement(insertQuery);){
            
            int maxId=0;
            if (rs.next()){
                maxId=rs.getInt(1);
            }
            maxId++;
            
            ps.setInt(1, maxId);
            ps.setString(2, articleName);
            ps.setInt(3, articlePrice);
            ps.setInt(4,0);
            ps.setInt(5,shopId);
            
            if  (ps.executeUpdate()>0){
                return maxId;
            }
            
        }
       catch (SQLException ex) {
            Logger.getLogger(jz160143_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return -1;
    }
}


