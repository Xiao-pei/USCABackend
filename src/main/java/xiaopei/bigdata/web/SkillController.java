package xiaopei.bigdata.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xiaopei.bigdata.model.Skill;
import xiaopei.bigdata.model.SkillRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SkillController {
    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);
    @Autowired
    private SkillRepository repository;

    @GetMapping(path = "/api/taglist")
    public Map<String, Object> SearchSkill(@RequestParam("text") String text) {
        logger.info("request" + text);
        Map<String, Object> objectMap = new HashMap<>();
        if (text.equals("%")) {
            objectMap.put("error", 1);
            objectMap.put("errmsg", "Illegal input.");
            return objectMap;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < text.length(); ++i) {
            stringBuffer.append('%');
            stringBuffer.append(text.charAt(i));
            stringBuffer.append('%');
        }
        List<Skill> skills = repository.findSkillByNameLike(stringBuffer.toString());
        objectMap.put("error", 0);
        objectMap.put("data", skills);
        return objectMap;
    }
}
