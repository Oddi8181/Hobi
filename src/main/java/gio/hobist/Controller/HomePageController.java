package gio.hobist.Controller;

import gio.hobist.Service.HomePageService;
import gio.hobist.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class HomePageController {

    private final HomePageService homePageService;
    private final UserService userService;

    public HomePageController(HomePageService homePageService, UserService userService) {
        this.homePageService = homePageService;
        this.userService = userService;
    }



    @GetMapping(path="/home")
    public String home(Model model, HttpSession session){


        var userId=(UUID) session.getAttribute("userId");

        var feedPosts = homePageService.findAllPosts(userId);
        model.addAttribute("feed", feedPosts);

        var currentUser = userService.getUser(userId);
        model.addAttribute("user", currentUser);
        return "homePage.html";
    }

    @GetMapping("/logout")
    public String  logout(HttpSession session){

        session.invalidate();
        return "redirect:/";
    }

}
