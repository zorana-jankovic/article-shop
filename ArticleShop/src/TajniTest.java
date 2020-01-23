
import java.math.BigDecimal;
import java.util.Calendar;
import operations.ArticleOperations;
import operations.BuyerOperations;
import operations.CityOperations;
import operations.GeneralOperations;
import operations.OrderOperations;
import operations.ShopOperations;
import operations.TransactionOperations;
import org.junit.Assert;
import student.jz160143_ArticleOperations;
import student.jz160143_BuyerOperations;
import student.jz160143_CityOperations;
import student.jz160143_GeneralOperations;
import student.jz160143_OrderOperations;
import student.jz160143_ShopOperations;
import student.jz160143_TransactionOperations;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zorana
 */
public class TajniTest {
    
    public static void main(String[] args){
        ArticleOperations articleOperations = new jz160143_ArticleOperations(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new jz160143_BuyerOperations();
        CityOperations cityOperations = new jz160143_CityOperations();
        GeneralOperations generalOperations = new jz160143_GeneralOperations();
        OrderOperations orderOperations = new jz160143_OrderOperations();
        ShopOperations shopOperations = new jz160143_ShopOperations();
        TransactionOperations transactionOperations = new jz160143_TransactionOperations();
        
        generalOperations.eraseAll();
        
        Calendar initialTime = Calendar.getInstance();
        initialTime.clear();
        initialTime.set(2018, Calendar.JANUARY, 1);
        generalOperations.setInitialTime(initialTime);
        Calendar receivedTime = Calendar.getInstance();
        receivedTime.clear();
        receivedTime.set(2018, Calendar.JANUARY, 22);
        
        int cityB = cityOperations.createCity("B");
        int cityC1 = cityOperations.createCity("C1");
        int cityA = cityOperations.createCity("A");
        int cityC2 = cityOperations.createCity("C2");
        int cityC3 = cityOperations.createCity("C3");
        int cityC4 = cityOperations.createCity("C4");
        int cityC5 = cityOperations.createCity("C5");
        
        int cityN = cityOperations.createCity("N");
        
        cityOperations.connectCities(cityB, cityC1, 8);
        cityOperations.connectCities(cityC1, cityA, 10);
        cityOperations.connectCities(cityA, cityC2, 3);
        cityOperations.connectCities(cityC2, cityC3, 2);
        cityOperations.connectCities(cityC3, cityC4, 1);
        cityOperations.connectCities(cityC4, cityA, 3);
        cityOperations.connectCities(cityA, cityC5, 15);
        cityOperations.connectCities(cityC5, cityB, 2);
        
        
        cityOperations.connectCities(cityN, cityC2, 3);
        cityOperations.connectCities(cityN, cityC1, 2);
        
        int shopA = shopOperations.createShop("shopA", "A");
        int shopC2 = shopOperations.createShop("shopC2", "C2");
        int shopC3 = shopOperations.createShop("shopC3", "C3");
        
        int shopN = shopOperations.createShop("shopN", "N");
        
        shopOperations.setDiscount(shopA, 20);
        shopOperations.setDiscount(shopC2, 50);
        
        int laptop = articleOperations.createArticle(shopA, "laptop", 1000);
        int monitor = articleOperations.createArticle(shopC2, "monitor", 200);
        int stolica = articleOperations.createArticle(shopC3, "stolica", 100);
        int sto = articleOperations.createArticle(shopC3, "sto", 200);
        
        int serverpc = articleOperations.createArticle(shopC2, "serverpc", 12000);
        shopOperations.increaseArticleCount(serverpc, 10);

        
        shopOperations.increaseArticleCount(laptop, 10);
        shopOperations.increaseArticleCount(monitor, 10);
        shopOperations.increaseArticleCount(stolica, 10);
        shopOperations.increaseArticleCount(sto, 10);

        int buyer = buyerOperations.createBuyer("kupac", cityB);
        buyerOperations.increaseCredit(buyer, new BigDecimal("20000"));
        
        int orderN = buyerOperations.createOrder(buyer);
        orderOperations.addArticle(orderN, serverpc, 2);
        orderOperations.completeOrder(orderN);
        
        generalOperations.time(3);
        System.out.println("After 3 days location of orderN should be: " + cityN + " Your code returned: " + orderOperations.getLocation(orderN));
        Assert.assertEquals(orderOperations.getLocation(orderN), cityN);
        
        generalOperations.time(5);
        System.out.println("After 8 days location of orderN should be: " + cityC1 + " Your code returned: " + orderOperations.getLocation(orderN));
        Assert.assertEquals(orderOperations.getLocation(orderN), cityC1);
        
        generalOperations.time(8);
        System.out.println("After 16 days location of orderN should be: " + cityB + " Your code returned: " + orderOperations.getLocation(orderN));
        Assert.assertEquals(orderOperations.getLocation(orderN), cityB);
        System.out.println("State of order should be arrived, yours is: " + orderOperations.getState(orderN));
        Assert.assertTrue("arrived".equals(orderOperations.getState(orderN)));
        
        int order = buyerOperations.createOrder(buyer);
        orderOperations.addArticle(order, laptop, 5);
        orderOperations.addArticle(order, monitor, 4);
        orderOperations.addArticle(order, stolica, 10);
        orderOperations.addArticle(order, sto, 4);
        orderOperations.completeOrder(order);
        
        BigDecimal shopAAmount = new BigDecimal("5").multiply(new BigDecimal("1000")).setScale(3);
        BigDecimal shopAAmountWithDiscount = new BigDecimal("0.8").multiply(shopAAmount).setScale(3);
        BigDecimal shopC2Amount = new BigDecimal("4").multiply(new BigDecimal("200")).setScale(3);
        BigDecimal shopC2AmountWithDiscount = new BigDecimal("0.5").multiply(shopC2Amount).setScale(3);
        BigDecimal shopC3Amount = (new BigDecimal("10").multiply(new BigDecimal("100")))
                .add(new BigDecimal("4").multiply(new BigDecimal("200"))).setScale(3);
        BigDecimal shopC3AmountWithDiscount = shopC3Amount;

        BigDecimal amountWithoutDiscounts = shopAAmount.add(shopC2Amount).add(shopC3Amount).setScale(3);
        BigDecimal amountWithDiscounts = shopAAmountWithDiscount.add(shopC2AmountWithDiscount).add(shopC3AmountWithDiscount).setScale(3);

        BigDecimal systemProfit = amountWithDiscounts.multiply(new BigDecimal("0.03")).setScale(3);
        BigDecimal shopAAmountReal = shopAAmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        BigDecimal shopC2AmountReal = shopC2AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        BigDecimal shopC3AmountReal = shopC3AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        
        BigDecimal amountWithFinalDiscount = amountWithDiscounts.multiply(new BigDecimal("0.98")).setScale(3);

        generalOperations.time(5);
        System.out.println("After 5 days location of order should be: " + cityN + " Your code returned: " + orderOperations.getLocation(order));
        Assert.assertEquals(orderOperations.getLocation(order), cityN);
        
        generalOperations.time(4);
        System.out.println("After 9 days location of order should be: " + cityC1 + " Your code returned: " + orderOperations.getLocation(order));
        Assert.assertEquals(orderOperations.getLocation(order), cityC1);
        
        generalOperations.time(8);
        System.out.println("After 17 days location of order should be: " + cityB + " Your code returned: " + orderOperations.getLocation(order));
        Assert.assertEquals(orderOperations.getLocation(order), cityB);
        System.out.println("State of order should be arrived, yours is: " + orderOperations.getState(order));
        Assert.assertTrue("arrived".equals(orderOperations.getState(order)));
        
        System.out.println("Your final price of order should be: "+ amountWithFinalDiscount +" Your code returned: "+orderOperations.getFinalPrice(order));
        Assert.assertEquals(amountWithFinalDiscount, orderOperations.getFinalPrice(order));
        System.out.println("Your discounts for order should be: "+ amountWithoutDiscounts.subtract(amountWithFinalDiscount) +" Your code returned: "+ orderOperations.getDiscountSum(order));
        Assert.assertEquals(amountWithoutDiscounts.subtract(amountWithFinalDiscount),orderOperations.getDiscountSum(order));
        
        System.out.println("The ammount that buyer paid for both orders should be: "+ amountWithFinalDiscount.add(new BigDecimal("12000")) +" Your code returned: "+ transactionOperations.getBuyerTransactionsAmmount(buyer));
        Assert.assertEquals(amountWithFinalDiscount.add(new BigDecimal("12000")),transactionOperations.getBuyerTransactionsAmmount(buyer));
        
        System.out.println("The ammount that shopC2 earned in total should be: "+ shopC2AmountReal.add(new BigDecimal("12000").multiply(new BigDecimal("0.95"))) +" Your code returned: "+ transactionOperations.getShopTransactionsAmmount(shopC2));
        Assert.assertEquals(shopC2AmountReal.add(new BigDecimal("12000").multiply(new BigDecimal("0.95"))).setScale(3),transactionOperations.getShopTransactionsAmmount(shopC2));
        System.out.println("Your system profit should be: "+ systemProfit.add(new BigDecimal("12000").multiply(new BigDecimal("0.05"))).setScale(3) +" Your code returned: "+ transactionOperations.getSystemProfit());
        Assert.assertEquals(systemProfit.add(new BigDecimal("12000").multiply(new BigDecimal("0.05"))).setScale(3), transactionOperations.getSystemProfit());
    }
}
