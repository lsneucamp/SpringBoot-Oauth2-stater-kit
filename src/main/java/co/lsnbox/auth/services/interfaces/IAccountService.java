package co.lsnbox.auth.services.interfaces;

import co.lsnbox.auth.models.Account;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IAccountService {

    @PreAuthorize("permitAll()")
    Account findOne(String id);

    @PreAuthorize("permitAll()")
    Account findByEmail(String email);

}
