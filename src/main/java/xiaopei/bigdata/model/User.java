package xiaopei.bigdata.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
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
    private String bestMatchJobName;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSkill> userSkills;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserJob> userJobs;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AnalysisResult> analysisResults;

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

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id) &&
                Objects.equal(userType, user.userType) &&
                Objects.equal(name, user.name) &&
                Objects.equal(username, user.username) &&
                Objects.equal(securedPassword, user.securedPassword) &&
                Objects.equal(telephone, user.telephone);
    }*/

/*    @Override
    public int hashCode() {
        return Objects.hashCode(username, securedPassword);
    }*/
}
