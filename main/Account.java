package main;

public class Account {

    private double balance;
    private AccountState state;

    public Account(double initialBalance, AccountState initialState) {
        this.balance = initialBalance;
        this.state = initialState;
    }

    //this one failed a state transition  test
//    public boolean deposit(double amount) {
//        if (state == AccountState.CLOSED) return false;
//        if (amount <= 0) return false;
//
//        balance += amount;
//        return true;
//    }

    public boolean deposit(double amount) {
        if (state == AccountState.CLOSED) return false;
        if (state == AccountState.SUSPENDED) return false;
        if (amount <= 0) return false;

        balance += amount;
        return true;
    }


    public boolean withdraw(double amount) {
        if (state == AccountState.CLOSED) return false;
        if (state == AccountState.SUSPENDED) return false;
        if (amount <= 0) return false;
        if (amount > balance) return false;

        balance -= amount;
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public AccountState getState() {
        return state;
    }

    public boolean verify() {
        if (state == AccountState.UNVERIFIED) {
            state = AccountState.VERIFIED;
            return true;
        }
        return false;
    }

    public boolean violate() {
        if (state == AccountState.VERIFIED) {
            state = AccountState.SUSPENDED;
            return true;
        }
        return false;
    }

    public boolean appeal() {
        if (state == AccountState.SUSPENDED) {
            state = AccountState.VERIFIED;
            return true;
        }
        return false;
    }

    public boolean close() {
        if (state != AccountState.CLOSED) {
            state = AccountState.CLOSED;
            return true;
        }
        return false;
    }
}
