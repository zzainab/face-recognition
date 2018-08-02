/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBL;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author User
 */
public class CustomersDB {
    
    public int addCustomers(Customers obj)
    {
        int row=0;
        try
        {
            String SQL="INSERT INTO customerdetails VALUES(null,'"+obj.getName()+"','"+obj.getAddress()+"',"
                    + "'"+obj.getMobile()+"')";
            DBConnection mycon=new DBConnection();
            row=mycon.addValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }
    
    public int addAccountdet(Customers obj)
    {
        int row=0;
        try
        {
            String SQL="INSERT INTO accountdetails VALUES((SELECT MAX(ID) FROM customerdetails),1000,500,"+obj.getDate()+",1234,"+obj.getAccount()+")";
                    
            DBConnection mycon=new DBConnection();
            row=mycon.addValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }
    
    public ResultSet getDetails(Customers obj)
    {
        ResultSet rs=null;
        try
        {
            String SQL="select*from customerdetails where ID="+obj.getID()+"";
            DBConnection mycon=new DBConnection();
            rs=mycon.getValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return rs;
    }
    
    public ResultSet getAccountdetails(Customers obj)
    {
        ResultSet rs=null;
        try
        {
            String SQL="select*from accountDetails where ID="+obj.getID()+" and PIN="+obj.getPin()+"";
            DBConnection mycon=new DBConnection();
            rs=mycon.getValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return rs;
    }
    
    public int widthDrawmoney(Customers obj)
    {
        int row=0;
        try
        {
            String SQL="update accountdetails set accBalance=accBalance-"+obj.getAccountBal()+""
                    + ",lastWithdraw="+obj.getAccountBal()+" where id="+obj.getID()+"";
            DBConnection mycon=new DBConnection();
            row=mycon.addValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }
    
    public int depositMoney(Customers obj)
    {
        int row=0;
        try
        {
            String SQL="update accountdetails set accBalance=accBalance+"+obj.getAccountBal()+" where id="+obj.getID()+"";
            DBConnection mycon=new DBConnection();
            row=mycon.addValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }
    
    public ResultSet getAlldetails()
    {
        ResultSet rs=null;
        try
        {
            String SQL="select*from customerdetails";
            DBConnection mycon=new DBConnection();
            rs=mycon.getValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return rs;
    }
    
    public DefaultTableModel customerTable()
    {
        DefaultTableModel objtable=new DefaultTableModel();
        try
        {
            String SQL="select*from customerdetails";
            DBConnection mycon=new DBConnection();
            objtable=mycon.getTables(SQL);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Error connecting database  "+e);
        }
        return objtable;
    }
    
    public DefaultTableModel accountsTable()
    {
        DefaultTableModel objtable=new DefaultTableModel();
        try
        {
            String SQL="select*from accountDetails";
            DBConnection mycon=new DBConnection();
            objtable=mycon.getTables(SQL);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Error connecting database  "+e);
        }
        return objtable;
    }
    
    public int updateCustomers(Customers obj)
    {
        int row=0;
        try
        {
            String SQL="update customerdetails set CName='"+obj.getName()+"',CAdd='"+obj.getAddress()+"',"
                    + "Mobile="+obj.getMobile()+" where ID="+obj.getID()+"";
            DBConnection mycon=new DBConnection();
            row=mycon.addValues(SQL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }
    
    
}
