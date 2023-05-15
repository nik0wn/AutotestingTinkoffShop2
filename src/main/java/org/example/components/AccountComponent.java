package org.example.components;
import org.example.entity.Account;
import org.example.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class AccountComponent {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserComponent userComponent;

    public Account putReplenishmentBalance(String phone, double balance) {
        var user = userComponent.getUserByPhone(phone);
        if (user == null) {
            throw new NoSuchElementException(
                    String.format("Пользователя с телефоном '%s' не существует!", phone));
        }
        var account = accountRepository.findByUserId(user.getId());
        if(account == null){
            account = new Account(user.getId());
            account.setBalance(account.getBalance() + balance);
            accountRepository.save(account);
            return account;
        }
        account.setBalance(account.getBalance() + balance);
        accountRepository.save(account);
        return account;
    }
    public Account getOrCreateAccount(String phone){
        var user = userComponent.getUserByPhone(phone);
        var account = accountRepository.findByUserId(user.getId());
        if(account == null){
            account = new Account(user.getId());
            accountRepository.save(account);
            return account;
        }
        return account;
    }
}
