package xiaopei.bigdata.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import xiaopei.bigdata.MyException;
import xiaopei.bigdata.Service.SecurityServiceInterface;
import xiaopei.bigdata.Service.UserDTORegisterServiceInterface;
import xiaopei.bigdata.model.*;
import xiaopei.bigdata.validator.UserValidator;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserValidator userValidator;
    private final UserDTORegisterServiceInterface userDTORegisterService;
    private final SecurityServiceInterface securityService;

    public UserController(SecurityServiceInterface securityService, UserRepository userRepository, SkillRepository skillRepository, UserValidator userValidator, UserDTORegisterServiceInterface userDTORegisterService) {
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userValidator = userValidator;
        this.userDTORegisterService = userDTORegisterService;
    }

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
        Map<String, Object> map = new HashMap<>();
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

    @GetMapping(path = "/user/info")
    public Map<String, Object> getCurrentUser() {
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
        //Todo
        return ResponseEntity.ok(map);
    }

    @PostMapping(path = "/user/job")
    public ObjectNode UserQingDingJob(@RequestParam Job job) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("errmsg", "nothing");
        //Todo
        return node;
    }

    @GetMapping(path = "/user/job")
    public Map<String, Object> GetUserJobResult() {
        Map<String, Object> map = new HashMap<String, Object>();
        //Todo
        return null;
    }

    @GetMapping(path = "/user/tags")
    public Map<String, Object> GetUserSkills() {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findUserByUsername(username);
        Set<UserSkill> userSkillList = user.getUserSkills();
        List<SkillNameLevel> skills = new ArrayList<SkillNameLevel>();
        for (UserSkill userSkill : userSkillList) {
            skills.add(new SkillNameLevel(userSkill.getSkill(), userSkill.getLevel()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", skills);
        return map;
    }

    @PostMapping(path = "/user/tags")
    public ObjectNode SaveUserSkills(@RequestBody SkillLevelList data) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        String username = securityService.findLoggedInUsername();
        User currentUser = userRepository.findUserByUsername(username);
        for (SkillLevel skillLevel : data.getData()) {
            Skill skill = skillRepository.findSkillById(skillLevel.getId());
            if (skill == null) {
                continue;
            }
            SaveUserSkill(currentUser, skill, skillLevel.getLevel());
        }
        node.put("errmsg", 0);
        return node;
    }

    @GetMapping(path = "/analysis/jobs")
    public Map<String, Object> getJobAnalysis() {
        //Todo
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("errmsg", "nothing");
        return map;
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

    private void SaveUserSkill(User user, Skill skill, int level) {
        UserSkill userSkill = new UserSkill(user, skill, level);
        if (user.getUserSkills().contains(userSkill))
            return;
        user.getUserSkills().add(userSkill);
        skillRepository.save(skill);
        userRepository.save(user);
    }

    private boolean DeleteUserSkill(User user, Skill skill) {
        Set<UserSkill> userskills = user.getUserSkills();
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
