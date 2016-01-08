package co.lsnbox.auth.repositories;

import co.lsnbox.auth.models.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AccountRepository extends PagingAndSortingRepository<Account, String> {

    /**
     * Find the Account by email
     *
     * @param email
     * @return
     */
    Optional<Account> findByEmail(String email);
}
