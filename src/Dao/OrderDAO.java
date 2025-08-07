package Dao;
import java.sql.*;
import java.io.*;
import Constants.*;
import Utils.*;
import Services.*;
import Dao.*;
import Db.*;
import Menus.*;
import Models.*;
import java.util.*;
public class OrderDAO {
    public static void addToCart() {
        AppConstants.s.nextLine();
        System.out.print("\nEnter restaurant name to select dishes :- ");
        String rest_name = AppConstants.s.nextLine();
        System.out.print("Enter dish name to select :- ");
        String dish_name = AppConstants.s.nextLine();
    }
    public static void viewCart() {

    }
    public static void orderHistory() {

    }
}
