package gio.hobist.Controller;

import gio.hobist.Service.NotificationService;
import gio.hobist.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    private final UserService userService;
    
    @GetMapping("/notifications")
    public String notificationsPage(Model model, HttpSession session) {
        var userId = (UUID) session.getAttribute("userId");

        var currentUser = userService.getUser(userId);
        model.addAttribute("user", currentUser);
        
        var notifications = notificationService.getUserNotifications(userId);
        model.addAttribute("notifications", notifications);
        return "notificationsPage.html";
    }
}
