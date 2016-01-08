package co.lsnbox.auth.configs.security;

import co.lsnbox.auth.models.Role;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentLogin();
        return (userName != null ? userName : Role.ANONYMOUS.name());
    }
}
