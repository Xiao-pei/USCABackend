package xiaopei.bigdata.User;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Integer userType; //0 for ordinary user
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String securedPassword;
    private String telephone;
    private String skill;

    protected User() {
        userType = 0;
    }

    public User(String username, String securedPassword) {
        this.username = username;
        this.securedPassword = securedPassword;
        this.userType = 0;
    }

    public User(Integer userType, String name, String username, String securedPassword) {
        this.userType = userType;
        this.name = name;
        this.username = username;
        this.securedPassword = securedPassword;
    }

}
