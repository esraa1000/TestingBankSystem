import main.Account;
import main.AccountState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    void deposit_negativeAmount() {
        Account acc = new Account(100, AccountState.VERIFIED);
        assertFalse(acc.deposit(-50));
    }

    @Test
    void deposit_validAmount() {
        Account acc = new Account(100, AccountState.VERIFIED);
        assertTrue(acc.deposit(50));
        assertEquals(150, acc.getBalance());
    }

    @Test
    void deposit_closedAccount() {
        Account acc = new Account(100, AccountState.CLOSED);
        assertFalse(acc.deposit(50));
    }

    @Test
    void withdraw_moreThanBalance() {
        Account acc = new Account(100, AccountState.VERIFIED);
        assertFalse(acc.withdraw(200));
    }

    @Test
    void withdraw_suspendedAccount() {
        Account acc = new Account(100, AccountState.SUSPENDED);
        assertFalse(acc.withdraw(50));
    }

    @Test
    void withdraw_valid() {
        Account acc = new Account(100, AccountState.VERIFIED);
        assertTrue(acc.withdraw(40));
        assertEquals(60, acc.getBalance());
    }
}
