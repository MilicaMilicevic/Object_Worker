package src.client;

import java.rmi.Naming;

import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;

import src.server.book.Book;
import src.server.ObjectWorkerInterface;

public class ObjectWorkerClient {
  
  public static void main(String[] args){
    try{
      //RMI
      System.out.println("Connecting to server...");
      ObjectWorkerInterface server=(ObjectWorkerInterface)Naming.lookup("Server");
      
      //procesiranje korisnikovog inputa
      BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
      boolean isEnd=false;  
      while(!isEnd){
       System.out.println("Please enter:\n1 - search collection of books\n2 - compare books\n3 - read summary\n4 - end");
       String userInput=in.readLine();
       int choice=Integer.parseInt(userInput);
       switch(choice){
         case 1:
         {
           System.out.println("Enter part of title:");
           List<Book> selected=server.search(in.readLine());
           for(Book tmp:selected)
             System.out.println(tmp);
           break;
       }
         case 2:
         {
           System.out.println("Send me books.");
           Book book1=new Book();
           Book book2=new Book();//NAPOMENA: Klijent moze kreirati knjige, ali ih ne moze dodavati u kolekciju
           if(server.compare(book1,book2)>0)
             System.out.println(book1+" has more pages.");
           else if(server.compare(book1,book2)<0)
             System.out.println(book2+" has more pages.");
           else
             System.out.println("Books have same number of pages.");
           break;
       }
         case 3: 
         {
           System.out.println("Enter title:");
           File summary=server.getSummary(in.readLine());
           BufferedReader inFile=new BufferedReader(new FileReader(summary));
           System.out.println(inFile.readLine()); //citanje sadrzaja iz fajla!
           break;
         }
         case 4:
         {
           System.out.println("Disconnecting from ObjectWorkerServer...");
           isEnd=true;
         }
       }
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
}