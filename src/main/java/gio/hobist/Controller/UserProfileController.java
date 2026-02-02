package gio.hobist.Controller;

import gio.hobist.Service.UserService;
import gio.hobist.Entity.Post;
import gio.hobist.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final PostRepository postRepository;

    @GetMapping("/user/{id}")
    public String viewUserProfile(@PathVariable("id") UUID id, Model model) {
        var userDto = userService.getUser(id);
        if (userDto == null) {
            return "errors/404";
        }
        model.addAttribute("user", userDto);

        List<Post> posts = postRepository.findAllByIdUser(id);
        model.addAttribute("posts", posts);

        return "common/profile";
    }

    @GetMapping("/user/{id}/posts")
    public String viewUserPosts(@PathVariable("id") UUID id, Model model) {
        List<Post> posts = postRepository.findAllByIdUser(id);
        model.addAttribute("posts", posts);
        model.addAttribute("profileOwnerId", id);
        return "common/posts-fragment";
    }
}