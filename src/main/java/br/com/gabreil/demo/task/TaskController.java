package br.com.gabreil.demo.task;

import br.com.gabreil.demo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The start/end date must be after the current date.");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The start date must be less than the end date.");
        }

        var task = taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }


    @GetMapping
    public List<TaskModel> list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");

        var tasks = this.taskRepository.findByUserId((UUID) userId);

        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(400).body("Task not found.");
        }

        var userId = request.getAttribute("userId");

        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(400).body("The user does not have permission to change this task");
        }

        Utils.copyNoNullProperties(taskModel, task);

        this.taskRepository.save(task);

        return ResponseEntity.status(200).body(task);
    }
}
