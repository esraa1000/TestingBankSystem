import main.Account;
import main.AccountState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TransactionTest {

    @Test
    void testDepositValid(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertTrue(acc.deposit(1000));
    }

    @Test
    void testDepositNegativeAmount(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertFalse(acc.deposit(-1000));
    }

    @Test
    void testDepositClosedState(){
        Account acc= new Account(1000,AccountState.CLOSED);
        assertFalse(acc.deposit(1000));
    }

    @Test
    void testDepositSuspendedState(){
        Account acc= new Account(1000,AccountState.SUSPENDED);
        assertFalse(acc.deposit(1000));
    }

    @Test
    void testWithdrawValid(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertTrue(acc.withdraw(1000));
    }

    @Test
    void testWithdrawNegativeAmount(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertFalse(acc.withdraw(-1000));
    }

    @Test
    void testWithdrawHigherThanBalance(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertFalse(acc.withdraw(2000));
    }

    @Test
    void testWithdrawClosedState(){
        Account acc= new Account(1000,AccountState.CLOSED);
        assertFalse(acc.withdraw(1000));
    }

    @Test
    void testWithdrawSuspendedState(){
        Account acc= new Account(1000,AccountState.SUSPENDED);
        assertFalse(acc.withdraw(1000));


    }


    //================ State Transition Tests =============================

    @Test
    void verifyFromUnverified(){
        Account acc= new Account(1000,AccountState.UNVERIFIED);
        assertTrue(acc.verify());
        assertEquals(AccountState.VERIFIED, acc.getState());
    }

    @Test
    void verifyFromVerified(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertFalse(acc.verify());
        assertEquals(AccountState.VERIFIED, acc.getState());
    }

    @Test
    void violateFromVerified(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertTrue(acc.violate());
        assertEquals(AccountState.SUSPENDED, acc.getState());
    }

    @Test
    void violateFromSuspended(){
        Account acc= new Account(1000,AccountState.SUSPENDED);
        assertFalse(acc.violate());
        assertEquals(AccountState.SUSPENDED, acc.getState());
    }

    @Test
    void appealFromSuspended(){
        Account acc= new Account(1000,AccountState.SUSPENDED);
        assertTrue(acc.appeal());
        assertEquals(AccountState.VERIFIED, acc.getState());
    }

    @Test
    void appealFromVerified(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertFalse(acc.appeal());
        assertEquals(AccountState.VERIFIED, acc.getState());
    }

    @Test
    void closeFromClosed(){
        Account acc= new Account(1000,AccountState.CLOSED);
        assertFalse(acc.close());
        assertEquals(AccountState.CLOSED, acc.getState());
    }

    @Test
    void closedFromVerified(){
        Account acc= new Account(1000,AccountState.VERIFIED);
        assertTrue(acc.close());
        assertEquals(AccountState.CLOSED, acc.getState());
    }

}
