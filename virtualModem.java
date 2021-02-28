
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;

public class virtualModem {
 public static void main(String[] param){
   (new virtualModem()).demo();
 }
 public void demo() {
   //VARIABLES
   int k;
   String message;
   message="";
   Modem modem;
   modem=new Modem();
   int p;
   String v;
   int r;
   String w="";
   Integer xor=0;
   int timeCounter=0;
   int im=0;
   int imError=0;
   int gps;
   String m="";
   int gps1=0;
   int justACounter=0;
   int eX3=0;

   //ARRAYLISTS
   ArrayList<Long> timeD= new ArrayList<Long>();
   ArrayList<Byte> myImage= new ArrayList<Byte>();
   ArrayList<Byte> myErrorImage= new ArrayList<Byte>();
   ArrayList<String> Gps1= new ArrayList<String>();
   ArrayList<String> timeGps=new ArrayList<String>();
   ArrayList<String> amplitude=new ArrayList<String>();
   ArrayList<String> length =new ArrayList<String>();
   ArrayList<Byte> myGps= new ArrayList<Byte>();
   ArrayList<Long> timeDifference=new ArrayList<Long>();
   ArrayList<Integer> aaaaaX= new ArrayList<Integer>();

   modem.setSpeed(50000);
   modem.setTimeout(2000);
   modem.open("ithaki");

   for (;;) {
     try {
       k=modem.read();
       message=message+(char) k;
       if (k==-1) break;
       System.out.print((char)k);
       if(message.indexOf("\r\n\n\n")>-1){
         break;
       }
     } catch (Exception x) {
       break;
     }
   }
   // i. erotima
   //Loop for 5 min
   final long NANOSEC_PER_SEC = 1000l*1000*1000;
   long startTime = System.nanoTime();
   while ((System.nanoTime()-startTime)< (5)*60*NANOSEC_PER_SEC){
      modem.write("E4831\r".getBytes());
      v="";
      //server time in ms at beginning//
      long sTime = System.currentTimeMillis();
      //for loop diabazei tous xaraktires pou epistrefei to modem kai tous ektiponei//
      for(;;){
        try{
          p=modem.read();
          if (p==-1) break;
          v=v+(char) p;
          System.out.print((char)p);
          if(v.indexOf("PSTOP")>-1){
            break;}
          }
        catch (Exception x) {
        break;}
      }
      System.out.print("\n");

      //time difference
      long endTime=System.currentTimeMillis();
      long time=(endTime-sTime);
      System.out.print("Time difference: "+ time);
      System.out.print("\n");

      //add time in Arraylist
      timeD.add(time);
    }
  //Print arraylist
  System.out.println(timeD);
  //txt File
  File file=new File("File.txt");

  //store array list in txt File
  int size= timeD.size();
  BufferedWriter writer = null;
  try {
      writer= new BufferedWriter( new FileWriter("File.txt"));
      for(int i=0; i<size; i++){
       String str = timeD.get(i).toString();
       writer.write(str);
       writer.newLine();
      }
    writer.close();
    }
  catch (IOException e){
        System.out.print("Print exeption");
        }

//IMAGE NO ERROR
modem.setSpeed(1000000);
modem.write("M6794\r".getBytes());
//for Loop gia na diabzsei to modem
for(;;){
  try{
    im=modem.read();
    if (im==-1){
     break;
    }
    myImage.add((byte)im);
  }
  catch (Exception x) {
   break;
  }
}
//file to store image
try{
  FileOutputStream imageNoError = new FileOutputStream("imageNoError.jpg");
  for(int i=0; i<myImage.size(); i++){
  imageNoError.write(myImage.get(i));
 }
 imageNoError.close();
}
catch (IOException e){
    System.out.print("Print exeption");
 }
 //IMAGE WITH ERROR
 modem.write("G1876\r".getBytes());
 //for loop
 for(;;){
   try{
     imError=modem.read();
     if (imError==-1){
      break;
     }
     myErrorImage.add((byte)imError);
   }
   catch (Exception x) {
    break;
   }
 }
 //file for image with error
 try{
   FileOutputStream imageError = new FileOutputStream("imageError.jpg");
   for(int i=0; i<myErrorImage.size(); i++){
   imageError.write(myErrorImage.get(i));
  }
  imageError.close();
 }
 catch (IOException e){
     System.out.print("Print exeption");
  }
  //Telos ii. erotimatos

  //GPS

  modem.write("P5865R=1007099\r".getBytes());
  //for loop for modem read
  for(;;){
    gps=modem.read();
    if (gps==-1){
      System.out.print("Break\n");
     break;
   }
    m=m+(char)gps;
    System.out.print((char)gps);
    if(m.indexOf("START ITHAKI GPS TRACKING\r\n")>-1)
    {
      m="";
    }
    // adds the elements we need in a arraylist
    if(m.indexOf("0000*")>-1)
    {
      gps=modem.read();
      System.out.print((char)gps);
      m=m+(char)gps;
      gps=modem.read();
      System.out.print((char)gps);
      m=m+(char)gps;
      Gps1.add(m);
      m="";
    }
  }
  System.out.print(Gps1);
  int counter=0;
  for(int i=0; i<Gps1.size(); i++){
    counter++;
    if(counter==19)
    {
      String s1=Gps1.get(i);
      timeGps.add(s1);
      counter=0;
    }
  }
  System.out.print("\n\n");
  System.out.print(timeGps);

  //apo to arraylist perno to platos
  for(int i=0; i<timeGps.size(); i++){
    String s=((timeGps.get(i)).substring(20,24));
    int z=(int)((Integer.parseInt((timeGps.get(i)).substring(25,29)))*0.006);
    String width=s+Integer.toString(z);
    amplitude.add(width);
  }
  System.out.print("\n\n");
  System.out.print("Width" +amplitude);

//apo to arraylist perno to mikos kai to apothikeuo
  for(int i=0; i<timeGps.size(); i++){
    String s1=((timeGps.get(i)).substring(33,37));
    int z1=(int)((Integer.parseInt((timeGps.get(i)).substring(38,42)))*0.006);
    String length1=s1+Integer.toString(z1);
    length.add(length1);
  }
  System.out.print("\n");
  System.out.print("Length"+length);

  modem.write(("P5865T="+length.get(0)+amplitude.get(0)+"T="+length.get(1)+amplitude.get(1)+"T="+length.get(2)+amplitude.get(2)+"T="+length.get(3)+amplitude.get(3)+"T="+length.get(4)+amplitude.get(4)+"\r").getBytes());
//apothikeuo ta dedomena pou epistrefei to modem
  for(;;){
    try{
      gps1=modem.read();
      if (gps1==-1){
       break;
      }
      myGps.add((byte)gps1);
    }
    catch (Exception x) {
     break;
    }
  }
  //creat a file for GPS image
  try{
    FileOutputStream GpsImage= new FileOutputStream("GpsImage.jpg");
    for(int i=0; i<myGps.size(); i++){
    GpsImage.write(myGps.get(i));
   }
   GpsImage.close();
  }
  catch (IOException e){
      System.out.print("Print exeption");
   }

   //4o erotima!!!!!!
   // loop for 5 min
   modem.setSpeed(1000);
   final long NANOSEC_P_S = 1000l*1000*1000;
   long startT = System.nanoTime();
   int counT=0;
   while ((System.nanoTime()-startT)<(6)*60*NANOSEC_P_S){
   long timeStart = System.currentTimeMillis();
 // blepo poia sunthiki i sxuei kai stelno ton katalilo kodiko
     if(justACounter==0 || xor==eX3){
       modem.write("Q5043\r".getBytes());
       System.out.print("ACK result code\n");
       aaaaaX.add(counT);
       counT=0;
     }
     else if(xor != eX3){
       modem.write("R5069\r".getBytes());
       System.out.print("NACK result code\n");
       counT++;
     }
     System.out.print("Counter :" +counT);
     System.out.print("\n");
     // for loop kai metatrepo ta stoixia poy xriazetai gia na mporo na ta sugkrino
     for(;;){
       try{
         r=modem.read();
         if (r==-1) break;
         w=w+(char)r;
         System.out.print((char)r);
         if(w.indexOf("PSTOP")>-1){
           long timeEnd=System.currentTimeMillis();
           eX3=(int)(Integer.parseInt(w.substring(49,52)));
           xor=0;

           for(int i=31; i<47; i++){
             xor=xor^w.charAt(i);
           }

           w="";
           //time difference
           long timeDif=(timeEnd-timeStart);
           // store time difference in a arraylist
           if(justACounter==0 || xor==eX3){
             timeDifference.add(timeDif);
           }
           System.out.print("\nTime difference: " + timeDif);
           break;
         }
         justACounter++;
       }
       catch (Exception x) {
         break;
       }
     }
     System.out.print("\nFCS:"+eX3);
     System.out.print("\nXOR result:"+xor);
     System.out.print("\n\n");
 }
  System.out.print(timeDifference);
  //print Nack times
  System.out.print("\n Nack times" +aaaaaX);

  File Time=new File("Time.txt");
  //store array list in txt File
  int sizeXor= timeDifference.size();
  BufferedWriter writer2 = null;
  try {
      writer2= new BufferedWriter( new FileWriter("Time.txt"));
      for(int i=0; i<sizeXor; i++){
       String str2 = timeDifference.get(i).toString();
       writer2.write(str2);
       writer2.newLine();
      }
    writer2.close();
    }
  catch (IOException e){
        System.out.print("Print exeption");
        }

//file to store Nack times in order to make a graph
  File Nack=new File("Nack.txt");
  BufferedWriter writer3 = null;
  try {
      writer3= new BufferedWriter( new FileWriter("Nack.txt"));
      for(int i=0; i<aaaaaX.size(); i++){
      String str3 = aaaaaX.get(i).toString();
      writer3.write(str3);
      writer3.newLine();
            }
          writer3.close();
          }
        catch (IOException e){
              System.out.print("Print exeption");
              }

  //TELOS ERGASIAS

    modem.close();
  }
}
