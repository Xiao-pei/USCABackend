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
import xiaopei.bigdata.Service.UserSkillAnalysisServiceInterface;
import xiaopei.bigdata.Service.UserSkillServiceInterface;
import xiaopei.bigdata.model.DTO.*;
import xiaopei.bigdata.model.Skill;
import xiaopei.bigdata.model.SkillRepository;
import xiaopei.bigdata.model.User;
import xiaopei.bigdata.model.UserRepository;
import xiaopei.bigdata.validator.UserValidator;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserValidator userValidator;
    private final UserDTORegisterServiceInterface userDTORegisterService;
    private final SecurityServiceInterface securityService;
    private final UserSkillServiceInterface userSkillService;
    private final UserSkillAnalysisServiceInterface userSkillAnalysisService;


    public UserController(SecurityServiceInterface securityService, UserRepository userRepository, SkillRepository skillRepository, UserValidator userValidator, UserDTORegisterServiceInterface userDTORegisterService, UserSkillServiceInterface userSkillService, UserSkillAnalysisServiceInterface userSkillAnalysisService) {
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userValidator = userValidator;
        this.userDTORegisterService = userDTORegisterService;
        this.userSkillService = userSkillService;
        this.userSkillAnalysisService = userSkillAnalysisService;
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
    public ObjectNode UserQingDingJob(@RequestParam JobWithScore jobWithScore) {
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
        List<SkillNameLevel> skills = userSkillService.getUserSkillNameLevel(username);
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
            userSkillService.SaveUserSkill(currentUser, skill, skillLevel.getLevel());
        }
        node.put("errmsg", 0);
        return node;
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


}
