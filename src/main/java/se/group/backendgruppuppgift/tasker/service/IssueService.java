package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.repository.IssueRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidIssueException;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public final class IssueService {

    private final IssueRepository repository;

    public IssueService(IssueRepository repository) {
        this.repository = repository;
    }

    public Optional<Issue> findIssue(Long id) {
        return repository.findById(id);
    }

    public List<Issue> getAll() {
        return repository.findAll();
    }

    public Optional<Issue> updateIssue(Long id, Issue issue) {
        validateIssue(issue);
        Optional<Issue> result = repository.findById(id);

        if (result.isPresent()) {
            Issue updatedIssue = result.get();
            updatedIssue.setDescription(issue.getDescription());
            updatedIssue = repository.save(updatedIssue);

            return Optional.ofNullable(updatedIssue);
        }

        return Optional.empty();
    }

    private void validateIssue(Issue issue) {
        if (isBlank(issue.getDescription())) {
            throw new InvalidIssueException("Description can not be empty");
        }
    }
}
