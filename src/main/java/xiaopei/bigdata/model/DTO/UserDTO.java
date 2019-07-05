package xiaopei.bigdata.model.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Data transfer object
 */
@Data
public class UserDTO {
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    @Size(min = 6, max = 20)
    private String password;
    @NotNull
    @Size(min = 6, max = 20)
    private String confirm;
    @NotNull
    private String phone;
    @NotNull
    private Long captcha;

    public UserDTO() {
    }

}
