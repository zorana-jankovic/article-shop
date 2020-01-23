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
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.GeneralOperations;

/**
 *
 * @author Zorana
 */
public class jz160143_GeneralOperations implements GeneralOperations {

    public static Calendar time;

    @Override
    public void setInitialTime(Calendar time) {
        this.time = Calendar.getInstance();
        this.time.clear();
        this.time.setTime(time.getTime());

    }

    @Override
    public Calendar time(int days) {
        time.add(Calendar.DATE, days);
        Connection conn = DB.getInstance().getConnection();
        String getOrders = "select id from Orders where stateOrder=? ";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(getOrders);) {

            ps.setString(1, "sent");
            rs = ps.executeQuery();

            while (rs.next()) {
                move(rs.getInt(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return time;
    }

    @Override
    public Calendar getCurrentTime() {
        return time;
    }

    @Override
    public void eraseAll() {
        Connection conn = DB.getInstance().getConnection();
        String delete;
        String initProfit = "SET IDENTITY_INSERT Profit ON; insert into Profit(id,amount) values(1,?)";
        
        try (Statement st = conn.createStatement();
                PreparedStatement ps1 = conn.prepareStatement(initProfit);) {
            delete = "delete from ArticleOrder";
            st.executeUpdate(delete);
            delete = "delete from Artical";
            st.executeUpdate(delete);
            delete = "delete from Transactions";
            st.executeUpdate(delete);
            delete = "delete from Orders";
            st.executeUpdate(delete);
            delete = "delete from Buyer";
            st.executeUpdate(delete);
            delete = "delete from ConnectedCities";
            st.executeUpdate(delete);
            delete = "delete from City";
            st.executeUpdate(delete);
            delete = "delete from Shop";
            st.executeUpdate(delete);
            delete = "delete from Profit";
            st.executeUpdate(delete);
            ps1.setBigDecimal(1, new BigDecimal(0).setScale(3));
            ps1.executeUpdate();
    
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*private int makeTransaction(int orderId) {
        Connection conn=DB.getInstance().getConnection();
        String newTransaction="insert into Transaction(id,idShop,idOrder,amount,datum) values(?,?,?,?,?)";
        String maxId="select max(id) from Transation";
        String findShops="select a.shopId, SUM((a.price*ar.num)-(a.price*ar.num-ar.discount/100)) from Artical a,ArticleOrder ar where ar.idOrder=? and a.id=ar.idArticle GROUP BY a.shopId ";
        String  setProfit="UPDATE Profit set amount=amount+?";
        String dohvatiProcenat="select dodatniPopust from Orders where o.idOrder=?";
        ResultSet rs1=null;
        ResultSet rs2=null;
        ResultSet rs3=null;
        ResultSet rs4=null;
        
       
        try( Statement st=conn.createStatement();
               PreparedStatement ps1=conn.prepareStatement(newTransaction);
                PreparedStatement ps2=conn.prepareStatement(findShops);
                 PreparedStatement ps3=conn.prepareStatement(setProfit);
                    PreparedStatement ps4=conn.prepareStatement(dohvatiProcenat);){
        
            ps2.setInt(1, orderId);
            rs1=ps2.executeQuery();
            int shop,next=0;
            BigDecimal amount=new BigDecimal(0);
            while(rs1!=null){
                shop=rs1.getInt(1);
                amount=rs1.getBigDecimal(2);
                
                rs2=st.executeQuery(maxId);
                if(rs2.next())
                    next=rs2.getInt(1);
                next++;
                
                amount.multiply(new BigDecimal(0.95));
                ps1.setInt(1, next);
                ps1.setInt(2, shop);
                ps1.setInt(3, orderId);
                ps1.setBigDecimal(4, amount);
                ps1.setDate(5, (java.sql.Date) time.getTime());
                
                if (ps1.executeUpdate()<=0)
                    return -1;
                
                ps4.setInt(1, orderId);
                rs4=ps4.executeQuery();
                if (rs4.next()){
                    int popust=rs4.getInt(1);
                    popust=5-popust;
                    amount.multiply(new BigDecimal(popust/100));
                    ps3.setBigDecimal(1, amount);
                    if (ps3.executeUpdate()>0){
                        return 1;
                    }
                }
                
            }
            
         
        
        }   catch (SQLException ex) {
            Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (rs1!=null){
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2!=null){
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs3!=null){
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs4!=null){
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }
     */
    private int move(int orderId) {
        ArrayList<Integer> citiesId = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        ArrayList<ArrayList<Integer>> putevi = new ArrayList<>();
        String selectQuery = "select id from City";
        String getConnectedCities = "select * from ConnectedCities";
        String findBuyer = "select b.cityId from Orders o,Buyer b where b.id=o.buyerId AND o.id=?";
        String checkBuyerCity = "select b.cityId from Buyer b, Shop s,City c where b.cityId=c.id and b.cityId=? and c.nameCity=s.nameCity";
        String selectArticles = "select c.id from ArticleOrder ar, Artical a, Shop s, City c where ar.idOrder=? and ar.idArticle=a.id and a.shopId=s.id and c.nameCity=s.nameCity ";
        String getSentTime = "select sentTime from Orders where id=?";
        String updateCity = "update Orders set cityId=? where id=?";
        String updateCityArrived = "update Orders set cityId=?,receiveTime=?,stateOrder=?,makeTransactions=?  where id=?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        ResultSet rs6 = null;

        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery);
                PreparedStatement ps2 = conn.prepareStatement(getConnectedCities);
                PreparedStatement ps3 = conn.prepareStatement(findBuyer);
                PreparedStatement ps4 = conn.prepareStatement(checkBuyerCity);
                PreparedStatement ps5 = conn.prepareStatement(selectArticles);
                PreparedStatement ps6 = conn.prepareStatement(getSentTime);
                PreparedStatement ps7 = conn.prepareStatement(updateCity);
                PreparedStatement ps8 = conn.prepareStatement(updateCityArrived);) {

            int max = 0;
            int pom = 0;
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                pom = rs1.getInt(1);
                citiesId.add(pom);
                if (pom > max) {
                    max = pom;
                }
            }

            max++;
            int matrica[][] = new int[max][max];
            int matricaDij[][] = new int[max][max];
            for (int i = 0; i < max; i++) {
                for (int j = 0; j < max; j++) {
                    matrica[i][j] = 100000;
                    matricaDij[i][j] = 0;
                    if (i == j) {
                        matrica[i][j] = 0;
                    }
                }
            }

            rs2 = ps2.executeQuery();

            while (rs2.next()) {
                int city1 = rs2.getInt(2);
                int city2 = rs2.getInt(3);
                int distance = rs2.getInt(4);

                if (citiesId.contains(city1) && citiesId.contains(city2)) {
                    matrica[city1][city2] = distance;
                    matrica[city2][city1] = distance;

                    matricaDij[city1][city2] = distance;
                    matricaDij[city2][city1] = distance;
                }
            }

            for (int k = 0; k < max; k++) {
                for (int i = 0; i < max; i++) {
                    for (int j = 0; j < max; j++) {
                        if (matrica[i][k] + matrica[k][j] < matrica[i][j]) {
                            matrica[i][j] = matrica[i][k] + matrica[k][j];
                        }
                    }
                }
            }
            ArrayList<Integer> path = new ArrayList<>();
            path = callDijkstra(orderId, matricaDij, putevi);

            int ind, min, minCity = 0;
            ps3.setInt(1, orderId);
            rs3 = ps3.executeQuery();
            if (rs3.next()) {
                int buyerCity = rs3.getInt(1);
                ps4.setInt(1, buyerCity);
                rs4 = ps4.executeQuery();
                if (rs4.next()) {
                    minCity = buyerCity;
                } else {
                    ind = path.get(0);
                    min = matrica[buyerCity][ind];
                    minCity = ind;
                    for (int i = 1; i < path.size(); i++) {
                        ind = path.get(i);
                        if (matrica[buyerCity][ind] < min) {
                            min = matrica[buyerCity][ind];
                            minCity = ind;
                        }
                    }
                }

                ps5.setInt(1, orderId);
                rs5 = ps5.executeQuery();
                max = 0;
                while (rs5.next()) {
                    int cityId = rs5.getInt(1);
                    if (matrica[minCity][cityId] > max) {
                        max = matrica[minCity][cityId];
                    }
                }

                ArrayList<Integer> put = putevi.get(minCity);

                Date sentDate = null;
                ps6.setInt(1, orderId);
                rs6 = ps6.executeQuery();
                if (rs6.next()) {
                    sentDate = rs6.getDate(1);
                }

                int city1 = -1, city2 = -1, dis, i;
                Date expectedArrive = null;
                Date sent = sentDate;
                long ltime = sent.getTime() + max * 24 * 60 * 60 * 1000 - 1;
                expectedArrive = new Date(ltime);
                
                city2=buyerCity;
                
                if (put.size()==0){
                    if (!time.getTime().after(expectedArrive)) {
                        return 1;
                    } 
                }
                
                for (i = put.size() - 1; i > 0; i--) {
                    city1 = put.get(i);
                    city2 = put.get(i - 1);
                    dis = matrica[city1][city2];
                    max = max + dis;

                    sent = sentDate;
                    ltime = sent.getTime() + max * 24 * 60 * 60 * 1000 - 1;

                    expectedArrive = new Date(ltime);
                    if (time.getTime().after(expectedArrive)) {

                    } else {
                        break;
                    }
                }

                if (i > 0) {
                    ps7.setInt(1, city1);
                    ps7.setInt(2, orderId);
                    if (ps7.executeUpdate() > 0) {
                        return 1;
                    }
                } else {
                    ps8.setInt(1, city2);
                    expectedArrive.setTime(expectedArrive.getTime() + 1);
                    ps8.setDate(2, new java.sql.Date(expectedArrive.getTime()));
                    ps8.setString(3, "arrived");
                    ps8.setInt(4, 1);
                    ps8.setInt(5, orderId);
                    if (ps8.executeUpdate() > 0) {
                        return 1;
                    }

                    // makeTransaction(orderId);
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
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
            if (rs3 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs4 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return -1;
    }

    private ArrayList<Integer> callDijkstra(int orderId, int[][] matrica, ArrayList<ArrayList<Integer>> pom) {
        Connection conn = DB.getInstance().getConnection();
        ArrayList<Integer> shopsCityArticles = new ArrayList<>();
        ArrayList<Integer> cityShops = new ArrayList<>();

        ArrayList<Integer> tek1, tek2;
        ArrayList<Integer> path = new ArrayList<>();
        String findBuyer = "select b.cityId from Orders o,Buyer b where b.id=o.buyerId AND o.id=?";
        String selectArticles = "select c.id from ArticleOrder ar, Artical a, Shop s, City c where ar.idOrder=? and ar.idArticle=a.id and a.shopId=s.id and c.nameCity=s.nameCity ";

        String citiesShops = "select c.id from Shop s, City c where c.nameCity=s.nameCity ";

        PreparedStatement ps = null;
        Statement ps1 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try {

            ps = conn.prepareStatement(findBuyer);
            ps.setInt(1, orderId);
            rs1 = ps.executeQuery();
            int start = -1;
            if (rs1.next()) {
                start = rs1.getInt(1);

                pom = dijkstra(matrica, start, pom);
                ps = conn.prepareStatement(selectArticles);
                ps.setInt(1, orderId);
                rs2 = ps.executeQuery();
                while (rs2.next()) {
                    int pomid = rs2.getInt(1);
                    if (!shopsCityArticles.contains(pomid)) {
                        shopsCityArticles.add(pomid);
                    }
                }

                ps1 = conn.createStatement();
                rs3 = ps1.executeQuery(citiesShops);

                while (rs3.next()) {
                    cityShops.add(rs3.getInt(1));
                }
                // prodji kroz sve gradove
//                for (int i = 1; i < pom.size(); i++) {
//                    // ako je to startni preskoci
//                    if (i == start) {
//                        continue;
//                    }
//                    // ako se nije porucivalo iz tog grada preskoci
//                    if (!shopsArticles.contains(i)) {
//                        continue;
//                    }
//                    ArrayList<Integer> tek = pom.get(i);
//
//                    // prodji kroz sve postale puteve
//                    for (int j = 0; j < tek.size(); j++) {
//                        int elem = tek.get(j);
//                        int i2;
//                        for (i2 = 1; i2 < pom.size(); i2++) {
//                            // ako je to startni
//                            if (i2 == start) {
//                                continue;
//                            }
//                            // ako se nije porucivalo iz tog grada preskoci
//                            if (!shopsArticles.contains(i2)) {
//                                continue;
//                            }
//
//                            if (!pom.get(i2).contains(elem)) {
//                                break;
//                            }
//                        }
//                        if (i2 == pom.size() && cityShops.contains(elem) && !path.contains(elem)) {
//                            path.add(elem);
//                        }
//                    }
//                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
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

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return cityShops;
    }

    private ArrayList<ArrayList<Integer>> dijkstra(int[][] adjacencyMatrix,
            int startVertex, ArrayList<ArrayList<Integer>> putevi) {
        int nVertices = adjacencyMatrix[0].length;
        int[] shortestDistances = new int[nVertices];

        boolean[] added = new boolean[nVertices];

        for (int vertexIndex = 0; vertexIndex < nVertices;
                vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        shortestDistances[startVertex] = 0;

        int[] parents = new int[nVertices];

        parents[startVertex] = -1;

        for (int i = 1; i < nVertices; i++) {
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0;
                    vertexIndex < nVertices;
                    vertexIndex++) {
                if (!added[vertexIndex]
                        && shortestDistances[vertexIndex]
                        < shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            added[nearestVertex] = true;
            for (int vertexIndex = 0;
                    vertexIndex < nVertices;
                    vertexIndex++) {
                int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0
                        && ((shortestDistance + edgeDistance)
                        < shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance
                            + edgeDistance;
                }
            }
        }

        return printSolution(startVertex, shortestDistances, parents, putevi);

    }

    private ArrayList<ArrayList<Integer>> printSolution(int startVertex, int[] distances, int[] parents, ArrayList<ArrayList<Integer>> putevi) {
        int nVertices = distances.length;

        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            putevi.add(new ArrayList());
        }

        for (int vertexIndex = 1; vertexIndex < nVertices; vertexIndex++) {
            if (vertexIndex != startVertex) {
                printPath(vertexIndex, parents, putevi.get(vertexIndex));
            }
        }

        return putevi;
    }

    private void printPath(int currentVertex, int[] parents, ArrayList<Integer> put) {

        if (currentVertex == -1) {
            return;
        }
        printPath(parents[currentVertex], parents, put);
        put.add(currentVertex);
    }

}
