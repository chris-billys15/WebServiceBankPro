/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject3;


/**
 *
 * @author Admin
 */
public class Transaction {
    private int nomornasabah;
    private String waktu;
    private String jenis;
    private int jumlah;
    private int rekeningterkait;
    
    public Transaction(int nomornasabah, String waktu, String jenis, int jumlah, int rekeningterkait){
        this.nomornasabah = nomornasabah;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.rekeningterkait = rekeningterkait;
        this.waktu = waktu;
    }
    
    public void setNomorNasabah(int nomornasabah){
        this.nomornasabah = nomornasabah;
    }
    
    public int getNomorNasabah(){
        return this.nomornasabah;
    }
    
    public void setWaktu(String waktu){
        this.waktu = waktu;
    }
    
    public String getWaktu(){
        return this.waktu;
    }
    
    public void setJenis(String jenis){
        this.jenis = jenis;
    }
    
    public String getJenis(){
        return this.jenis;
    }
    
    public void setJumlah(int jumlah){
        this.jumlah = jumlah;
    }
    
    public int getJumlah(){
        return this.jumlah;
    }
    
    public void setRekeningTerkait(int rekeningterkait){
        this.rekeningterkait = rekeningterkait;
    }
    
    public int getRekeningTerkait(){
        return this.rekeningterkait;
    }
}
