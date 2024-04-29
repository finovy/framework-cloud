package tech.finovy.framework.security.auth.core.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Ryan Luo
 * @Date: 2024/1/31 11:17
 */
@Data
@NoArgsConstructor
public class UserDetails {

    private String password = "DEFAULT";

    private String email;

    private String sub;

    private String phone;

    private String givenName;

    private String familyName;

    private String preferredUsername;

    public UserDetails(String email, String givenName, String familyName, String preferredUsername) {
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.preferredUsername = preferredUsername;
    }

    public UserDetails(String password, String email, String givenName, String familyName, String preferredUsername) {
        if (password != null) {
            this.password = password;
        }
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.preferredUsername = preferredUsername;
    }
}
