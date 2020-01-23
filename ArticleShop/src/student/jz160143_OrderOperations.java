/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import student.JDBC.DB;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.OrderOperations;
import static student.jz160143_GeneralOperations.time;

/**
 *
 * @author Zorana
 */
public class jz160143_OrderOperations implements OrderOperations {

    /**
     * Adds article to order. It adds articles only if there are enough of them
     * in shop. If article is in order already, it only increases count.
     *
     * @param orderId order id
     * @param articleId article id
     * @param count number of articles to be added
     * @return item id (item contains information about number of article
     * instances in particular order), -1 if failure
     */
    @Override
    public int addArticle(int orderId, int articleId, int count) {
        Connection conn = DB.getInstance().getConnection();
        String selectQueryA = "select num,shopId from Artical where id=?";
        String updateArtical = "update Artical set num=? where id=?";
        String selectQueryD = "select discount from Shop where id=?";
        String selectQueryO = "select num,id from ArticleOrder where idArticle=? and idOrder=?";
        String updateQuery = "update ArticleOrder set num=? where idArticle=? and idOrder=?";
        String insertQuery = "SET IDENTITY_INSERT ArticleOrder ON; insert into ArticleOrder(id,idArticle,idOrder,num,discount) values(?,?,?,?,?)";
        String maxId = "select max(id) from ArticleOrder";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;

        try (PreparedStatement ps1 = conn.prepareStatement(selectQueryA);
                PreparedStatement ps2 = conn.prepareStatement(selectQueryO);
                PreparedStatement ps3 = conn.prepareStatement(updateQuery);
                PreparedStatement ps4 = conn.prepareStatement(insertQuery);
                PreparedStatement ps5 = conn.prepareStatement(maxId);
                PreparedStatement ps6 = conn.prepareStatement(selectQueryD);
                PreparedStatement ps7 = conn.prepareStatement(updateArtical);) {

            ps1.setInt(1, articleId);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                if (rs1.getInt(1) >= count) {
                    int ukupno = rs1.getInt(1);
                    ps7.setInt(1, ukupno - count);
                    ps7.setInt(2, articleId);
                    if (ps7.executeUpdate() > 0) {

                        ps2.setInt(1, articleId);
                        ps2.setInt(2, orderId);
                        rs2 = ps2.executeQuery();

                        if (rs2.next()) {
                            int pom = rs2.getInt(1);
                            count = count + rs2.getInt(1);
                            ps3.setInt(1, count);
                            ps3.setInt(2, articleId);
                            ps3.setInt(3, orderId);
                            if (ps3.executeUpdate() > 0) {
                                return rs2.getInt(2);
                            }
                        } else {
                            rs3 = ps5.executeQuery();
                            int nextId = 0;
                            if (rs3.next()) {
                                nextId = rs3.getInt(1);
                            }
                            nextId++;

                            ps6.setInt(1, rs1.getInt(2));
                            rs4 = ps6.executeQuery();
                            int dis = 0;
                            if (rs4.next()) {
                                dis = rs4.getInt(1);
                            }

                            ps4.setInt(1, nextId);
                            ps4.setInt(2, articleId);
                            ps4.setInt(3, orderId);
                            ps4.setInt(4, count);
                            ps4.setInt(5, dis);

                            if (ps4.executeUpdate() > 0) {
                                return nextId;
                            }
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs3 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }

    @Override
    public int removeArticle(int orderId, int articleId) {
        Connection conn = DB.getInstance().getConnection();
        String selectAOCount = "select num from ArticleOrder where idArticle=? and idOrder=?";
        String deleteQuery = "delete from ArticleOrder where idArticle=? and idOrder=?";
        String selectQueryA = "select num,shopId from Artical where id=?";
        String updateArtical = "update Artical set num=? where id";
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try (PreparedStatement ps1 = conn.prepareStatement(deleteQuery);
                PreparedStatement ps2 = conn.prepareStatement(selectQueryA);
                PreparedStatement ps3 = conn.prepareStatement(updateArtical);
                PreparedStatement ps4 = conn.prepareStatement(selectAOCount);) {

            ps4.setInt(1, articleId);
            ps4.setInt(2, orderId);
            rs1 = ps4.executeQuery();
            if (rs1.next()) {
                ps1.setInt(1, articleId);
                ps1.setInt(2, orderId);
                if (ps1.executeUpdate() > 0) {
                    ps2.setInt(1, articleId);
                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        int nova = rs1.getInt(1) + rs2.getInt(1);
                        ps3.setInt(1, nova);
                        ps3.setInt(2, articleId);
                        if (ps3.executeUpdate() > 0) {
                            return 1;
                        };

                    }

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return -1;
        }
    }

    @Override
    public List<Integer> getItems(int orderId) {
        List<Integer> rez = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from ArticleOrder where idOrder=?";
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {
            ps.setInt(1, orderId);
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

    private int finishOrder(int orderId) {
        ArrayList<Integer> citiesId = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select id from City";
        String getConnectedCities = "select * from ConnectedCities";
        String findBuyer = "select b.cityId from Orders o,Buyer b where b.id=o.buyerId AND o.id=?";
        String checkBuyerCity = "select b.cityId from Buyer b, Shop s,City c where b.cityId=c.id and b.cityId=? and c.nameCity=s.nameCity";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        try (PreparedStatement ps1 = conn.prepareStatement(selectQuery);
                PreparedStatement ps2 = conn.prepareStatement(getConnectedCities);
                PreparedStatement ps3 = conn.prepareStatement(findBuyer);
                PreparedStatement ps4 = conn.prepareStatement(checkBuyerCity);) {

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
            ArrayList<ArrayList<Integer>> putevi = new ArrayList<>();
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

            }
            return minCity;

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

    @Override
    public int completeOrder(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String getBuyer = "select buyerId from Orders where id=?";
        String getCredit = "select credit from Buyer where id=?";
        String pay = "update  Buyer set credit=? where id=?";
        String order = "update Orders set stateOrder=? , sentTime=? where id=?";
        String orderCity = "update Orders set cityId=?,dodatniPopust=? where id=?";
        String check = "select SUM(t.amount) from Transactions t where t.idBuyer=? and DATEDIFF(day,?,t.datum)<30";
     
        ResultSet rs1 = null, rs2 = null, rs3 = null;

        try (PreparedStatement ps1 = conn.prepareStatement(getBuyer);
                PreparedStatement ps2 = conn.prepareStatement(getCredit);
                PreparedStatement ps3 = conn.prepareStatement(check);
                PreparedStatement payment = conn.prepareStatement(pay);
                PreparedStatement orderchange = conn.prepareStatement(order);
                PreparedStatement cityUpdate = conn.prepareStatement(orderCity);) {

            ps1.setInt(1, orderId);
            rs1 = ps1.executeQuery();

            if (rs1.next()) {
                int buyer = rs1.getInt(1);
                ps2.setInt(1, buyer);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    int mon = rs2.getInt(1);
                    BigDecimal money = new BigDecimal(mon);
                    
                    Date pomDate = new java.sql.Date(time.getTime().getTime());

                    ps3.setInt(1, buyer);
                    ps3.setDate(2, pomDate);
                    rs3 = ps3.executeQuery();
                    BigDecimal ukupno = new BigDecimal(0);
                    int dodatniPopust = 0;
                    if (rs3.next()) {
                        ukupno = rs3.getBigDecimal(1);
                        if (ukupno!= null && ukupno.doubleValue() > 10000) {
                            dodatniPopust = 2;
                        }
                    }
                    BigDecimal suma = getFinalPrice(orderId);
                    float discout = suma.floatValue() * ((float)dodatniPopust)/100;
                    suma = suma.subtract(new BigDecimal(discout)).setScale(3);
                    
                    if (money.compareTo(suma) != -1) {
                        BigDecimal ostalo = money.subtract(suma);
                        int pom = ostalo.toBigInteger().intValue();
                        payment.setInt(1, pom);
                        payment.setInt(2, buyer);
                        if (payment.executeUpdate() > 0) {
                            orderchange.setString(1, "sent");
                            orderchange.setDate(2, new java.sql.Date(jz160143_GeneralOperations.time.getTime().getTime()));
                            orderchange.setInt(3, orderId);
                            if (orderchange.executeUpdate() > 0) {
                                int city = finishOrder(orderId);
                                cityUpdate.setInt(1, city);
                                cityUpdate.setInt(2, dodatniPopust);
                                cityUpdate.setInt(3, orderId);
                                if (cityUpdate.executeUpdate() > 0) {
                                    return makeTransaction(orderId,dodatniPopust);
                                }
                            }

                        }

                    }
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
        }

        return -1;
    }

    @Override
    public BigDecimal getFinalPrice(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "{ ? = call SP_FINAL_PRICE(?)}";
        String complete = "select * from Orders where id=?";
        ResultSet r = null;
        try (CallableStatement cs = conn.prepareCall(sql);
                PreparedStatement ps1 = conn.prepareStatement(complete);) {

            //ps1.setString(1, "sent"); //kada je kompeltirana???
            ps1.setInt(1, orderId);
            r = ps1.executeQuery();
            if (!(r.next())) {
                return new BigDecimal(-1);
            }

            cs.registerOutParameter(1, Types.FLOAT);
            cs.setInt(2, orderId);
            cs.execute();

            BigDecimal ret = cs.getBigDecimal(1).setScale(3);
            return ret;

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getDiscountSum(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String price = "SELECT price FROM Artical WHERE id=?";
        String getArticles = "SELECT idArticle,num,discount FROM ArticleOrder WHERE idOrder=?";
        String complete = "select * from Orders where stateOrder<>? and id=?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try (PreparedStatement ps1 = conn.prepareStatement(getArticles);
                PreparedStatement ps2 = conn.prepareStatement(price);
                PreparedStatement ps3 = conn.prepareStatement(complete)) {

            ps3.setString(1, "created"); //da li je tada kompletirana???
            ps3.setInt(2, orderId);
            rs1 = ps3.executeQuery();
            if (!rs1.next()) {
                return new BigDecimal(-1);
            }
            ps1.setInt(1, orderId);
            rs1 = ps1.executeQuery();

            int idArtical = 0, num = 0, discount = 0, cena = 0;
            BigDecimal finalPrice = new BigDecimal(0), finalPriceWithDiscount = new BigDecimal(0);
            while (rs1.next()) {
                idArtical = rs1.getInt(1);
                num = rs1.getInt(2);
                discount = rs1.getInt(3);

                ps2.setInt(1, idArtical);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    cena = rs2.getInt(1);
                    finalPrice = finalPrice.add(new BigDecimal((num * cena) - (num * cena) * (discount / 100)));
                }
            }

            finalPriceWithDiscount = getFinalPrice(orderId);

            BigDecimal rez = new BigDecimal(0);
            rez = finalPrice.subtract(finalPriceWithDiscount).setScale(3);
            return rez;

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return new BigDecimal(-1);
    }

    @Override
    public String getState(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select stateOrder from Orders where id=?";
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String ret = rs.getString(1);
                return ret;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return null;
    }

    @Override
    public Calendar getSentTime(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select sentTime from Orders where id=?";
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            Calendar rez = Calendar.getInstance();
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getDate(1) == null) {
                    return null;
                }
                java.util.Date date = new java.util.Date(rs.getDate(1).getTime());
                rez.clear();
                rez.setTime(date);
                return rez;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return null;
    }

    @Override
    public Calendar getRecievedTime(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select receiveTime from Orders where id=?";
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            Calendar rez = Calendar.getInstance();
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Date date = rs.getDate(1);
                if (date!=null){
                    rez.setTime(date);
                    return rez;
                }
                return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return null;
    }

    @Override
    public int getBuyer(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select buyerId from Orders where id=?";
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return -1;
    }

    /**
     * Gets location for an order. If order is assembled and order is moving
     * from city C1 to city C2 then location of an order is city C1. If order is
     * not yet assembled then location of the order is location of the shop
     * (associated with order) that is closest to buyer's city. If order is in
     * state "created" then location is -1.
     *
     * @param orderId order's id
     * @return id of city, -1 if failure
     */
    @Override
    public int getLocation(int orderId) {
        Connection conn = DB.getInstance().getConnection();
        String selectQuery = "select stateOrder,cityId from Orders where id=?";
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(selectQuery);) {

            String state = null;
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                state = rs.getString(1);
                switch (state) {
                    case "created":
                        return -1;
                    case "sent":
                        return rs.getInt(2);
                    case "arrived":
                        return rs.getInt(2);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
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
                    if (!shopsCityArticles.contains(pomid)){
                        shopsCityArticles.add(pomid);
                    }
                }
                ps1=conn.createStatement();
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

    private int makeTransaction(int orderId, int dodatniPopust) {
        Connection conn = DB.getInstance().getConnection();
        String newTransaction = "SET IDENTITY_INSERT Transactions ON; insert into Transactions(id,idBuyer,idOrder,amount,datum,dodatniPopust) values(?,?,?,?,?,?)";
        String maxId = "select max(id) from Transactions";
        String findBuyer = "select o.buyerId, SUM((a.price*ar.num)-(a.price*ar.num)*(cast(ar.discount as decimal(10,3))/100)) from Artical a,ArticleOrder ar,Orders o where o.id=? and ar.idOrder=? and a.id=ar.idArticle GROUP BY o.buyerId ";
     
        String updateOrder = "update Orders set dodatniPopust = ? where id = ?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        
        try (Statement st = conn.createStatement();
                PreparedStatement ps1 = conn.prepareStatement(newTransaction);
                PreparedStatement ps2 = conn.prepareStatement(findBuyer);
                PreparedStatement ps4 = conn.prepareStatement(updateOrder);) {

            ps2.setInt(1, orderId);
            ps2.setInt(2, orderId);
            rs1 = ps2.executeQuery();
            int buyer, next = 0;
            BigDecimal amount = new BigDecimal(0).setScale(3);
            while (rs1.next()) {
                buyer = rs1.getInt(1);
                amount = rs1.getBigDecimal(2);

                rs2 = st.executeQuery(maxId);
                if (rs2.next()) {
                    next = rs2.getInt(1);
                }
                next++;

                
                float discout = amount.floatValue() * ((float)dodatniPopust)/100;
                amount = amount.subtract(new BigDecimal(discout)).setScale(3);
                ps1.setInt(1, next);
                ps1.setInt(2, buyer);
                ps1.setInt(3, orderId);
                ps1.setBigDecimal(4, amount);
                ps1.setDate(5, new java.sql.Date(time.getTime().getTime()));
                ps1.setInt(6, dodatniPopust);

                if (ps1.executeUpdate() <= 0) {
                    return -1;
                }
                ps4.setInt(1, dodatniPopust);
                ps4.setInt(2, orderId);
                if (ps4.executeUpdate() <= 0) {
                    return -1;
                }

            }

            return 1;

        } catch (SQLException ex) {
            Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs3 != null) {
                try {
                    rs3.close();
                } catch (SQLException ex) {
                    Logger.getLogger(jz160143_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }
}
