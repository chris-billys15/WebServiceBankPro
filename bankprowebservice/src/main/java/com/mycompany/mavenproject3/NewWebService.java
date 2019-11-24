/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject3;

import com.sun.tools.javac.comp.Check;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author Admin
 */
@WebService(serviceName = "NewWebService")
public class NewWebService {

    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "validateRekening")
    public CheckAccount validateRekening(@WebParam(name = "Rekening") Integer Rekening) {
        //TODO write your implementation code here:
        Boolean found = false;
        CheckAccount checkAccount = new CheckAccount();
        String dbUrl = "jdbc:mysql://localhost:3306/bankprodb";
        String dbClass = "com.mysql.jdbc.Driver";
        String queryRekening = "Select * FROM bankprodb.nasabah where nomornasabah = " + Rekening ;
        String queryVirtualAcc = "Select * FROM bankprodb.virtualaccount where virtualaccount = " + Rekening ;
        String userName = "Azhar", password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection (dbUrl, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery(queryRekening);
            while(rs1.next() && !found){
                int nomornasabah = rs1.getInt("nomornasabah");
                if(Rekening == nomornasabah){
                    found = true;
                    checkAccount.setAccountExists(true);
                    checkAccount.setVirtualAccount(false);
                }
            }
            if(!found){
                ResultSet rs2 = stmt.executeQuery(queryVirtualAcc);
                while(rs2.next() && !found){
                    int nomorVirtualAcc = rs2.getInt("virtualaccount");
                    if(Rekening == nomorVirtualAcc){
                        found = true;
                        checkAccount.setAccountExists(false);
                        checkAccount.setVirtualAccount(true);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            return checkAccount;
        }
        
    }

    /**
     * Web service operation
     * @param RekeningPengirim
     * @param RekeningPenerima
     * @param Nominal
     * @return 
     * @throws java.lang.ClassNotFoundException 
     */
    @WebMethod(operationName = "Transfer")
    public Boolean Transfer(@WebParam(name = "RekeningPengirim") Integer RekeningPengirim, @WebParam(name = "RekeningPenerima") Integer RekeningPenerima, @WebParam(name = "Nominal") Integer Nominal) throws ClassNotFoundException {
        Boolean sukses = false;
        String dbUrl = "jdbc:mysql://localhost:3306/bankprodb";
        String dbClass = "com.mysql.jdbc.Driver";
        String userName = "Azhar", password = "";
        Date date = new Date();
        Boolean enoughBalance = false;
        Boolean existReceiver = false;
        int saldoNasabah = 0;
        int saldoPenerima = 0;
        if(RekeningPenerima.equals(RekeningPengirim)){
            return false;
        }
        else{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection (dbUrl, userName, password);
                Statement stmt = con.createStatement();
                //Cek Saldo Pengirim
                ResultSet rs = stmt.executeQuery("SELECT saldo FROM bankprodb.nasabah WHERE nomornasabah = " + RekeningPengirim);
                while(rs.next() && !enoughBalance) {
                    saldoNasabah = rs.getInt("saldo");
                    if (saldoNasabah >= Nominal){
                        enoughBalance = true;
                    }
                }

                //Cek Rekening Penerima kalau saldo mencukupi
                if (enoughBalance && !existReceiver){
                    rs = stmt.executeQuery("Select nomornasabah FROM bankprodb.nasabah WHERE nomornasabah = " + RekeningPenerima);
                    while(rs.next() && !existReceiver) {
                        int nomorNasabah = rs.getInt("nomornasabah");
                        if (nomorNasabah == RekeningPenerima){
                            existReceiver = true;
                        }
                    }

                    //Kalau belum ketemu, cek di akun virtual
                    if (!existReceiver){
                        rs = stmt.executeQuery("Select * FROM bankprodb.virtualaccount WHERE virtualaccount = " + RekeningPenerima);
                        while(rs.next() && !existReceiver) {
                            int nomorAkunVirtual = rs.getInt("virtualaccount");
                            int nomorNasabah = rs.getInt("nomornasabah");
                            if (nomorAkunVirtual == RekeningPenerima){
                                existReceiver = true;
                                String Query = "UPDATE bankprodb.virtualaccount SET NomorPengirim = ? WHERE virtualaccount = ?";
                                PreparedStatement updatenomorpengirim = con.prepareStatement(Query);
                                updatenomorpengirim.setInt(1, RekeningPengirim);
                                updatenomorpengirim.setInt(2, RekeningPenerima);
                                updatenomorpengirim.executeUpdate();
                                RekeningPenerima = nomorAkunVirtual;
                            }
                        }
                    }
                }

                if (existReceiver && enoughBalance){

                    rs = stmt.executeQuery("SELECT * FROM bankprodb.nasabah WHERE nomornasabah = " + RekeningPenerima);
                    while (rs.next()){
                        int nomorRekeningPenerima = rs.getInt("nomornasabah");
                        if (nomorRekeningPenerima == RekeningPenerima){
                            saldoPenerima = rs.getInt("saldo");
                            saldoPenerima = saldoPenerima + Nominal;
                        }
                    }

                    String Query = "UPDATE bankprodb.nasabah SET saldo = ? WHERE nomornasabah = ?";
                    PreparedStatement updatesaldopenerima = con.prepareStatement(Query);
                    updatesaldopenerima.setInt(1, saldoPenerima);
                    updatesaldopenerima.setInt(2, RekeningPenerima);
                    updatesaldopenerima.executeUpdate();

                    rs = stmt.executeQuery("SELECT nomornasabah,saldo FROM bankprodb.nasabah WHERE nomornasabah = " + RekeningPengirim);
                    while (rs.next()){
                        int nomorRekeningPengirim = rs.getInt("nomornasabah");
                        if (nomorRekeningPengirim == RekeningPenerima){
                            saldoNasabah = rs.getInt("saldo");
                            saldoNasabah = saldoNasabah - Nominal;

                        }
                    }
                    PreparedStatement updatesaldopengirim = con.prepareStatement(Query);
                    updatesaldopengirim.setInt(1, saldoNasabah);
                    updatesaldopengirim.setInt(2, RekeningPengirim);
                    updatesaldopengirim.executeUpdate();

                    String Query3 = "INSERT INTO bankprodb.transaksi (NomorNasabah, Waktu, Jenis, Jumlah, RekeningTerkait) VALUES (?,?,?,?,?), (?,?,?,?,?)" ;
                    PreparedStatement inserttransaksi = con.prepareStatement(Query3);
                    inserttransaksi.setInt(1, RekeningPengirim);
                    inserttransaksi.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
                    inserttransaksi.setString(3,"Debit");
                    inserttransaksi.setInt(4, Nominal);
                    inserttransaksi.setInt(5,RekeningPenerima);
                    inserttransaksi.setInt(6, RekeningPenerima);
                    inserttransaksi.setTimestamp(7, new java.sql.Timestamp(date.getTime()));
                    inserttransaksi.setString(8,"Kredit");
                    inserttransaksi.setInt(9, Nominal);
                    inserttransaksi.setInt(10,RekeningPengirim);
                    int i = inserttransaksi.executeUpdate();
                    sukses = true;
                }
            } catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            } finally {
                return sukses;
            }
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addVirtualAccount")
    public Integer addVirtualAccount(@WebParam(name = "Rekening") Integer Rekening) {
        String dbUrl = "jdbc:mysql://localhost:3306/bankprodb";
        String dbClass = "com.mysql.jdbc.Driver";
        String userName = "Azhar", password = "";
        int randomVirtualAccount = 0;
        Random randomNumber = new Random();
        Boolean unique = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection (dbUrl, userName, password);
            Statement stmt = con.createStatement();
            
            randomVirtualAccount = randomNumber.nextInt((9999999 - 1000000) + 1) + 1000000;
            while (!unique){
                //Check VirtualAccount di Database
                ResultSet rs = stmt.executeQuery("SELECT * FROM bankprodb.virtualaccount WHERE virtualaccount = " + randomVirtualAccount);
                rs.last();
                if (rs.getRow() == 0){
                    unique = true;
                } else {
                    randomVirtualAccount = randomNumber.nextInt((9999999 - 1000000) + 1 ) + 1000000;
                }
            }
            
            //randomVirtualAccount pasti sudah unique
            String Query = "INSERT INTO bankprodb.virtualaccount (NomorNasabah,VirtualAccount) VALUES (?,?)";
            PreparedStatement insertvirtualaccount = con.prepareStatement(Query);
            insertvirtualaccount.setInt(1, Rekening);
            insertvirtualaccount.setInt(2, randomVirtualAccount);
            insertvirtualaccount.executeUpdate();
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            return randomVirtualAccount;
        }
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "checkTransaction")
    public Boolean checkTransaction(@WebParam(name = "rekening") Integer rekening, @WebParam(name = "nominal") Integer nominal, @WebParam(name = "startDate") String startDate, @WebParam(name = "finishDate") String finishDate) {
        String dbUrl = "jdbc:mysql://localhost:3306/bankprodb";
        String dbClass = "com.mysql.jdbc.Driver";
        String userName = "Azhar", password = "";
        Boolean found = false;
        long dateSecond = 0;
        long startDateinSecond = (new org.apache.xmlbeans.GDate(startDate)).getDate().getTime();
        long finishDateinSecond = (new org.apache.xmlbeans.GDate(finishDate)).getDate().getTime();
        try {
            
            //Check kalau Rekening adalah virtual account 
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection (dbUrl, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nomornasabah FROM bankprodb.virtualaccount WHERE virtualaccount = " + rekening);
            while (rs.next()){
                if (rs.getInt("virtualaccount") == rekening){
                    rekening = rs.getInt("nomornasabah");
                }
            }
            rs.close();
            //Check Transaksi dengan rekening dan jumlah terkait
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM bankprodb.transaksi WHERE nomornasabah = " + rekening + " AND jumlah = " + nominal); 
            while (rs2.next()){
                dateSecond = rs2.getTimestamp("Waktu").getTime();
                if (startDateinSecond <= dateSecond && finishDateinSecond >=dateSecond && rs2.getString("Jenis").equals("Kredit")){
                    found = true;
                }
            }
        }  catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            return found;
        }
    }
    
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "historyTransaction")
        
    public HistoryNasabah historyTransaction(@WebParam(name = "rekening") Integer rekening) {
        String dbUrl = "jdbc:mysql://localhost:3306/bankprodb";
        String dbClass = "com.mysql.jdbc.Driver";
        String userName = "Azhar", password = "";
        String strfound = "";
        long dateSecond = 0;
        HistoryNasabah history = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection (dbUrl, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bankprodb.nasabah WHERE nomornasabah=" + rekening);
            while(rs.next()){
                history = new HistoryNasabah(rs.getString("namapemilik"),rs.getString("namabank"),rs.getInt("nomornasabah"),rs.getInt("saldo"));
            }
            
            ArrayList<Transaction> arrlist = new ArrayList<Transaction>();
            strfound = "Belum query transaksi";
            
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM bankprodb.transaksi WHERE nomornasabah=" + rekening);
            while(rs2.next()){
                strfound="Masuk while";
                arrlist.add(new Transaction(rs2.getInt("nomornasabah"),rs2.getString("waktu"), rs2.getString("jenis"), rs2.getInt("jumlah"),rs2.getInt("rekeningterkait")));
            }
            strfound = "Berhasil query transaksi";
            history.setHistoryList(arrlist);
            
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            return history;
        }
        
    }
}
