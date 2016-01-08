package co.lsnbox.auth.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by luciano on 01/03/15.
 */

@Document(collection = Account.COLLECTION)
public class Account extends AbstractDocument {

    public final static String COLLECTION = "accounts";
    public static final String INDEX_NAME = "accounts";
    public static final String TYPE = "account";

    @NotEmpty(message = "account.email.error.empty")
    @Email
    //Mongodb annotations
    @Indexed(unique = true)
    protected String email;

    @NotEmpty(message = "account.password.error.empty")
    @Size(min = 6, message = "account.password.error.length")
    private String password;

    @NotNull(message = "account.status.error.null")
    private AccountStatus status;

    @NotEmpty(message = "account.role.error.empty")
    private List<Role> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getCollection() {
        return COLLECTION;
    }
    @Override
    public String getIndexName() {
        return INDEX_NAME;
    }
    @Override
    public String getType() {
        return TYPE;
    }

}
