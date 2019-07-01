package xiaopei.bigdata.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import xiaopei.bigdata.Service.UserDTORegisterServiceInterface;
import xiaopei.bigdata.model.User;
import xiaopei.bigdata.model.UserDTO;
import xiaopei.bigdata.model.UserRepository;
import xiaopei.bigdata.validator.UserValidator;

import java.util.HashMap;
import java.util.Map;

@RestController
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserDTORegisterServiceInterface userDTORegisterService;

    @GetMapping(path = "/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping(path = "/register")
    public @ResponseBody
    ObjectNode Register(@ModelAttribute("user") UserDTO user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        User registered;
        ObjectNode node = new ObjectMapper().createObjectNode();
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
    public ResponseEntity<User> getProfile(@PathVariable("username") String username, Model model) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    String addUser(@RequestParam String username,
                   @RequestParam String password) {
        User user = new User(username, password);
        userRepository.save(user);
        return user.toString();
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Map<String, Object> getAllUsers() {
        // This returns a JSON or XML with the users
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", userRepository.findAll());
        return map;
    }

    @GetMapping(path = "/login")
    public String getLoginForm() {
        return "login";
    }


    @GetMapping({"/welcome"})
    public @ResponseBody
    String welcome(Model model) {
        return "welcome";
    }

    private User saveRegisterUser(UserDTO u) {
        try {
            return userDTORegisterService.registerNewUserAccount(u);
        } catch (Exception e) {
            return null;
        }
    }

}
