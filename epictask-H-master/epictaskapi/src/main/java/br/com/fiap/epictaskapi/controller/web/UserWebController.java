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

import br.com.fiap.epictaskapi.model.User;
import br.com.fiap.epictaskapi.service.UserService;

@Controller
@RequestMapping("/user")
public class UserWebController { 

    @Autowired
    UserService service;

    @GetMapping
    public ModelAndView index(@RequestParam(defaultValue = "all") String filter, @PageableDefault(size = 5) Pageable pageable){
        Page<User> list = service.listAll(pageable);

        return new ModelAndView("task/done")
                    .addObject("users", list);
    }

    @GetMapping("new")
    public String form(User user){
        return "user/form";
    }

    @PostMapping
    public String save(@Valid User user, BindingResult binding, RedirectAttributes redirect){
        if (binding.hasErrors()) return "user/form";
        String message = (user.getId() != null)?
                        "Usuário alterado com sucesso":"Usuário cadastrado com sucesso";

        service.save(user);
        redirect.addFlashAttribute("message", message);
        return "redirect:/user";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect){
        service.remove(id);
        redirect.addFlashAttribute("message", "Usuário apagado com sucesso");
        return "redirect:/user";
    }

    @GetMapping("{id}")
    public ModelAndView edit(@PathVariable Long id){
        Optional<User> optional = service.getById(id);
        if (optional.isEmpty()) return new ModelAndView("/user");

        return new ModelAndView("user/form").addObject("user", optional.get());
    }
    
}