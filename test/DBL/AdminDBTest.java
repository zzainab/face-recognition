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
public class AdminDBTest {
    
    public AdminDBTest() {
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
     * Test of login method, of class AdminDB.
     */
    @Test
    public void testLogin() throws SQLException {
        System.out.println("login");
        Admin obj = new Admin();
        obj.setUsername("E007");
        obj.setPassword("123abc");
        AdminDB instance = new AdminDB();
        int expResult = 1;
        int a=0;
        ResultSet result = instance.login(obj);
        while(result.next())
        {
            a=1;
        }
        assertEquals(expResult,a);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addUser method, of class AdminDB.
     */
    @Test
    public void testAddUser() {
        System.out.println("addUser");
        Admin obj = new Admin();
        obj.setUsername("E008");
        obj.setPassword("123abc");
        AdminDB instance = new AdminDB();
        int expResult = 1;
        int result = instance.addUser(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of adminTable method, of class AdminDB.
     */
    @Test
    public void testAdminTable() {
        System.out.println("adminTable");
        AdminDB instance = new AdminDB();
        DefaultTableModel expResult = null;
        DefaultTableModel result = instance.adminTable();
        expResult=result;
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAlldetails method, of class AdminDB.
     */
    @Test
    public void testGetAlldetails() throws SQLException {
        System.out.println("getAlldetails");
        AdminDB instance = new AdminDB();
        String expResult = "E007",result="";
        ResultSet rs = instance.getAlldetails();
        while(rs.next())
        {
            result=rs.getString(1);
        }
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
