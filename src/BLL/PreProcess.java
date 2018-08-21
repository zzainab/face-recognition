/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BLL;

import com.sun.glass.events.ViewEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author User
 */
public class PreProcess {
   
    
    /** Pixels value - ARGB */
 
    
    /** Total number of pixel in an image*/
  

    int[][][] rgb_buffer;
    
    //convert colored image to gray scale image
     public BufferedImage converttoGray(BufferedImage img)
    {
        int height=img.getHeight();
        int width=img.getWidth();
        try
        {
            for(int i = 0; i < height; i++)
            {
                for(int j = 0; j < width; j++)
                {
                    Color c=new Color(img.getRGB(j, i));
                    int r=(int)(c.getRed()*0.39);
                    int g=(int)(c.getGreen()*0.50);
                    int b=(int)(c.getBlue()*0.11);
                    
                    Color nc=new Color(r+g+b,r+g+b,r+g+b);
                    
                    img.setRGB(j, i, nc.getRGB());
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return img;
    }
    
     
     
     //another method to convert to grayscale this is the preferred method
     public BufferedImage converttoGray3(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int p = img.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
                
                int avg = (r+g+b)/3;

                //replace RGB value with avg
                p = (a<<24)|(avg<<16) | (avg<<8) | avg;
                img.setRGB(x, y, p);
            }
        }
        return img;
    }   
     
     BufferedImage getGrayscaleImage(BufferedImage src) {
    BufferedImage gImg = new BufferedImage(src.getWidth(), src.getHeight(),
                         BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster wr = src.getRaster();
    WritableRaster gr = gImg.getRaster();
    for(int i=0;i<wr.getWidth();i++){
        for(int j=0;j<wr.getHeight();j++){
            gr.setSample(i, j, 0, wr.getSample(i, j, 0));
        }
    }
    gImg.setData(gr);
    return gImg;
}
     
     
     
  public BufferedImage equalize(BufferedImage src){
    BufferedImage nImg = new BufferedImage(src.getWidth(), src.getHeight(),
                         BufferedImage.TYPE_BYTE_GRAY);
    WritableRaster wr = src.getRaster();
    WritableRaster er = nImg.getRaster();
    int totpix= wr.getWidth()*wr.getHeight();
    int[] histogram = new int[256];

    for (int x = 0; x < wr.getWidth(); x++) {
        for (int y = 0; y < wr.getHeight(); y++) {
            histogram[wr.getSample(x, y, 0)]++;
        }
    }

    int[] chistogram = new int[256];
    chistogram[0] = histogram[0];
    for(int i=1;i<256;i++){
        chistogram[i] = chistogram[i-1] + histogram[i];
    }

    float[] arr = new float[256];
    for(int i=0;i<256;i++){
        arr[i] =  (float)((chistogram[i]*255.0)/(float)totpix);
    }

    for (int x = 0; x < wr.getWidth(); x++) {
        for (int y = 0; y < wr.getHeight(); y++) {
            int nVal = (int) arr[wr.getSample(x, y, 0)];
            er.setSample(x, y, 0, nVal);
        }
    }
    nImg.setData(er);
    return nImg;
}
     //method to perform histogram equalization on grayScale image
     public BufferedImage histogramEqualization(BufferedImage img)
     {
         BufferedImage histImg = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
         WritableRaster wr = img.getRaster();
         WritableRaster er = histImg.getRaster();
         int pixel= wr.getWidth()*wr.getHeight();
         int[] histogram = new int[256];
         for (int x = 0; x < wr.getWidth(); x++)
         {
             for (int y = 0; y < wr.getHeight(); y++) 
             {
                 histogram[wr.getSample(x, y, 0)]++;
             }
         }
         
         int[] chistogram = new int[256];
         chistogram[0] = histogram[0];
         for(int i=1;i<256;i++)
         {
             chistogram[i] = chistogram[i-1] + histogram[i];
         }
         
         float[] arr = new float[256];
         for(int i=0;i<256;i++)
         {
             arr[i] =  (float)((chistogram[i]*255.0)/(float)pixel);
         
         }
         
         for (int x = 0; x < wr.getWidth(); x++) 
         {
             for (int y = 0; y < wr.getHeight(); y++) 
             {
                 int nVal = (int) arr[wr.getSample(x, y, 0)];
                 er.setSample(x, y, 0, nVal);
             }
         }
         histImg.setData(er);
       
         return histImg;
        
         
     }
     
     
    
     
        public BufferedImage SmoothImage(BufferedImage img1) {
          
            rgb_buffer = new int[3][img1.getHeight()][img1.getWidth()];// get the height and width
            
            for (int row = 0; row < img1.getHeight(); row++) {
            for (int col = 0; col < img1.getWidth(); col++) {
                Color c = new Color(img1.getRGB(col, row));
                rgb_buffer[0][row][col] = c.getRed();
                rgb_buffer[1][row][col] = c.getGreen();
                rgb_buffer[2][row][col] = c.getBlue();
            }
        }// read image pixels
          
                for (int row = 1; row < img1.getHeight()-1; row++) {
                    for (int col = 1; col < img1.getWidth()-1; col++) {
                        int r = 
                                rgb_buffer[0][row-1][col-1]+
                                rgb_buffer[0][row-1][col]+
                                rgb_buffer[0][row-1][col+1]+
                                
                                rgb_buffer[0][row][col-1]+
                                rgb_buffer[0][row][col]+
                                rgb_buffer[0][row][col+1]+
                                
                                rgb_buffer[0][row+1][col-1]+
                                rgb_buffer[0][row+1][col]+
                                rgb_buffer[0][row+1][col+1];
                          int g = 
                                rgb_buffer[1][row-1][col-1]+
                                rgb_buffer[1][row-1][col]+
                                rgb_buffer[1][row-1][col+1]+
                                
                                rgb_buffer[1][row][col-1]+
                                rgb_buffer[1][row][col]+
                                rgb_buffer[1][row][col+1]+
                                
                                rgb_buffer[1][row+1][col-1]+
                                rgb_buffer[1][row+1][col]+
                                rgb_buffer[1][row+1][col+1];
                            int b = 
                                rgb_buffer[2][row-1][col-1]+
                                rgb_buffer[2][row-1][col]+
                                rgb_buffer[2][row-1][col+1]+
                                
                                rgb_buffer[2][row][col-1]+
                                rgb_buffer[2][row][col]+
                                rgb_buffer[2][row][col+1]+
                                
                                rgb_buffer[2][row+1][col-1]+
                                rgb_buffer[2][row+1][col]+
                                rgb_buffer[2][row+1][col+1];
                                
                            Color c = new Color(r/9, g/9, b/9);
                            img1.setRGB(col, row, c.getRGB());

                    }
                }
              
            
       return img1;
    }
  
     
     
   
}
