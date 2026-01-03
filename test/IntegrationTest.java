package test;
import main.Account;
import main.AccountState;
import main.AccountService;
import main.TransactionProcessor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    // 1. Tests communication between AccountService and Account (Success Case)
    @Test
    void testServiceToAccountDepositIntegration() {
        // Setup: Create a VERIFIED account with 100 balance
        Account acc = new Account(100, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        // Execute: Service calls Account.deposit()
        String result = service.deposit(acc, 50);
        
        // Verify: Check return message and updated balance
        assertEquals("Deposit successful", result);
        assertEquals(150, acc.getBalance());
    }

    // 2. Tests service behavior with state constraints (Failure Case)
    @Test
    void testServiceWithdrawOnSuspendedAccount() {
        // Setup: Account is SUSPENDED
        Account acc = new Account(500, AccountState.SUSPENDED);
        AccountService service = new AccountService();
        
        // Execute: Attempt to withdraw from suspended account
        String result = service.withdraw(acc, 100);
        
        // Verify: Operation should fail and balance stays same
        assertEquals("Withdrawal failed", result); 
        assertEquals(500, acc.getBalance()); 
    }

    // 3. Tests communication between TransactionProcessor and Account
    @Test
    void testProcessorIntegrationWithVerifiedAccount() {
        // Setup: Verified account with 1000 balance
        Account acc = new Account(1000, AccountState.VERIFIED);
        TransactionProcessor processor = new TransactionProcessor();
        
        // Execute: Processor calls Account methods directly
        boolean success = processor.processWithdrawal(acc, 200);
        
        // Verify: Direct call returns true and balance is deducted
        assertTrue(success);
        assertEquals(800, acc.getBalance());
    }

    // 4. Tests Full Flow (State Change + Service Call)
    @Test
    void testFullStateToServiceFlow() {
        // Setup: Start with UNVERIFIED account
        Account acc = new Account(0, AccountState.UNVERIFIED);
        AccountService service = new AccountService();
        
        // Step 1: Verify the account (State Transition) with assertion
        assertTrue(acc.verify()); // Verify transition succeeded
        assertEquals(AccountState.VERIFIED, acc.getState());
        
        // Step 2: Deposit through the Service
        String result = service.deposit(acc, 1000);
        
        // Verify: Final state is VERIFIED and balance is correct
        assertEquals("Deposit successful", result);
        assertEquals(AccountState.VERIFIED, acc.getState());
        assertEquals(1000, acc.getBalance());
    }

    // 5. Tests behavior with a CLOSED account
    @Test
    void testIntegrationWithClosedAccount() {
        // Setup: Account is CLOSED
        Account acc = new Account(100, AccountState.CLOSED);
        AccountService service = new AccountService();
        
        // Execute: Attempt deposit on closed account
        String result = service.deposit(acc, 50);
        
        // Verify: Service returns failure message
        assertEquals("Deposit failed", result);
        assertEquals(100, acc.getBalance());
    }

    // 6. Tests withdrawal exceeding balance
    @Test
    void testServiceWithdrawExceedingBalance() {
        Account acc = new Account(100, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        String result = service.withdraw(acc, 200); // More than balance
        
        assertEquals("Withdrawal failed", result);
        assertEquals(100, acc.getBalance()); // Should remain unchanged
    }

    // 7. Tests deposit with negative amount
    @Test
    void testServiceDepositNegativeAmount() {
        Account acc = new Account(100, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        String result = service.deposit(acc, -50);
        
        assertEquals("Deposit failed", result);
        assertEquals(100, acc.getBalance());
    }

    // 8. Tests withdrawal with negative amount
    @Test
    void testServiceWithdrawNegativeAmount() {
        Account acc = new Account(100, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        String result = service.withdraw(acc, -20);
        
        assertEquals("Withdrawal failed", result);
        assertEquals(100, acc.getBalance());
    }

    // 9. Tests deposit with zero amount (edge case)
    @Test
    void testServiceDepositZeroAmount() {
        Account acc = new Account(100, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        String result = service.deposit(acc, 0);
        
        assertEquals("Deposit failed", result); // Should fail for zero
        assertEquals(100, acc.getBalance());
    }

    // 10. Tests TransactionProcessor deposit flow
    @Test
    void testProcessorIntegrationForDeposit() {
        Account acc = new Account(500, AccountState.VERIFIED);
        TransactionProcessor processor = new TransactionProcessor();
        
        boolean success = processor.processDeposit(acc, 300);
        
        assertTrue(success);
        assertEquals(800, acc.getBalance());
    }

    // 11. Tests TransactionProcessor with suspended account
    @Test
    void testProcessorWithSuspendedAccount() {
        Account acc = new Account(500, AccountState.SUSPENDED);
        TransactionProcessor processor = new TransactionProcessor();
        
        boolean success = processor.processDeposit(acc, 100);
        
        assertFalse(success); // Should fail for suspended
        assertEquals(500, acc.getBalance());
    }

    // 12. Tests full workflow with error recovery
    @Test
    void testFullWorkflowWithAppeal() {
        // Start with VERIFIED account
        Account acc = new Account(1000, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        // Step 1: Account gets suspended (violation)
        assertTrue(acc.violate());
        assertEquals(AccountState.SUSPENDED, acc.getState());
        
        // Step 2: Try to withdraw (should fail)
        String withdrawResult = service.withdraw(acc, 200);
        assertEquals("Withdrawal failed", withdrawResult);
        
        // Step 3: Appeal and get back to VERIFIED
        assertTrue(acc.appeal());
        assertEquals(AccountState.VERIFIED, acc.getState());
        
        // Step 4: Now withdraw should succeed
        String finalResult = service.withdraw(acc, 200);
        assertEquals("Withdrawal successful", finalResult);
        assertEquals(800, acc.getBalance());
    }

    // 13. Tests account closure workflow
    @Test
    void testAccountClosureWorkflow() {
        Account acc = new Account(500, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        // Step 1: Close the account
        assertTrue(acc.close());
        assertEquals(AccountState.CLOSED, acc.getState());
        
        // Step 2: Try to deposit (should fail)
        String depositResult = service.deposit(acc, 100);
        assertEquals("Deposit failed", depositResult);
        
        // Step 3: Try to withdraw (should fail)
        String withdrawResult = service.withdraw(acc, 100);
        assertEquals("Withdrawal failed", withdrawResult);
        
        // Balance should remain unchanged
        assertEquals(500, acc.getBalance());
    }

    // 14. Tests consistency between TransactionProcessor and AccountService
    @Test
    void testConsistencyBetweenComponents() {
        Account acc = new Account(300, AccountState.VERIFIED);
        AccountService service = new AccountService();
        TransactionProcessor processor = new TransactionProcessor();
        
        // Both should produce same result for same input
        String serviceResult = service.deposit(acc, 100);
        assertEquals("Deposit successful", serviceResult);
        // After first deposit, balance is 400
        assertEquals(400, acc.getBalance());

        boolean processorResult = processor.processDeposit(acc, 100);
        assertTrue(processorResult);
        // After both deposits, balance is 500
        assertEquals(500, acc.getBalance());
    }

    // 15. Tests very large amount (boundary case)
    @Test
    void testServiceWithVeryLargeAmount() {
        Account acc = new Account(1000, AccountState.VERIFIED);
        AccountService service = new AccountService();
        
        // Test with maximum double value
        String result = service.deposit(acc, Double.MAX_VALUE);
        
        // This should either succeed or fail gracefully
        // We're testing the system doesn't crash
        assertNotNull(result); // Result should not be null
        // Balance check might overflow, so we just ensure no crash
    }
}

