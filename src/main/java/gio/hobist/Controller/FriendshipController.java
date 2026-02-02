package gio.hobist.Controller;

import gio.hobist.Service.ChatService;
import gio.hobist.Service.NotificationService;
import gio.hobist.Service.UserService;
import gio.hobist.Service.FriendshipService;
import gio.hobist.Dto.FriendshipDto;
import gio.hobist.Enum.Status;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.util.UUID;

@Controller
@RequestMapping("/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final ChatService chatService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final FriendshipService friendshipService;

    @GetMapping("/add/{friendId}")
    public String addFriend(HttpSession session, @PathVariable UUID friendId) {
        var userId = (UUID) session.getAttribute("userId");

        try {
            chatService.createFriendship(userId, friendId);

            var sender = userService.getUser(userId);
            notificationService.sendFriendRequestNotification(
                    userId,
                    friendId,
                    sender.getName() + " " + sender.getSurname()
            );

            return "redirect:/searchPage?success=friend_added";
        } catch (Exception e) {
            return "redirect:/searchPage?error=friend_request_failed";
        }
    }

    @GetMapping("/respond/{senderId}/{action}")
    public String respondToFriendRequest(HttpSession session,
                                         @PathVariable UUID senderId,
                                         @PathVariable String action) {
        var receiverId = (UUID) session.getAttribute("userId");

        try {
            var friendship = chatService.getFriendshipId(receiverId, senderId);
            if (friendship == null) {
                return "redirect:/notifications?error=no_friendship";
            }

            var dto = friendshipService.getCurrentFriendship(friendship.getId());
            if (dto == null) {
                return "redirect:/notifications?error=no_friendship";
            }

            switch (action.toLowerCase()) {
                case "accept":
                    dto.setStatus(Status.accepted);
                    dto.setDateOfBefriending(new Timestamp(System.currentTimeMillis()));
                    friendshipService.updateFriendship(friendship.getId(), dto);
                    break;
                case "decline":
                    friendshipService.deleteFriendship(friendship.getId());
                    break;
                case "block":
                    dto.setStatus(Status.blocked);
                    friendshipService.updateFriendship(friendship.getId(), dto);
                    break;
                default:
                    return "redirect:/notifications?error=unknown_action";
            }

            notificationService.deleteFriendRequestNotifications(senderId, receiverId);

            return "redirect:/notifications?success=friendship_updated";
        }
        catch (Exception e) {
            return "redirect:/notifications?error=friendship_update_failed";
        }
    }
}