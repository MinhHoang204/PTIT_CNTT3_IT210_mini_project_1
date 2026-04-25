package controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/todos")
public class TodoController {
    private final TodoRepository todoRepository;
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public String listTodos(Model model) {
        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("todo", new Todo()); // dùng cho form
        return "todo-list";
    }

    @PostMapping
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("todos", todoRepository.findAll());
            return "todo-list";
        }

        todo.setStatus("TODO");
        todoRepository.save(todo);
        return "redirect:/todos";
    }
}