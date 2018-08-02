/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBL;

import java.awt.image.BufferedImage;

/**
 *
 * @author User
 */
public class Customers {
    private String name;
    private String address;
    private String date;
    private int mobile; 
    private int account;
    private int ID;
    private int pin;
    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage img3;
    private float accountBal;
    private float lastwidthdraw;
    
    /*
    *add the variable for the image to get and set it atleast two variables for the images
    */

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the mobile
     */
    public int getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the account
     */
    public int getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(int account) {
        this.account = account;
    }

    /**
     * @return the img1
     */
    public BufferedImage getImg1() {
        return img1;
    }

    /**
     * @param img1 the img1 to set
     */
    public void setImg1(BufferedImage img1) {
        this.img1 = img1;
    }

    /**
     * @return the img2
     */
    public BufferedImage getImg2() {
        return img2;
    }

    /**
     * @param img2 the img2 to set
     */
    public void setImg2(BufferedImage img2) {
        this.img2 = img2;
    }

    /**
     * @return the img3
     */
    public BufferedImage getImg3() {
        return img3;
    }

    /**
     * @param img3 the img3 to set
     */
    public void setImg3(BufferedImage img3) {
        this.img3 = img3;
    }

    /**
     * @return the accountBal
     */
    public float getAccountBal() {
        return accountBal;
    }

    /**
     * @param accountBal the accountBal to set
     */
    public void setAccountBal(float accountBal) {
        this.accountBal = accountBal;
    }

    /**
     * @return the lastwidthdraw
     */
    public float getLastwidthdraw() {
        return lastwidthdraw;
    }

    /**
     * @param lastwidthdraw the lastwidthdraw to set
     */
    public void setLastwidthdraw(float lastwidthdraw) {
        this.lastwidthdraw = lastwidthdraw;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the pin
     */
    public int getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(int pin) {
        this.pin = pin;
    }
}
