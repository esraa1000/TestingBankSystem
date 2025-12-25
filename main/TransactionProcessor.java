package main;

public class TransactionProcessor {

    public boolean processDeposit(Account account, double amount) {
        return account.deposit(amount);
    }

    public boolean processWithdrawal(Account account, double amount) {
        return account.withdraw(amount);
    }
}
