package se.group.backendgruppuppgift.tasker.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
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

    public Optional<Task> findTask(Long id) {
        return repository.findById(id);
    }

    public void deleteTask(Long id){

        repository.deleteById(id);
    }

    public List<Task> findTaskByStatus(String status){

        status = checkString(status);

        if(status.equals("started"))
        {
            return repository.findTaskByStatus(TaskStatus.STARTED);
        }

        else if(status.equals("unstarted"))
        {
            return repository.findTaskByStatus(TaskStatus.UNSTARTED);
        }

        else if(status.equals("done"))
        {
            return repository.findTaskByStatus(TaskStatus.DONE);
        }

        return new ArrayList<>();

    }

    public List<Task> findTaskByText(String searchText){
        String result = checkString(searchText);
        return repository.findAllByNameContains(result);
    }

    public void deleteTaskById(Long id){
        repository.deleteById(id);
    }

    public String checkString(String string){

        String result = string;

        result = result.trim();
        result = result.toLowerCase();

        return result;


    }

}
