package xiaopei.bigdata.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import xiaopei.bigdata.MyException;
import xiaopei.bigdata.Service.SecurityServiceInterface;
import xiaopei.bigdata.Service.UserDTORegisterServiceInterface;
import xiaopei.bigdata.Service.WordCountTestService;
import xiaopei.bigdata.model.*;
import xiaopei.bigdata.validator.UserValidator;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserDTORegisterServiceInterface userDTORegisterService;
    @Autowired
    private SecurityServiceInterface securityService;
    @Autowired
    private WordCountTestService service;

    @GetMapping(path = "/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping(path = "/user/register")
    public ObjectNode Register(@RequestBody UserDTO user, BindingResult bindingResult) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        userValidator.validate(user, bindingResult);
        User registered;
        if (bindingResult.hasErrors()) {
            node.put("error", 1);
            node.put("field", bindingResult.getFieldError().getField());
            node.put("code", bindingResult.getFieldError().getCode());
            return node;
        } else {
            registered = saveRegisterUser(user);
            node.put("error", 0);
            node.put("username", registered.getUsername());
            return node;
        }
    }

    @GetMapping(path = "/profile/{username}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable("username") String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            throw new MyException(HttpServletResponse.SC_NOT_FOUND, "No such user " + username);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error", 0);
        map.put("data", user);
        return ResponseEntity.ok(map);
    }

    @PostMapping(path = "/test")
    public String count(@RequestParam String words) {
        User user = userRepository.findUserByUsername("xiaopei");
        Skill skill = skillRepository.findSkillByNameLike("说唱").get(0);
        DeleteUserSkill(user, skill);
        return words;
    }

    @GetMapping(path = "/api/user")
    public Map<String, Object> getCurrentUser() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error", 0);
        String username = securityService.findLoggedInUsername();
        User currentUser = userRepository.findUserByUsername(username);
        if (currentUser == null)
            throw new MyException(HttpServletResponse.SC_NOT_FOUND, "No such login user " + username);
        map.put("data", currentUser);
        return map;
    }

    @GetMapping(path = "/api/check")
    public ObjectNode IsUsernameConfed(@RequestParam String username) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        if (userRepository.findUserByUsername(username) == null) {
            node.put("error", 0);
            node.put("message", "Congratulation! username is Ok to use.");
        } else {
            node.put("error", 1);
            node.put("message", "不！用户名已被占用 !");
        }
        return node;
    }

    @PostMapping(path = "/profile/update")
    public ResponseEntity<Map<String, Object>> updateUserInfo(@ModelAttribute UserUpdateForm updateForm) {
        Map<String, Object> map = new HashMap<String, Object>();
        //Todo
        return ResponseEntity.ok(map);
    }

    @GetMapping(path = "/login")
    public String getLoginForm() {
        return "login";
    }

    private User saveRegisterUser(UserDTO u) {
        try {
            return userDTORegisterService.registerNewUserAccount(u);
        } catch (Exception e) {
            return null;
        }
    }

    protected void SaveUserSkill(User user, Skill skill) {
        UserSkill userSkill = new UserSkill(user, skill);
        if (user.getUserSkills().contains(userSkill))
            return;
        user.getUserSkills().add(userSkill);
        skillRepository.save(skill);
        userRepository.save(user);
    }

    protected boolean DeleteUserSkill(User user, Skill skill) {
        List<UserSkill> userskills = user.getUserSkills();
        for (UserSkill userSkill : userskills) {
            if (userSkill.getSkill().equals(skill)) {
                userskills.remove(userSkill);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }


}
