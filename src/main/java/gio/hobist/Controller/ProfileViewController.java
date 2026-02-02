package gio.hobist.Controller;

import gio.hobist.Entity.Friendship;
import gio.hobist.Enum.Status;
import gio.hobist.Dto.UserDto;
import gio.hobist.Repository.FriendshipRepository;
import gio.hobist.Repository.PostRepository;
import gio.hobist.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProfileViewController {

    private final UserService userService;
    private final FriendshipRepository friendshipRepository;
    private final PostRepository postRepository;

    @GetMapping("/profile/view/{id}")
    public String viewProfile(@PathVariable("id") UUID id, Model model, HttpSession session) {
        UUID currentUserId = (UUID) session.getAttribute("userId");

        UserDto userDto = userService.getUser(id);
        if (userDto == null) {
            return "errors/404";
        }
        model.addAttribute("user", userDto);

        boolean isFriend = false;
        if (currentUserId != null) {
            Friendship f = friendshipRepository.findByUser1IdAndUser2Id(currentUserId, id);
            if (f != null && f.getStatus() == Status.accepted) {
                isFriend = true;
            }
        }
        model.addAttribute("isFriend", isFriend);

        if (isFriend) {
            var posts = postRepository.findAllByIdUser(id);
            model.addAttribute("posts", posts);
        }

        return "common/profile-view";
    }
}