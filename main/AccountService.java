package main;

public class AccountService {

    public String deposit(Account account, double amount) {
        boolean success = account.deposit(amount);
        return success ? "Deposit successful" : "Deposit failed";
    }

    public String withdraw(Account account, double amount) {
        boolean success = account.withdraw(amount);
        return success ? "Withdrawal successful" : "Withdrawal failed";
    }
}
