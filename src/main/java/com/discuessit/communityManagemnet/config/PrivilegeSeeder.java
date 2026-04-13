package com.discuessit.communityManagemnet.config;

import com.discuessit.communityManagemnet.model.ModeratorPrivilegeType;
import com.discuessit.communityManagemnet.model.Privilege;
import com.discuessit.communityManagemnet.repository.PrivilegeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PrivilegeSeeder implements CommandLineRunner {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public void run(String... args) {
        seedPrivileges();
    }

    private void seedPrivileges() {
        if (privilegeRepository.count() == 0) {
            List<Privilege> privileges = List.of(
                    new Privilege(null, ModeratorPrivilegeType.MANAGE_MEMBERS, "Ban, mute, or warn users"),
                    new Privilege(null, ModeratorPrivilegeType.MANAGE_POSTS, "Delete or edit posts/comments"),
                    new Privilege(null, ModeratorPrivilegeType.MANAGE_REPORTS, "Review and resolve user reports"),
                    new Privilege(null, ModeratorPrivilegeType.PIN_POSTS, "Pin posts to the top of a section"),
                    new Privilege(null, ModeratorPrivilegeType.VIEW_LOGS, "Access moderation activity logs"),
                    new Privilege(null, ModeratorPrivilegeType.MANAGE_ROLES, "Assign/remove moderator roles")
            );
            privilegeRepository.saveAll(privileges);
        }
    }
}

