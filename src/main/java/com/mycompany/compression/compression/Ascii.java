package com.mycompany.compression.compression;

import java.util.*;
import java.io.*;
class Ascii{
	public static void main(String ...st){
	 try{
             
    //   Charset utf8 = Charset.forName("UTF-8");
  //      Charset def = Charset.defaultCharset();

//        String charToPrint = "u0905";

        //byte[] bytes = charToPrint.getBytes("UTF-8");
        //String message = new String(bytes , def.name());

        //PrintStream printStream = new PrintStream(System.out, true, utf8.name());
       // printStream.println(message);
 FileWriter fw;
 int val=0; 
 int k=0;   
 char c; 
    File f1=new File("/ascii.txt");
    if(!(f1.exists()))
      f1.createNewFile();
    BufferedWriter bw=new BufferedWriter(new FileWriter(f1.getAbsoluteFile()));
    StringBuilder sdd=new StringBuilder();
	    FileReader f; 
	 	for( k=0;k<=255;k++){
            val=k;
            c=(char)k;
        for (int i = 0; i < 8; i++) {
            sdd.append((val & 128) == 0 ? 0 : 1);
            val <<= 1;
        }
        
         sdd.append(' ');
         sdd.append(c);  
         sdd.append('\n');
        }    
                bw.write(sdd.toString());
               	bw.close();
                
            }catch(Exception e){
            	System.out.println(e);
            }
	}
}