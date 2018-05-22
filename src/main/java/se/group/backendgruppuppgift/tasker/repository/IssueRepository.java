package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Issue;

import java.util.List;

@Repository
public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

    @Override
    List<Issue> findAll();
}
