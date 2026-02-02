package gio.hobist.Controller;

import gio.hobist.Dto.UserDto;
import gio.hobist.Enum.Status;
import gio.hobist.Repository.FriendshipRepository;
import gio.hobist.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserSearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @GetMapping("/searchPage")
    public String searchPage(Model model, HttpSession session) {
        var userId = (UUID) session.getAttribute("userId");
        if (userId != null) {
            var currentUser = userService.getUser(userId);
            model.addAttribute("user", currentUser);
        }
        return "common/searchPage";
    }

    @GetMapping("/search/users")
    public String searchUsers(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "all") String mode,
            Model model,
            HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", userService.getUser(userId));

        if (q == null || q.trim().isEmpty()) {
            return "common/searchPage";
        }

        try {
            List<UserDto> users;
            if ("sharedHobby".equals(mode)) {
                users = userService.searchByQueryWithSharedHobby(q, userId)
                        .stream()
                        .filter(u -> !u.getId().equals(userId))
                        .toList();
            } else {
                users = userService.searchByQuery(q)
                        .stream()
                        .filter(u -> !u.getId().equals(userId))
                        .toList();
            }
            model.addAttribute("users", users);
            if (users.isEmpty()) {
                model.addAttribute("noResults", "No users found for: " + q);
            }

            var friendships = friendshipRepository.findByUser1Id(userId);
            var acceptedFriendIds = friendships.stream()
                    .filter(f -> f.getStatus() != null && f.getStatus().equals(Status.accepted))
                    .map(f -> {
                        var other = f.getUser1().getId().equals(userId) ? f.getUser2() : f.getUser1();
                        return other.getId();
                    })
                    .toList();

            model.addAttribute("acceptedFriends", acceptedFriendIds);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Search failed. Please try again.");
        }

        return "common/searchPage";
    }
}