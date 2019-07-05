/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package xiaopei.bigdata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xiaopei.bigdata.model.DTO.SkillNameLevel;
import xiaopei.bigdata.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserSkillService implements UserSkillServiceInterface {
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void SaveUserSkill(User user, Skill skill, int level) {
        UserSkill userSkill = new UserSkill(user, skill, level);
        if (user.getUserSkills().contains(userSkill))
            return;
        user.getUserSkills().add(userSkill);
        skillRepository.save(skill);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public boolean DeleteUserSkill(User user, Skill skill) {
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

    @Transactional(readOnly = true)
    @Override
    public List<SkillNameLevel> getUserSkillNameLevel(String username) {
        User user = userRepository.findUserByUsername(username);
        Set<UserSkill> userSkillList = user.getUserSkills();
        List<SkillNameLevel> skills = new ArrayList<SkillNameLevel>();
        for (UserSkill userSkill : userSkillList) {
            skills.add(new SkillNameLevel(userSkill.getSkill(), userSkill.getLevel()));
        }
        return skills;
    }
}
