package gio.hobist.Controller;

import gio.hobist.Service.HomePageService;
import gio.hobist.Service.UserService;
import gio.hobist.Dto.PostDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ForYouController {

    private final HomePageService homePageService;
    private final UserService userService;

    public ForYouController(HomePageService homePageService, UserService userService) {
        this.homePageService = homePageService;
        this.userService = userService;
    }

    @GetMapping("/forYou")
    public String forYou(Model model, HttpSession session){
        var userId = (UUID) session.getAttribute("userId");

        List<PostDto> feedPosts = homePageService.findFriendPosts(userId);
        model.addAttribute("feed", feedPosts);

        var currentUser = userService.getUser(userId);
        model.addAttribute("user", currentUser);

        var authorIds = feedPosts.stream()
                .map(PostDto::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<UUID, Object> authors = new HashMap<>();
        for (UUID id : authorIds) {
            authors.put(id, userService.getUser(id));
        }
        model.addAttribute("authors", authors);

       return "forYouPage";
    }
}