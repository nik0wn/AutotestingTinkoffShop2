import org.example.components.AccountComponent;
import org.example.components.UserComponent;
import org.example.entity.Account;
import org.example.entity.User;
import org.example.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class AccountComponentUnitTest extends AbstractTest {
    @Mock
    UserComponent userComponent;
    @InjectMocks
    AccountComponent accountComponent;
    @Mock
    AccountRepository accountRepository;
    @Test
    void notUserAccountPutReplenishmentBalance(){
        var userPhone = "89120003010";
        var userName = "Oleg";
        var balance = 100;
        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(1L);

        Mockito.when(userComponent.getUserByPhone(userPhone)).thenReturn(user);
        accountComponent.putReplenishmentBalance(user.getPhone(), balance);
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }
    @Test
    void userAccountPutReplenishmentBalance(){
        var userPhone = "89120003010";
        var userName = "Oleg";
        var balance = 100;
        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(1L);
        var account = new Account(user.getId());
        Mockito.when(userComponent.getUserByPhone(userPhone)).thenReturn(user);
        Mockito.when(accountRepository.findByUserId(user.getId())).thenReturn(account);
        accountComponent.putReplenishmentBalance(user.getPhone(), balance);
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }
    @Test
    void balanceNotUserAccountPutReplenishmentBalance(){
        var userPhone = "89120003010";
        var userName = "Oleg";
        var balance = 100;

        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(1L);
        var balanceStart = new Account(user.getId()).getBalance();

        Mockito.when(userComponent.getUserByPhone(userPhone)).thenReturn(user);
        var account = accountComponent.putReplenishmentBalance(user.getPhone(), balance);
        var newBalance = balanceStart + balance;
        assertEquals(newBalance, account.getBalance());
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }
    @Test
    void balanceUserAccountPutReplenishmentBalance(){
        var userPhone = "89120003010";
        var userName = "Oleg";
        var balance = 100;

        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(1L);

        var account = new Account(user.getId());

        Mockito.when(userComponent.getUserByPhone(userPhone)).thenReturn(user);
        Mockito.when(accountRepository.findByUserId(user.getId())).thenReturn(account);

        var newBalance = account.getBalance() + balance;

        accountComponent.putReplenishmentBalance(user.getPhone(), balance);

        assertEquals(newBalance, account.getBalance());

        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }

    @Test
    void errorNotUserPutReplenishmentBalance(){
        var userPhone = "89120003010";
        var balance = 100;

        assertThrows(NoSuchElementException.class, ()-> accountComponent.putReplenishmentBalance(userPhone,balance));
        Mockito.verify(accountRepository, Mockito.times(0)).save(any(Account.class));
    }
    @Test
    void notUserAccountGetOrCreateAccount(){
        var userPhone = "89120003010";
        var userName = "Oleg";
        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(1L);

        Mockito.when(userComponent.getUserByPhone(userPhone)).thenReturn(user);
        accountComponent.getOrCreateAccount(userPhone);
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }
@Test
    void userAccountGetOrCreateAccount(){
        var userPhone = "89120003010";
        var userName = "Oleg";
        var user = new User();
        user.setPhone(userPhone);
        user.setName(userName);
        user.setId(1L);
        var account = new Account(user.getId());
        Mockito.when(userComponent.getUserByPhone(user.getPhone())).thenReturn(user);
        Mockito.when(accountRepository.findByUserId(user.getId())).thenReturn(account);
        var accountResult = accountComponent.getOrCreateAccount(user.getPhone());
        assertEquals(account,accountResult);
        Mockito.verify(accountRepository, Mockito.times(0)).save(any(Account.class));
    }
}
