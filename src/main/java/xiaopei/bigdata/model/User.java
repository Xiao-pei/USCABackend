package xiaopei.bigdata.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Integer userType; //0 for ordinary user
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    @JsonIgnore
    private String securedPassword;
    private String telephone;
    @ManyToMany
    private List<Skill> skills;

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
