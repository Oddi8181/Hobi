package gio.hobist.Controller;

import gio.hobist.Dto.MessageDto;
import gio.hobist.Service.ChatService;
import gio.hobist.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;


    @GetMapping("/friendList")
    public String friendList(Model model, HttpSession session){

        var userId=(UUID)session.getAttribute("userId");
        var friends=chatService.getUsers(userId);

        model.addAttribute("user",
                userService.getUser(userId));

        model.addAttribute("friends",friends);

        return "common/friendList.html";
    }

    @GetMapping("/{friendId}")
    public String openChat(HttpSession session, @PathVariable UUID friendId, Model model) {

        var userId=(UUID)session.getAttribute("userId");

        model.addAttribute("activeUser",
                userService.getUser(friendId));

        model.addAttribute("user",
                userService.getUser(userId));

        var friendship = chatService.getFriendshipId(userId, friendId);
        
        model.addAttribute("messages",
                chatService.getAllMessages(friendship.getId()));

        return "common/chat.html";
    }


    @GetMapping("/{friendId}/messages")
    public String openLatestMessages(HttpSession session, @PathVariable UUID friendId, Model model) {

        model.addAttribute("activeUser",
                userService.getUser(friendId));

        var userId=(UUID)session.getAttribute("userId");
        model.addAttribute("user",
                userService.getUser(userId));

        var friendship = chatService.getFriendshipId(userId, friendId);
        model.addAttribute("messages",
                chatService.getLatestMessages(friendship.getId()));

        return "common/latestMessagesList.html";
    }


    @GetMapping("/{friendId}/messagesHistory")
    public String openAllMessages(HttpSession session, @PathVariable UUID friendId, Model model) {

        model.addAttribute("activeUser",
                userService.getUser(friendId));

        var userId=(UUID)session.getAttribute("userId");
        model.addAttribute("user",
                userService.getUser(userId));

        var friendship = chatService.getFriendshipId(userId, friendId);
        model.addAttribute("messages",
                chatService.getAllMessages(friendship.getId()));

        return "common/allMessagesList.html";
    }

    @PostMapping("/{friendId}/send")
    public String sendMessage(@RequestParam String text, HttpSession session,@PathVariable UUID friendId) {
        var userId=(UUID)session.getAttribute("userId");


        var message = new MessageDto(
                null,
                chatService.getFriendshipId(userId,friendId).getId(),
                userId,
                text,
                null,
                null,
                null
        );

        chatService.createMessage(message);

//        return "common/chat.html";
        return "redirect:/chat/"+friendId;
    }

}
