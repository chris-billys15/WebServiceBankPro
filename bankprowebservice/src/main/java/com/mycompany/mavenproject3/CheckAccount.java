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

public class CheckAccount {
    private boolean virtualAccount;
    private boolean accountExists;

    public CheckAccount(){
        this.virtualAccount = false;
        this.accountExists = false;
    }
    public CheckAccount(boolean isVirtualAcc, boolean isAccountExists){
        this.virtualAccount = isVirtualAcc;
        this.accountExists = isAccountExists;
    }
    
    /**
     * @param isAccountExists the isAccountExists to set
     */
    public void setAccountExists(boolean isAccountExists) {
        this.accountExists = isAccountExists;
    }

    /**
     * @return the accountExists
     */
    public boolean isAccountExists() {
        return this.accountExists;
    }

    /**
     * @param virtualAccount the virtualAccount to set
     */
    public void setVirtualAccount(boolean virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

    /**
     * @return the virtualAccount
     */
    public boolean isVirtualAccount() {
        return this.virtualAccount;
    }
}
