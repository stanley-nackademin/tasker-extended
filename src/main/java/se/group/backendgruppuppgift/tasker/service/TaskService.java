package se.group.backendgruppuppgift.tasker.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;

import java.util.Optional;

@Service
public final class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }


    public Task createTask(TaskWeb task) {
        System.out.println(task); // TODO: 2018-05-09 Remove in final stage of development
        return repository.save(new Task(task.getName(), task.getStatus()));
    }

    public Optional<Task> updateTask(Long id, TaskWeb task) {
        Optional<Task> result = repository.findById(id);
        System.out.println(task);
        if (result.isPresent()) {
            Task updatedTask = result.get();

            if (!StringUtils.isBlank(task.getName())) {
                updatedTask.setName(task.getName());
            }
            if (!StringUtils.isBlank(task.getStatus().toString())) {
                updatedTask.setStatus(task.getStatus());
            }

            System.out.println(updatedTask);

            return Optional.ofNullable(repository.save(updatedTask));
        }

        return Optional.empty();
    }
}
