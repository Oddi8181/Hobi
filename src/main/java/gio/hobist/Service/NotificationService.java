package gio.hobist.Service;

import gio.hobist.Entity.Notification;
import gio.hobist.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public void sendLikeNotification(UUID senderId, UUID receiverId, String senderName, String postTitle) {
        var notification = new Notification(
            senderId, 
            receiverId, 
            "LIKE", 
            senderName + " liked your post: " + postTitle
        );
        notificationRepository.save(notification);
    }
    
    public void sendCommentNotification(UUID senderId, UUID receiverId, String senderName, String postTitle) {
        var notification = new Notification(
            senderId, 
            receiverId, 
            "COMMENT", 
            senderName + " commented on your post: " + postTitle
        );
        notificationRepository.save(notification);
    }
    
    public void sendFriendRequestNotification(UUID senderId, UUID receiverId, String senderName) {
        var notification = new Notification(
            senderId, 
            receiverId, 
            "FRIEND_REQUEST", 
            senderName + " sent you a friend request"
        );
        notificationRepository.save(notification);
    }
    
    public List<Notification> getUserNotifications(UUID userId) {
        return notificationRepository.findByIdReceiverOrderByCreatedAtDesc(userId);
    }
    public void deleteFriendRequestNotifications(UUID senderId, UUID receiverId) {
        var list = notificationRepository.findByIdSenderAndIdReceiverAndSubject(senderId, receiverId, "FRIEND_REQUEST");
        if (list != null && !list.isEmpty()) {
            notificationRepository.deleteAll(list);
        }
    }
}