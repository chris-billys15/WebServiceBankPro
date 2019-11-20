/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject3;
import java.util.*;


/**
 *
 * @author Admin
 */


public class HistoryNasabah {
    private String namapengguna;
    private String namabank;
    private int nomornasabah;
    private int saldo;
    private List<Transaction> historylist;
    
    public HistoryNasabah(String namapengguna, String namabank, int nomornasabah, int saldo){
        this.namapengguna = namapengguna;
        this.nomornasabah = nomornasabah;
        this.saldo = saldo;
        this.namabank = namabank;
    }
    
    public void setNamaPengguna(String namapengguna){
        this.namapengguna = namapengguna;
    }
    
    
    public String getNamaPengguna(){
        return this.namapengguna;
    }
    
    public void setNomorNasabah (int nomornasabah){
        this.nomornasabah = nomornasabah;
    }
    
    
    public int getNomorNasabah(){
        return this.nomornasabah;
    }
    
    public void setSaldo(int saldo){
        this.saldo = saldo;
    }
    
    
    public int getSaldo(){
        return this.saldo;
    }
    
    public void setNamaBank(String namabank){
        this.namabank = namabank;
    }
    
    
    public String getNamaBank(){
        return this.namabank;
    }
    
    public void setHistoryList(List<Transaction> historylist){
        this.historylist = historylist;
    }
    
    
    public List<Transaction> getHistoryList(){
        return this.historylist;
    }
}
