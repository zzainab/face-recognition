/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBL;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class CustomersDBTest {
    
    public CustomersDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addCustomers method, of class CustomersDB.
     */
    @Test
    public void testAddCustomers() {
        System.out.println("addCustomers");
        Customers obj = new Customers();
        obj.setName("Test");
        obj.setAddress("Test");
        obj.setMobile(1234567890);
        CustomersDB instance = new CustomersDB();
        int expResult = 1;
        int result = instance.addCustomers(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addAccountdet method, of class CustomersDB.
     */
    @Test
    public void testAddAccountdet() {
        System.out.println("addAccountdet");
        Customers obj = new Customers();
        obj.setAccount(123);
        obj.setDate("31318");
        CustomersDB instance = new CustomersDB();
        int expResult = 1;
        int result = instance.addAccountdet(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getDetails method, of class CustomersDB.
     */
    @Test
    public void testGetDetails() throws SQLException {
        System.out.println("getDetails");
        Customers obj = new Customers();
        obj.setID(1);
        int mobr=0;
        CustomersDB instance = new CustomersDB();
        int expResult = 771234567;
        ResultSet result = instance.getDetails(obj);
        while(result.next())
        {
            mobr=result.getInt(4);
        }
        assertEquals(expResult, mobr);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAccountdetails method, of class CustomersDB.
     */
    @Test
    public void testGetAccountdetails() throws SQLException {
        System.out.println("getAccountdetails");
        Customers obj = new Customers();
        obj.setID(1);
        obj.setPin(1234);
        CustomersDB instance = new CustomersDB();
        int expResult = 260320181;
        int acc=0;
        ResultSet result = instance.getAccountdetails(obj);
        while(result.next())
        {
            acc=result.getInt(2);
        }
        assertEquals(expResult,acc);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of widthDrawmoney method, of class CustomersDB.
     */
    @Test
    public void testWidthDrawmoney() {
        System.out.println("widthDrawmoney");
        Customers obj = new Customers();
        obj.setAccountBal(500);
        obj.setID(1);
        CustomersDB instance = new CustomersDB();
        int expResult = 1;
        int result = instance.widthDrawmoney(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAlldetails method, of class CustomersDB.
     */
    @Test
    public void testGetAlldetails() throws SQLException {
        System.out.println("getAlldetails");
        CustomersDB instance = new CustomersDB();
        int expResult = 24,id=0;
        ResultSet result = instance.getAlldetails();
        while(result.next())
        {
            id=result.getInt(1);
        }
        assertEquals(expResult, id);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of customerTable method, of class CustomersDB.
     */
    @Test
    public void testCustomerTable() {
        System.out.println("customerTable");
        CustomersDB instance = new CustomersDB();
        DefaultTableModel expResult = null;
        DefaultTableModel result = instance.customerTable();
        expResult=result;
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of accountsTable method, of class CustomersDB.
     */
    @Test
    public void testAccountsTable() {
        System.out.println("accountsTable");
        CustomersDB instance = new CustomersDB();
        DefaultTableModel expResult = null;
        DefaultTableModel result = instance.accountsTable();
        expResult=result;
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
