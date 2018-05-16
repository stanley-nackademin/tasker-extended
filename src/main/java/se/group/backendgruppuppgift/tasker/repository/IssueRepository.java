package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Issue;

import java.util.List;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long> {

    @Override
    List<Issue> findAll();
}
