package controller;

import entity.Todo;
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
        model.addAttribute("todo", new Todo());
        return "todo-list";
    }

    @PostMapping
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("todos", todoRepository.findAll());
            return "todo-list";
        }

        todoRepository.save(todo);
        redirectAttributes.addFlashAttribute("message", "Lưu thành công!");
        return "redirect:/todos";
    }

    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable Long id, Model model) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task"));

        model.addAttribute("todo", todo);
        model.addAttribute("todos", todoRepository.findAll());
        return "todo-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {

        todoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công!");
        return "redirect:/todos";
    }

    @GetMapping("/start")
    public String showStartPage() {
        return "start";
    }

    @PostMapping("/start")
    public String saveOwner(@RequestParam String ownerName,
                            HttpSession session) {
        session.setAttribute("owner", ownerName);
        return "redirect:/todos";
    }

    @GetMapping
    public String listTodos(Model model, HttpSession session) {
        String owner = (String) session.getAttribute("owner");
        if (owner == null) {
            return "redirect:/todos/start";
        }

        model.addAttribute("owner", owner);
        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("todo", new Todo());

        return "todo-list";
    }
}