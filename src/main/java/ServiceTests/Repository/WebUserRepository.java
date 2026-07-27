package management_workflow_api.Repository;

import management_workflow_api.Entity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebUserRepository extends JpaRepository<WebUser, Long> {

    Optional<WebUser> findByUsername(String username);

    boolean existsByUsername(String username);
}