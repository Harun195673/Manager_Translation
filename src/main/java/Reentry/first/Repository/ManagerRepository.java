package Reentry.first.Repository;
import Reentry.first.Entity.Manager;

import Reentry.first.Entity.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerRepository extends JpaRepository <Manager, Long> {


    @Query("SELECT w FROM WorkGroup w JOIN FETCH w.manager")
    List<WorkGroup> getWorkGroups();

    Boolean existsByName(String name);




}
