package src.server;

import src.server.book.Book;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.List;

import java.io.File;

public interface ObjectWorkerInterface extends Remote {
  public int compare(Book arg1,Book arg2) throws RemoteException;
  public List<Book> search(String partOfTitle) throws RemoteException;
  public File getSummary(String fullTitle) throws RemoteException;
}
  