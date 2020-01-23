import operations.*;
import tests.TestHandler;
import tests.TestRunner;

import java.util.Calendar;
import student.jz160143_ArticleOperations;
import student.jz160143_BuyerOperations;
import student.jz160143_CityOperations;
import student.jz160143_GeneralOperations;
import student.jz160143_OrderOperations;
import student.jz160143_ShopOperations;
import student.jz160143_TransactionOperations;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new jz160143_ArticleOperations(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new jz160143_BuyerOperations();
        CityOperations cityOperations = new jz160143_CityOperations();
        GeneralOperations generalOperations = new jz160143_GeneralOperations();
        OrderOperations orderOperations = new jz160143_OrderOperations();
        ShopOperations shopOperations = new jz160143_ShopOperations();
        TransactionOperations transactionOperations = new jz160143_TransactionOperations();
//
//        Calendar c = Calendar.getInstance();
//        c.clear();
//        c.set(2010, Calendar.JANUARY, 01);
//
//
//        Calendar c2 = Calendar.getInstance();
//        c2.clear();
//        c2.set(2010, Calendar.JANUARY, 01);
//
//        if(c.equals(c2)) System.out.println("jednako");
//        else System.out.println("nije jednako");

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
    }
}
