package co.lsnbox.auth.services;

import co.lsnbox.auth.models.Account;
import co.lsnbox.auth.repositories.AccountRepository;
import co.lsnbox.auth.services.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class AccountServiceImpl implements IAccountService {

    @Inject
    AccountRepository accountRepository;

    @Override
    public Account findOne(String id) {
        return accountRepository.findOne(id);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).get();
    }

    
}
