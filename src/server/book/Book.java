package src.server.book;

import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Book implements Comparable<Book>, Serializable{
  private int numOfPages;
  private String title;
  private String author;
  private String summary;
  
  private File txtFile;      //pripadajuci fajl koji se kreira po kreiranju knjige!
 
  public Book() throws IOException {
    BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Number of pages:");
    numOfPages=Integer.parseInt(in.readLine());
    System.out.println("Title:");
    title=in.readLine();
    System.out.println("Author:");
    author=in.readLine();
    System.out.println("Summary:");
    summary=in.readLine();
    System.out.println();
    txtFile=new File(".\\src\\server\\files\\"+title+".txt");
    PrintWriter out=new PrintWriter(txtFile);
    out.println(summary); //pp. kratak sadrzaj->jedna linija!
    out.close();
    }
  
  @Override
  public int compareTo(Book arg){   //knjige se porede po broju stranica
    return numOfPages-arg.numOfPages;
  }
  
  @Override
  public String toString(){
    return "["+title+"|"+author+"|"+numOfPages+"|"+summary+"]";
  }
  
  public String getTitle(){
    return title;
  }
  
  public File getTxtFile(){
    return txtFile;
  }  
  
}
  