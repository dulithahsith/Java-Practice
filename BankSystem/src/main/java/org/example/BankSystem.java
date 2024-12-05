package org.example;

import java.util.ArrayList;
import java.util.List;

class BankAcc{
    private int accountNumber;
    private String accountHolder;

    public double getBalance() {
        return balance;
    }

    private double balance;

    public BankAcc(int accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " deposited " + amount +
                ". New Balance: " + balance);
    }

    public synchronized void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " withdrew " + amount +
                    ". New Balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " attempted to withdraw " +
                    amount + " but insufficient funds. Balance: " + balance);
        }
    }

    public void displayBalance() {
        System.out.println("Account " + accountNumber + " - Holder: " + accountHolder +
                ", Balance: " + balance);
    }
}

class SavingsAccount extends BankAcc {
    private double interestRate;

    public SavingsAccount(int accountNumber, String accountHolder, double initialBalance, double interestRate) {
        super(accountNumber, accountHolder, initialBalance);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = super.getBalance() * (interestRate / 100);
        super.deposit(interest);
        System.out.println("Interest of " + interest + " added. Balance: " + super.getBalance());
    }
}

class Bank {
    private List<BankAcc> accounts = new ArrayList<>();

    public void addAccount(BankAcc account) {
        accounts.add(account);
    }

    public void displayAllAccounts() {
        for (BankAcc account : accounts) {
            account.displayBalance();
        }
    }
}

class BankOperations implements Runnable {
    private BankAcc account;

    public BankOperations(BankAcc account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            account.deposit(100);
            account.withdraw(50);
        }
    }
}

public class BankSystem {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        BankAcc acc1 = new BankAcc(101, "Alice", 1000);
        SavingsAccount acc2 = new SavingsAccount(102, "Bob", 2000, 5);

        bank.addAccount(acc1);
        bank.addAccount(acc2);

        System.out.println("Initial Balances:");
        bank.displayAllAccounts();

        Thread t1 = new Thread(new BankOperations(acc1), "Thread-1");
        Thread t2 = new Thread(new BankOperations(acc2), "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        acc2.addInterest();

        System.out.println("\nFinal Balances:");
        bank.displayAllAccounts();
    }
}
