package src.server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

import java.io.File;
import java.io.Serializable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import src.server.book.Book;


public class ObjectWorkerServer extends UnicastRemoteObject implements ObjectWorkerInterface,Serializable  {
  private static Map<String,Book> books;  //pp. ne postoje knjige sa istim naslovom!
  
  private static File noMatching; 

  public ObjectWorkerServer() throws RemoteException, IOException {
    noMatching=new File(".\\src\\server\\files\\noMatching.txt");
    PrintWriter out=new PrintWriter(noMatching);
    out.println("The title you've asked for doesn't exist!");
    out.close();
  }
  
  static {
    try{
      if((new File(".\\src\\server\\collection.ser")).length()==0)
        books=new HashMap<String,Book>();
      else {
        FileInputStream fis=new FileInputStream(".\\src\\server\\collection.ser"); //deserijalizacija!
        ObjectInputStream ois=new ObjectInputStream(fis);
        books=(HashMap<String,Book>)ois.readObject();
        fis.close();
        ois.close();
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
//implementacija metoda ObjectWorkerInterface
  @Override
  public int compare(Book arg1,Book arg2) throws RemoteException {
    return arg1.compareTo(arg2);  //funkcionalnost Book::compareTo()
  }
  
  @Override //(TACNO)
  public List<Book> search(String partOfTitle) throws RemoteException {
    List<Book> selected=new ArrayList<Book>();
    Set titles=books.keySet(); //iteriracemo kroz naslove knjiga
    Iterator iteratorTitle=titles.iterator();
    while(iteratorTitle.hasNext()){
      String title=(String)iteratorTitle.next();
      if(title.contains(partOfTitle))
        selected.add(books.get(title));
    }
    return selected;
  }
  
  @Override
  public File getSummary(String fullTitle) throws RemoteException {
    if(books.containsKey(fullTitle))
      return (books.get(fullTitle)).getTxtFile();
    return noMatching;
  }
     
  public static void main(String[] args){
    try{
      //RMI dio
      ObjectWorkerServer server=new ObjectWorkerServer();
      LocateRegistry.createRegistry(1099);
      Naming.rebind("Server",server);
      
      System.out.println(booksToString()); //ispisuje knjige koje vec ima u kolekciji
      System.out.println("ObjectWorkerServer is started.");
      
      //kreiranje knjiga i pripadajucih .txt fajlova
      Book[] serverBooks={new Book(),new Book()};
      books.put(serverBooks[0].getTitle(),serverBooks[0]);
      books.put(serverBooks[1].getTitle(),serverBooks[1]);
      
      //serijalizacija kolekcije
      FileOutputStream fos=new FileOutputStream(".\\src\\server\\collection.ser");
      ObjectOutputStream oos=new ObjectOutputStream(fos);
      oos.writeObject(books);
      fos.close();
      oos.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public static String booksToString(){
    String result="BOOKS:\n";
    Set titles=books.keySet();
    Iterator iteratorTitle=titles.iterator();
    while(iteratorTitle.hasNext())
      result+=books.get((String)iteratorTitle.next())+"\n";
    return result;
  } 
}