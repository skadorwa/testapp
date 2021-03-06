package pl.onsight.wypozyczalnia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.onsight.wypozyczalnia.model.entity.UserEntity;


@Controller
public class LoginController {

    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        modelAndView.addObject("user", new UserEntity());
        return modelAndView;
    }
}
