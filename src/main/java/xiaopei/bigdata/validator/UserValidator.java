package xiaopei.bigdata.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import xiaopei.bigdata.model.DTO.UserDTO;

import java.util.regex.Pattern;

@Slf4j
@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDTO user = (UserDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() > 32) {
            errors.rejectValue("username", "密码长度必须在32个字符内");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            errors.rejectValue("password", "密码长度必须在6-20个字符内");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty");
        if (!Pattern.matches("^1(3|4|5|7|8)\\d{9}$", user.getPhone())) {
            errors.rejectValue("phone", "电话格式错误");
        }
        if (!user.getConfirm().equals(user.getPassword())) {
            errors.rejectValue("confirm", "两次密码不一致");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "captcha", "NotEmpty");
        if (user.getCaptcha() != 114514) {
            errors.rejectValue("captcha", "验证码错误");
        }
        log.debug("UserForm form OK");
    }
}