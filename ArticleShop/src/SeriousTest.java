

import operations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;

import student.jz160143_ArticleOperations;
import student.jz160143_BuyerOperations;
import student.jz160143_CityOperations;
import student.jz160143_GeneralOperations;
import student.jz160143_OrderOperations;
import student.jz160143_ShopOperations;
import student.jz160143_TransactionOperations;
import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
public class SeriousTest
{
	private static ArticleOperations articleOperations = new jz160143_ArticleOperations();
	private static BuyerOperations buyerOperations = new jz160143_BuyerOperations();
	private static CityOperations cityOperations = new jz160143_CityOperations();
	private static GeneralOperations generalOperations = new jz160143_GeneralOperations();
	private static OrderOperations orderOperations = new jz160143_OrderOperations();
	private static ShopOperations shopOperations = new jz160143_ShopOperations();
	private static TransactionOperations transactionOperations = new jz160143_TransactionOperations();
	
	private static Calendar date(int year, int month, int day)
	{
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(year, month, day);
		return date;
	}
	
	private static void test1()
	{
		generalOperations.eraseAll();
		
		// Set initial time
		Calendar initialTime = date(2019, Calendar.FEBRUARY, 1);
		generalOperations.setInitialTime(initialTime);
		
		// Create city map [c1 - c8]
		int c1 = cityOperations.createCity("C1");
		int c2 = cityOperations.createCity("C2");
		int c3 = cityOperations.createCity("C3");
		int c4 = cityOperations.createCity("C4");
		int c5 = cityOperations.createCity("C5");
		int c6 = cityOperations.createCity("C6");
		int c7 = cityOperations.createCity("C7");
		int c8 = cityOperations.createCity("C8");
		assertNotEquals(-1, cityOperations.connectCities(c1, c2, 11));
		assertNotEquals(-1, cityOperations.connectCities(c1, c3, 3));
		assertNotEquals(-1, cityOperations.connectCities(c1, c7, 4));
		assertNotEquals(-1, cityOperations.connectCities(c2, c3, 6));
		assertNotEquals(-1, cityOperations.connectCities(c3, c4, 4));
		assertNotEquals(-1, cityOperations.connectCities(c3, c5, 5));
		assertNotEquals(-1, cityOperations.connectCities(c4, c5, 7));
		assertNotEquals(-1, cityOperations.connectCities(c4, c6, 1));
		assertNotEquals(-1, cityOperations.connectCities(c4, c7, 8));
		assertNotEquals(-1, cityOperations.connectCities(c6, c7, 3));
		assertNotEquals(-1, cityOperations.connectCities(c6, c8, 7));
		assertNotEquals(-1, cityOperations.connectCities(c7, c8, 2));
		
		// Create shop 1 [S3]
		//  laptopS3
		//  monitorS3
		//  telefonS3
		int s3 = shopOperations.createShop("s3", "C3");
		int laptopS3 = articleOperations.createArticle(s3, "laptop", 3000);
		int monitorS3 = articleOperations.createArticle(s3, "monitor", 400);
		int telefonS3 = articleOperations.createArticle(s3, "telefon", 250);
		assertEquals(10, shopOperations.increaseArticleCount(laptopS3, 10));
		assertEquals(10, shopOperations.increaseArticleCount(monitorS3, 10));
		assertEquals(10, shopOperations.increaseArticleCount(telefonS3, 10));
		
		// Create shop 2 [S5]
		//  laptopS5
		//  telefonS5
		int s5 = shopOperations.createShop("s5", "C5");
		int laptopS5 = articleOperations.createArticle(s5, "laptop", 2700);
		int telefonS5 = articleOperations.createArticle(s5, "telefon", 200);
		assertEquals(10, shopOperations.increaseArticleCount(laptopS5, 10));
		assertEquals(10, shopOperations.increaseArticleCount(telefonS5, 10));
		
		// Create shop 3 [S8]
		//  telefonS8
		//  klimaS8
		int s8 = shopOperations.createShop("s8", "C8");
		int telefonS8 = articleOperations.createArticle(s8, "telefon", 300);
		int klimaS8 = articleOperations.createArticle(s8, "klima", 200);
		assertEquals(10, shopOperations.increaseArticleCount(telefonS8, 10));
		assertEquals(10, shopOperations.increaseArticleCount(klimaS8, 10));
		
		// Set discounts
		assertNotEquals(-1, shopOperations.setDiscount(s3, 15));
		assertNotEquals(-1, shopOperations.setDiscount(s8, 25));
		
		// Create buyer 1
		int b1 = buyerOperations.createBuyer("b1", c1);
		assertEquals(new BigDecimal(20000).setScale(3), buyerOperations.increaseCredit(b1, new BigDecimal(20000).setScale(3)));
		
		// Create buyer 2
		int b2 = buyerOperations.createBuyer("b2", c2);
		assertEquals(new BigDecimal(20000).setScale(3), buyerOperations.increaseCredit(b2, new BigDecimal(20000).setScale(3)));
		
		// Create buyer 3
		int b3 = buyerOperations.createBuyer("b3", c3);
		assertEquals(new BigDecimal(20000).setScale(3), buyerOperations.increaseCredit(b3, new BigDecimal(20000).setScale(3)));
		
		// Create buyer 4
		int b4 = buyerOperations.createBuyer("b4", c4);
		assertEquals(new BigDecimal(20000).setScale(3), buyerOperations.increaseCredit(b4, new BigDecimal(20000).setScale(3)));
		
		// Shop 1 [S3] {15%}
		//  laptopS3  - 3000 {2550}
		//  monitorS3 - 400  {340}
		//  telefonS3 - 250  {212.5}
		// Shop 2 [S5] {0%}
		//  laptopS5  - 2700 {2700}
		//  telefonS5 - 200  {200}
		// Shop 3 [S8] {25%}
		//  telefonS8 - 300  {225}
		//  klimaS8   - 200  {150}
		
		// Create order 1-1
		//  Buying from S5, S8
		//  Details:
		//   PlacedOn: 01-FEB-2019
		//   AssemblingOn: +13 days (at C8)
		//   ReceivingOn: +6 days (at C1)
		//   Path: C8-C7-C1 [2+4]
		//   FinaPrice: 9200.000
		//   DiscountSum: 100.000
		//   ShopGains: 0 8455 285
		//   SystemProfit: 460
		int order11 = buyerOperations.createOrder(b1);
		assertEquals(-1, orderOperations.addArticle(order11, laptopS5, 20));
		assertNotEquals(-1, orderOperations.addArticle(order11, laptopS5, 3));
		assertNotEquals(-1, orderOperations.addArticle(order11, telefonS5, 4));
		assertNotEquals(-1, orderOperations.addArticle(order11, klimaS8, 2));
		
		assertEquals("created", orderOperations.getState(order11));
		assertEquals(1, orderOperations.completeOrder(order11));
		assertEquals("sent", orderOperations.getState(order11));
		
		assertEquals(date(2019, Calendar.FEBRUARY, 1), orderOperations.getSentTime(order11));
		assertNull(orderOperations.getRecievedTime(order11));
		assertEquals(new BigDecimal(9200).setScale(3), orderOperations.getFinalPrice(order11));
		assertEquals(new BigDecimal(100).setScale(3), orderOperations.getDiscountSum(order11));
		assertEquals(new BigDecimal(9200).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b1));
		assertEquals(c8, orderOperations.getLocation(order11));
		
		// Create order 2-1
		//  Buying from S8
		//  Details:
		//   PlacedOn: 01-FEB-2019
		//   AssemblingOn: +0 days (at C8)
		//   ReceivingOn: +15 days (at C2)
		//   Path: C8-C7-C1-C3-C2 [2+4+3+6]
		//   FinaPrice: 450.000
		//   DiscountSum: 150.000
		//   ShopGains: 0 0 427.5
		//   SystemProfit: 22.5
		int order21 = buyerOperations.createOrder(b2);
		assertEquals(-1, orderOperations.addArticle(order21, klimaS8, 9));
		assertNotEquals(-1, orderOperations.addArticle(order21, klimaS8, 3));
		
		assertEquals("created", orderOperations.getState(order21));
		assertEquals(1, orderOperations.completeOrder(order21));
		assertEquals("sent", orderOperations.getState(order21));
		
		assertEquals(date(2019, Calendar.FEBRUARY, 1), orderOperations.getSentTime(order21));
		assertNull(orderOperations.getRecievedTime(order21));
		assertEquals(new BigDecimal(450).setScale(3), orderOperations.getFinalPrice(order21));
		assertEquals(new BigDecimal(150).setScale(3), orderOperations.getDiscountSum(order21));
		assertEquals(new BigDecimal(450).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b2));
		assertEquals(c8, orderOperations.getLocation(order21));
		
		// Create order 2-2
		//  Buying from S3, S8
		//  Details:
		//   PlacedOn: 01-FEB-2019
		//   AssemblingOn: +9 days (at C3)
		//   ReceivingOn: +6 days (at C2)
		//   Path: C3-C2 [6]
		//   FinaPrice: 10875.000
		//   DiscountSum: 2025.000
		//   ShopGains: 9690 0 641.25
		//   SystemProfit: 543.75
		int order22 = buyerOperations.createOrder(b2);
		assertEquals(-1, orderOperations.addArticle(order22, telefonS5, 7));
		assertNotEquals(-1, orderOperations.addArticle(order22, laptopS3, 4));
		assertNotEquals(-1, orderOperations.addArticle(order22, telefonS8, 3));
		
		assertEquals("created", orderOperations.getState(order22));
		assertEquals(1, orderOperations.completeOrder(order22));
		assertEquals("sent", orderOperations.getState(order22));
		
		assertEquals(date(2019, Calendar.FEBRUARY, 1), orderOperations.getSentTime(order22));
		assertNull(orderOperations.getRecievedTime(order22));
		assertEquals(new BigDecimal(10875).setScale(3), orderOperations.getFinalPrice(order22));
		assertEquals(new BigDecimal(2025).setScale(3), orderOperations.getDiscountSum(order22));
		assertEquals(new BigDecimal(11325).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b2));
		assertEquals(c3, orderOperations.getLocation(order22));
		
		// Create order 2-3
		//  Buying from S8
		//  Details:
		//   PlacedOn: 01-FEB-2019
		//   AssemblingOn: +0 days (at C8)
		//   ReceivingOn: +15 days (at C2)
		//   Path: C8-C7-C1-C3-C2 [2+4+3+6]
		//   FinaPrice: 441.000
		//   DiscountSum: 159.000
		//   ShopGains: 0 0 427.5
		//   SystemProfit: 13.5
		int order23 = buyerOperations.createOrder(b2);
		assertEquals(-1, orderOperations.addArticle(order23, klimaS8, 9));
		assertNotEquals(-1, orderOperations.addArticle(order23, klimaS8, 3));
		
		assertEquals("created", orderOperations.getState(order23));
		assertEquals(1, orderOperations.completeOrder(order23));
		assertEquals("sent", orderOperations.getState(order23));
		
		assertEquals(date(2019, Calendar.FEBRUARY, 1), orderOperations.getSentTime(order23));
		assertNull(orderOperations.getRecievedTime(order23));
		assertEquals(new BigDecimal(441).setScale(3), orderOperations.getFinalPrice(order23));
		assertEquals(new BigDecimal(159).setScale(3), orderOperations.getDiscountSum(order23));
		assertEquals(new BigDecimal(11766).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b2));
		assertEquals(c8, orderOperations.getLocation(order23));
		
		// Pass 3 days
		assertEquals(date(2019, Calendar.FEBRUARY, 4), generalOperations.time(3));
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c7, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c7, orderOperations.getLocation(order23));
		
		assertNotEquals(-1, shopOperations.setDiscount(s8, 20));
		
		// Shop 1 [S3] {15%}
		//  laptopS3  - 3000 {2550}
		//  monitorS3 - 400  {340}
		//  telefonS3 - 250  {212.5}
		// Shop 2 [S5] {0%}
		//  laptopS5  - 2700 {2700}
		//  telefonS5 - 200  {200}
		// Shop 3 [S8] {20%}
		//  telefonS8 - 300  {240}
		//  klimaS8   - 200  {160}
		
		// Create order 3-1
		//  Buying from S3, S5, S8
		//  Details:
		//   PlacedOn: 04-FEB-2019
		//   AssemblingOn: +9 days (at C3)
		//   ReceivingOn: +0 days (at C3)
		//   Path: C3 [0]
		//   FinaPrice: 6020.000
		//   DiscountSum: 980.000
		//   ShopGains: 4845 570 304
		//   SystemProfit: 301
		int order31 = buyerOperations.createOrder(b3);
		assertEquals(-1, orderOperations.addArticle(order31, laptopS3, 7));
		assertNotEquals(-1, orderOperations.addArticle(order31, laptopS3, 2));
		assertNotEquals(-1, orderOperations.addArticle(order31, telefonS5, 3));
		assertNotEquals(-1, orderOperations.addArticle(order31, klimaS8, 2));
		
		assertEquals("created", orderOperations.getState(order31));
		assertEquals(1, orderOperations.completeOrder(order31));
		assertEquals("sent", orderOperations.getState(order31));
		
		assertEquals(date(2019, Calendar.FEBRUARY, 4), orderOperations.getSentTime(order31));
		assertNull(orderOperations.getRecievedTime(order31));
		assertEquals(new BigDecimal(6020).setScale(3), orderOperations.getFinalPrice(order31));
		assertEquals(new BigDecimal(980).setScale(3), orderOperations.getDiscountSum(order31));
		assertEquals(new BigDecimal(6020).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b3));
		assertEquals(c3, orderOperations.getLocation(order31));
		
		// Create order 4-1
		//  Buying from S8
		//  Details:
		//   PlacedOn: 04-FEB-2019
		//   AssemblingOn: +0 days (at C8)
		//   ReceivingOn: +6 days (at C4)
		//   Path: C8-C7-C6-C4 [2+3+1]
		//   FinaPrice: 720.000
		//   DiscountSum: 225.000
		//   ShopGains: 0 0 684
		//   SystemProfit: 36
		int order41 = buyerOperations.createOrder(b4);
		assertEquals(-1, orderOperations.addArticle(order41, klimaS8, 1));
		assertNotEquals(-1, orderOperations.addArticle(order41, telefonS8, 3));
		
		assertEquals("created", orderOperations.getState(order41));
		assertEquals(1, orderOperations.completeOrder(order41));
		assertEquals("sent", orderOperations.getState(order41));
		
		assertEquals(date(2019, Calendar.FEBRUARY, 4), orderOperations.getSentTime(order41));
		assertNull(orderOperations.getRecievedTime(order41));
		assertEquals(new BigDecimal(720).setScale(3), orderOperations.getFinalPrice(order41));
		assertEquals(new BigDecimal(180).setScale(3), orderOperations.getDiscountSum(order41));
		assertEquals(new BigDecimal(720).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b4));
		assertEquals(c8, orderOperations.getLocation(order41));
		
		// Recheck final prices, discount sums & buyer spendings
		assertEquals(new BigDecimal(9200).setScale(3), orderOperations.getFinalPrice(order11));
		assertEquals(new BigDecimal(100).setScale(3), orderOperations.getDiscountSum(order11));
		assertEquals(new BigDecimal(450).setScale(3), orderOperations.getFinalPrice(order21));
		assertEquals(new BigDecimal(150).setScale(3), orderOperations.getDiscountSum(order21));
		assertEquals(new BigDecimal(10875).setScale(3), orderOperations.getFinalPrice(order22));
		assertEquals(new BigDecimal(2025).setScale(3), orderOperations.getDiscountSum(order22));
		assertEquals(new BigDecimal(441).setScale(3), orderOperations.getFinalPrice(order23));
		assertEquals(new BigDecimal(159).setScale(3), orderOperations.getDiscountSum(order23));
		assertEquals(new BigDecimal(6020).setScale(3), orderOperations.getFinalPrice(order31));
		assertEquals(new BigDecimal(980).setScale(3), orderOperations.getDiscountSum(order31));
		assertEquals(new BigDecimal(720).setScale(3), orderOperations.getFinalPrice(order41));
		assertEquals(new BigDecimal(180).setScale(3), orderOperations.getDiscountSum(order41));
		assertEquals(new BigDecimal(9200).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b1));
		assertEquals(new BigDecimal(11766).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b2));
		assertEquals(new BigDecimal(6020).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b3));
		assertEquals(new BigDecimal(720).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b4));
		
		assertEquals(new BigDecimal(0).setScale(3), transactionOperations.getSystemProfit());
		
		// Simulation
		
		// Order11:
		//   ShopGains: 0 8455 285
		//   SystemProfit: 460
		// Order21:
		//   ShopGains: 0 0 427.5
		//   SystemProfit: 22.5
		// Order22:
		//   ShopGains: 9690 0 641.25
		//   SystemProfit: 543.75
		// Order23:
		//   ShopGains: 0 0 427.5
		//   SystemProfit: 13.5
		// Order31:
		//   ShopGains: 4845 570 304
		//   SystemProfit: 301
		// Order41:
		//   ShopGains: 0 0 684
		//   SystemProfit: 36
		// Total:
		//   ShopGains: 14535 9025 2769.25
		//   SystemProfit: 1376.75
		
		// Order11 (A:+10d, R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+12d [C7-C1-C3-C2 | 3+3+6])
		// Order22 (A:+6d,  R:+6d  [C3-C2       | 6])
		// Order23 (A:done, R:+12d [C7-C1-C3-C2 | 3+3+6])
		// Order31 (A:+9d,  R:+0d  [C3          | 0])
		// Order41 (A:+0d,  R:+6d  [C8-C7-C6-C4 | 2+3+1])
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 5), generalOperations.time(1));
		// Order11 (A:+9d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+11d [C7-C1-C3-C2 | 2+3+6])
		// Order22 (A:+5d,  R:+6d  [C3-C2       | 6])
		// Order23 (A:done, R:+11d [C7-C1-C3-C2 | 2+3+6])
		// Order31 (A:+8d,  R:+0d  [C3          | 0])
		// Order41 (A:done, R:+5d  [C8-C7-C6-C4 | 1+3+1])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c7, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c7, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c8, orderOperations.getLocation(order41));
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 6), generalOperations.time(1));
		// Order11 (A:+8d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+10d [C7-C1-C3-C2 | 1+3+6])
		// Order22 (A:+4d,  R:+6d  [C3-C2       | 6])
		// Order23 (A:done, R:+10d [C7-C1-C3-C2 | 1+3+6])
		// Order31 (A:+7d,  R:+0d  [C3          | 0])
		// Order41 (A:done, R:+4d  [C7-C6-C4    | 3+1])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c7, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c7, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c7, orderOperations.getLocation(order41));
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 7), generalOperations.time(1));
		// Order11 (A:+7d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+9d  [C1-C3-C2    | 3+6])
		// Order22 (A:+3d,  R:+6d  [C3-C2       | 6])
		// Order23 (A:done, R:+9d  [C1-C3-C2    | 3+6])
		// Order31 (A:+6d,  R:+0d  [C3          | 0])
		// Order41 (A:done, R:+3d  [C7-C6-C4    | 2+1])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c1, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c1, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c7, orderOperations.getLocation(order41));
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 8), generalOperations.time(1));
		// Order11 (A:+6d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+8d  [C1-C3-C2    | 2+6])
		// Order22 (A:+2d,  R:+6d  [C3-C2       | 6])
		// Order23 (A:done, R:+8d  [C1-C3-C2    | 2+6])
		// Order31 (A:+5d,  R:+0d  [C3          | 0])
		// Order41 (A:done, R:+2d  [C7-C6-C4    | 1+1])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c1, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c1, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c7, orderOperations.getLocation(order41));
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 9), generalOperations.time(1));
		// Order11 (A:+5d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+7d  [C1-C3-C2    | 1+6])
		// Order22 (A:+1d,  R:+6d  [C3-C2       | 6])
		// Order23 (A:done, R:+7d  [C1-C3-C2    | 1+6])
		// Order31 (A:+4d,  R:+0d  [C3          | 0])
		// Order41 (A:done, R:+1d  [C6-C4       | 1])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c1, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c1, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c6, orderOperations.getLocation(order41));
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 10), generalOperations.time(1));
		// Order11 (A:+4d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+6d  [C3-C2       | 6])
		// Order22 (A:done, R:+6d  [C3-C2       | 6])
		// Order23 (A:done, R:+6d  [C3-C2       | 6])
		// Order31 (A:+3d,  R:+0d  [C3          | 0])
		// Order41 (A:done, R:rcv  [C4          | 0])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c3, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c3, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c4, orderOperations.getLocation(order41));
		
		// Checkpoint 1
		assertNull(orderOperations.getRecievedTime(order11));
		assertNull(orderOperations.getRecievedTime(order21));
		assertNull(orderOperations.getRecievedTime(order22));
		assertNull(orderOperations.getRecievedTime(order23));
		assertNull(orderOperations.getRecievedTime(order31));
		assertEquals(date(2019, Calendar.FEBRUARY, 10), orderOperations.getRecievedTime(order41));
		assertEquals(new BigDecimal("0").setScale(3), transactionOperations.getShopTransactionsAmmount(s3));
		assertEquals(new BigDecimal("0").setScale(3), transactionOperations.getShopTransactionsAmmount(s5));
		assertEquals(new BigDecimal("684").setScale(3), transactionOperations.getShopTransactionsAmmount(s8));
		assertEquals(new BigDecimal("36").setScale(3), transactionOperations.getSystemProfit());
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 11), generalOperations.time(1));
		// Order11 (A:+3d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+5d  [C3-C2       | 5])
		// Order22 (A:done, R:+5d  [C3-C2       | 5])
		// Order23 (A:done, R:+5d  [C3-C2       | 5])
		// Order31 (A:+2d,  R:+0d  [C3          | 0])
		// Order41 (A:done, R:rcv  [C4          | 0])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c3, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c3, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c4, orderOperations.getLocation(order41));
		
		// Pass 2 days
		assertEquals(date(2019, Calendar.FEBRUARY, 13), generalOperations.time(2));
		// Order11 (A:+1d,  R:+6d  [C8-C7-C1    | 2+4])
		// Order21 (A:done, R:+3d  [C3-C2       | 3])
		// Order22 (A:done, R:+3d  [C3-C2       | 3])
		// Order23 (A:done, R:+3d  [C3-C2       | 3])
		// Order31 (A:done, R:rcv  [C3          | 0])
		// Order41 (A:done, R:rcv  [C4          | 0])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c3, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c3, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c4, orderOperations.getLocation(order41));
		
		// Checkpoint 2
		assertNull(orderOperations.getRecievedTime(order11));
		assertNull(orderOperations.getRecievedTime(order21));
		assertNull(orderOperations.getRecievedTime(order22));
		assertNull(orderOperations.getRecievedTime(order23));
		assertEquals(date(2019, Calendar.FEBRUARY, 13), orderOperations.getRecievedTime(order31));
		assertEquals(date(2019, Calendar.FEBRUARY, 10), orderOperations.getRecievedTime(order41));
		assertEquals(new BigDecimal("4845").setScale(3), transactionOperations.getShopTransactionsAmmount(s3));
		assertEquals(new BigDecimal("570").setScale(3), transactionOperations.getShopTransactionsAmmount(s5));
		assertEquals(new BigDecimal("988").setScale(3), transactionOperations.getShopTransactionsAmmount(s8));
		assertEquals(new BigDecimal("337").setScale(3), transactionOperations.getSystemProfit());
		
		// Pass 2 days
		assertEquals(date(2019, Calendar.FEBRUARY, 15), generalOperations.time(2));
		// Order11 (A:done, R:+5d  [C8-C7-C1    | 1+4])
		// Order21 (A:done, R:+1d  [C3-C2       | 1])
		// Order22 (A:done, R:+1d  [C3-C2       | 1])
		// Order23 (A:done, R:+1d  [C3-C2       | 1])
		// Order31 (A:done, R:rcv  [C3          | 0])
		// Order41 (A:done, R:rcv  [C4          | 0])
		assertEquals(c8, orderOperations.getLocation(order11));
		assertEquals(c3, orderOperations.getLocation(order21));
		assertEquals(c3, orderOperations.getLocation(order22));
		assertEquals(c3, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c4, orderOperations.getLocation(order41));
		
		// Pass 1 day
		assertEquals(date(2019, Calendar.FEBRUARY, 16), generalOperations.time(1));
		// Order11 (A:done, R:+4d  [C7-C1       | 4])
		// Order21 (A:done, R:rcv  [C2          | 0])
		// Order22 (A:done, R:rcv  [C2          | 0])
		// Order23 (A:done, R:rcv  [C2          | 0])
		// Order31 (A:done, R:rcv  [C3          | 0])
		// Order41 (A:done, R:rcv  [C4          | 0])
		assertEquals(c7, orderOperations.getLocation(order11));
		assertEquals(c2, orderOperations.getLocation(order21));
		assertEquals(c2, orderOperations.getLocation(order22));
		assertEquals(c2, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c4, orderOperations.getLocation(order41));
		
		// Checkpoint 3
		assertNull(orderOperations.getRecievedTime(order11));
		assertEquals(date(2019, Calendar.FEBRUARY, 16), orderOperations.getRecievedTime(order21));
		assertEquals(date(2019, Calendar.FEBRUARY, 16), orderOperations.getRecievedTime(order22));
		assertEquals(date(2019, Calendar.FEBRUARY, 16), orderOperations.getRecievedTime(order23));
		assertEquals(date(2019, Calendar.FEBRUARY, 13), orderOperations.getRecievedTime(order31));
		assertEquals(date(2019, Calendar.FEBRUARY, 10), orderOperations.getRecievedTime(order41));
		assertEquals(new BigDecimal("14535").setScale(3), transactionOperations.getShopTransactionsAmmount(s3));
		assertEquals(new BigDecimal("570").setScale(3), transactionOperations.getShopTransactionsAmmount(s5));
		assertEquals(new BigDecimal("2484.25").setScale(3), transactionOperations.getShopTransactionsAmmount(s8));
		assertEquals(new BigDecimal("916.75").setScale(3), transactionOperations.getSystemProfit());
		
		// Pass 2 days
		assertEquals(date(2019, Calendar.FEBRUARY, 18), generalOperations.time(2));
		// Order11 (A:done, R:+2d  [C7-C1       | 2])
		// Order21 (A:done, R:rcv  [C2          | 0])
		// Order22 (A:done, R:rcv  [C2          | 0])
		// Order23 (A:done, R:rcv  [C2          | 0])
		// Order31 (A:done, R:rcv  [C3          | 0])
		// Order41 (A:done, R:rcv  [C4          | 0])
		assertEquals(c7, orderOperations.getLocation(order11));
		assertEquals(c2, orderOperations.getLocation(order21));
		assertEquals(c2, orderOperations.getLocation(order22));
		assertEquals(c2, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c4, orderOperations.getLocation(order41));
		
		// Pass 5 days
		assertEquals(date(2019, Calendar.FEBRUARY, 23), generalOperations.time(5));
		// Order11 (A:done, R:rcv  [C1          | 0])
		// Order21 (A:done, R:rcv  [C2          | 0])
		// Order22 (A:done, R:rcv  [C2          | 0])
		// Order23 (A:done, R:rcv  [C2          | 0])
		// Order31 (A:done, R:rcv  [C3          | 0])
		// Order41 (A:done, R:rcv  [C4          | 0])
		assertEquals(c1, orderOperations.getLocation(order11));
		assertEquals(c2, orderOperations.getLocation(order21));
		assertEquals(c2, orderOperations.getLocation(order22));
		assertEquals(c2, orderOperations.getLocation(order23));
		assertEquals(c3, orderOperations.getLocation(order31));
		assertEquals(c4, orderOperations.getLocation(order41));
		
		// Checkpoint 4
		assertEquals(date(2019, Calendar.FEBRUARY, 20), orderOperations.getRecievedTime(order11));
		assertEquals(date(2019, Calendar.FEBRUARY, 16), orderOperations.getRecievedTime(order21));
		assertEquals(date(2019, Calendar.FEBRUARY, 16), orderOperations.getRecievedTime(order22));
		assertEquals(date(2019, Calendar.FEBRUARY, 16), orderOperations.getRecievedTime(order23));
		assertEquals(date(2019, Calendar.FEBRUARY, 13), orderOperations.getRecievedTime(order31));
		assertEquals(date(2019, Calendar.FEBRUARY, 10), orderOperations.getRecievedTime(order41));
		assertEquals(new BigDecimal("14535").setScale(3), transactionOperations.getShopTransactionsAmmount(s3));
		assertEquals(new BigDecimal("9025").setScale(3), transactionOperations.getShopTransactionsAmmount(s5));
		assertEquals(new BigDecimal("2769.25").setScale(3), transactionOperations.getShopTransactionsAmmount(s8));
		assertEquals(new BigDecimal("1376.75").setScale(3), transactionOperations.getSystemProfit());
		
		// Pass 90 days
                Calendar time = generalOperations.time(90);
		assertEquals(date(2019, Calendar.MAY, 24), time);
		
		// Recheck final prices, discount sums & buyer spendings
		assertEquals(new BigDecimal(9200).setScale(3), orderOperations.getFinalPrice(order11));
		assertEquals(new BigDecimal(100).setScale(3), orderOperations.getDiscountSum(order11));
		assertEquals(new BigDecimal(450).setScale(3), orderOperations.getFinalPrice(order21));
		assertEquals(new BigDecimal(150).setScale(3), orderOperations.getDiscountSum(order21));
		assertEquals(new BigDecimal(10875).setScale(3), orderOperations.getFinalPrice(order22));
		assertEquals(new BigDecimal(2025).setScale(3), orderOperations.getDiscountSum(order22));
		assertEquals(new BigDecimal(441).setScale(3), orderOperations.getFinalPrice(order23));
		assertEquals(new BigDecimal(159).setScale(3), orderOperations.getDiscountSum(order23));
		assertEquals(new BigDecimal(6020).setScale(3), orderOperations.getFinalPrice(order31));
		assertEquals(new BigDecimal(980).setScale(3), orderOperations.getDiscountSum(order31));
		assertEquals(new BigDecimal(720).setScale(3), orderOperations.getFinalPrice(order41));
		assertEquals(new BigDecimal(180).setScale(3), orderOperations.getDiscountSum(order41));
		assertEquals(new BigDecimal(9200).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b1));
		assertEquals(new BigDecimal(11766).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b2));
		assertEquals(new BigDecimal(6020).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b3));
		assertEquals(new BigDecimal(720).setScale(3), transactionOperations.getBuyerTransactionsAmmount(b4));
		
		generalOperations.eraseAll();
	}
	
	public static void main(String[] args)
	{
		int total = 0;
		int succ = 0;
		
		try { ++total; test1(); ++succ; }
		catch (Throwable tr) { tr.printStackTrace(); }
		
		System.out.println("+Tests passed: " + succ + "/" + total);
		System.out.println("-Tests failed: " + (total - succ) + "/" + total);
	}
}
