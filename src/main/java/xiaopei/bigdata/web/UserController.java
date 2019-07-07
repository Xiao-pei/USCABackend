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
import xiaopei.bigdata.Service.UserSkillServiceInterface;
import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.DTO.SkillLevel;
import xiaopei.bigdata.model.DTO.SkillNameLevel;
import xiaopei.bigdata.model.DTO.UserDTO;
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
    private final UserSkillServiceInterface userSkillService;


    public UserController(SecurityServiceInterface securityService, UserRepository userRepository, SkillRepository skillRepository, UserValidator userValidator, UserDTORegisterServiceInterface userDTORegisterService, UserSkillServiceInterface userSkillService) {
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userValidator = userValidator;
        this.userDTORegisterService = userDTORegisterService;
        this.userSkillService = userSkillService;
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
            node.put("errmsg", bindingResult.getFieldError().getCode());
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
            node.put("errmsg", "Congratulation! username is Ok to use.");
        } else {
            node.put("error", 1);
            node.put("errmsg", "不！用户名已被占用 !");
        }
        return node;
    }

//    @PostMapping(path = "/profile/update")
//    public ResponseEntity<Map<String, Object>> updateUserInfo(@ModelAttribute UserUpdateForm updateForm) {
//        Map<String, Object> map = new HashMap<>();
//
//        return ResponseEntity.ok(map);
//    }

    @PostMapping(path = "/user/job")
    public ObjectNode UserQingDingJob(@RequestBody JobWithScore jobWithScore) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findUserByUsername(username);
        user.setBestMatchJobName(jobWithScore.getName());
        userRepository.save(user);
        node.put("error", 0);
        node.put("errmsg", "nothing");
        return node;
    }

    @GetMapping(path = "/user/job")
    public Map<String, Object> GetUserJobResult() {
        Map<String, Object> map = new HashMap<>();
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findUserByUsername(username);
        String jobName = user.getBestMatchJobName();
        JobWithScore jobWithScore = null;
        for (UserJob userJob : user.getUserJobs()) {
            if (userJob.getJob().getName().equals(jobName)) {
                jobWithScore = new JobWithScore(userJob);
            }
        }
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", jobWithScore);
        return map;
    }

    @GetMapping(path = "/user/joblist")
    public Map<String, Object> GetUserJobResults() {
        Map<String, Object> map = new HashMap<>();
        List<JobWithScore> jobWithScoreList = new ArrayList<>();
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findUserByUsername(username);
        for (UserJob userJob : user.getUserJobs()) {
            jobWithScoreList.add(new JobWithScore(userJob));
        }
        Collections.sort(jobWithScoreList);
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", jobWithScoreList);
        return map;
    }

    @GetMapping(path = "/user/tags")
    public Map<String, Object> GetUserSkills() {
        String username = securityService.findLoggedInUsername();
        List<SkillNameLevel> skills = userSkillService.getUserSkillNameLevel(userRepository.findUserByUsername(username));
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", skills);
        return map;
    }

    @PostMapping(path = "/user/tags")
    public ObjectNode SaveUserSkills(@RequestBody List<SkillLevel> data) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        String username = securityService.findLoggedInUsername();
        User currentUser = userRepository.findUserByUsername(username);
        userSkillService.DeleteUserSkill(currentUser);
        for (SkillLevel skillLevel : data) {
            Skill skill = skillRepository.findSkillById(skillLevel.getId());
            if (skill == null) {
                continue;
            }
            userSkillService.SaveUserSkill(currentUser, skill, skillLevel.getLevel());
        }
        node.put("error", 0);
        node.put("errmsg", "ok");
        return node;
    }



    private User saveRegisterUser(UserDTO u) {
        try {
            return userDTORegisterService.registerNewUserAccount(u);
        } catch (Exception e) {
            return null;
        }
    }


}
