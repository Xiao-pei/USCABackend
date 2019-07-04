package xiaopei.bigdata.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserUpdateForm {
    @NotNull
    private String name;
    @NotNull
    private String telephone;
    @NotNull
    @Size(min = 6, max = 20)
    private String password;
    @NotNull
    @Size(min = 6, max = 20)
    private String confirmPassword;
}
