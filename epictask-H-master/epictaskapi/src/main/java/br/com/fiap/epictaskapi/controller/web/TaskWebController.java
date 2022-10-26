package br.com.fiap.epictaskapi.controller.web;

import java.util.Optional;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.epictaskapi.model.Task;
import br.com.fiap.epictaskapi.service.TaskService;

@Controller
@RequestMapping("/task")
public class TaskWebController {

    @Autowired
    TaskService service;

    @GetMapping
    public ModelAndView index(@RequestParam(defaultValue = "all") String filter, @PageableDefault(size = 5) Pageable pageable){
        Page<Task> list = (filter.equals("done"))?(Page<Task>) service.listDone():service.listAll(pageable) ;

        return new ModelAndView("task/index")
                    .addObject("tasks", list);
    }

    @GetMapping("new")
    public String form(Task task){
        return "task/form";
    }

    @PostMapping
    public String save(@Valid Task task, BindingResult binding, RedirectAttributes redirect){
        if (binding.hasErrors()) return "task/form";
        String message = (task.getId() != null)?
                        "Tarefa alterada com sucesso":"Tarefa cadastrada com sucesso";

        service.save(task);
        redirect.addFlashAttribute("message", message);
        return "redirect:/task";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect){
        service.remove(id);
        redirect.addFlashAttribute("message", "Tarefa apagada com sucesso");
        return "redirect:/task";
    }

    @GetMapping("{id}")
    public ModelAndView edit(@PathVariable Long id){
        Optional<Task> optional = service.getById(id);
        if (optional.isEmpty()) return new ModelAndView("/task");

        return new ModelAndView("task/form").addObject("task", optional.get());
    }
    
}
