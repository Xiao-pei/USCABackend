package xiaopei.bigdata.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Index {
    @GetMapping(path = "/")
    public String getHome() {
        return "index";
    }
}
