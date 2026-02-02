package gio.hobist.Repository;

import gio.hobist.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByIdReceiverOrderByCreatedAtDesc(UUID idReceiver);
    List<Notification> findByIdSenderAndIdReceiverAndSubject(UUID idSender, UUID idReceiver, String subject);
}