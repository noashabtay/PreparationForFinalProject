package Presentation;

import Presentation.Guest.GuestMenu;
import Presentation.SystemManager.SystemManagerMenu;
import Exception.*;
import javax.swing.*;

import java.sql.Connection;
<<<<<<< HEAD
import java.sql.ResultSet;
import java.sql.Statement;

=======
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static DataBase.DBConnector.getConnection;
>>>>>>> origin/master
import static com.sun.glass.ui.Cursor.setVisible;


import static DataBase.DBConnector.getConnection;

public class mainPresentation {
    public static void main(String[] args){

        try{
            Connection myConn = getConnection();
            Statement myStatement = myConn.createStatement();
            ResultSet rs = myStatement.executeQuery("select * from League");

            while (rs.next()){
                System.out.println(rs.getString("seasonsId"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }


//        SystemManagerMenu systemManagerMenu=new SystemManagerMenu();
//        systemManagerMenu.showMenu();

       // GuestMenu guestMenu = new GuestMenu();
    //    guestMenu.showMenu();



        /*
        try{

            Connection myConn = getConnection();
            Statement myStatement = myConn.createStatement();
            ResultSet rs = myStatement.executeQuery("select * from league");

            while (rs.next()){
                System.out.println(rs.getString("seasonsId"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
          */

        // systemManagerMenu.addSystemManager();
      //  systemManagerMenu.addAssociationDeligate();
       // systemManagerMenu.removeAssociationDeligate();

    }
}
