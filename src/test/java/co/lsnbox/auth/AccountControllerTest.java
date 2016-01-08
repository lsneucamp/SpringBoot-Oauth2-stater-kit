package co.lsnbox.auth;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by luciano on 04/03/15.
 */
public class AccountControllerTest extends AbstractTest {

    @Test
    public void findCurrentTest() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.get("/user")
//                .header("Authorization", "Bearer " + getAccessToken("admin@friska.com", "friska123")))
//                .andDo(print())
//                .andExpect(status().isOk());
    }
}
