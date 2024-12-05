package org.example;

class Account{
    private int balance = 1000;

    public synchronized void withdraw(int amount, String threadName){
        if(balance>=amount){
            System.out.println(threadName+" is withdrawing "+amount );
            balance-=amount;
            System.out.println(threadName+" completed withdrawal. Remaining balance: "+balance);
        }
        else{
            System.out.println("Insufficient funds for "+threadName);
        }
    }
}

class WithdrawalTask implements Runnable{
    private Account account;
    private int amount;

    public WithdrawalTask(Account account, int amount){
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run(){
        account.withdraw(amount,Thread.currentThread().getName());
    }
}

public class Main{
    public static void main(String[] args){
        Account sharedAccount = new Account();

        Thread user1 = new Thread(new WithdrawalTask(sharedAccount, 600),"User1");
        Thread user2 = new Thread(new WithdrawalTask(sharedAccount, 400),"User2");
        Thread user3 = new Thread(new WithdrawalTask(sharedAccount, 200),"User3");

        user1.start();
        user2.start();
        user3.start();
    }
}
