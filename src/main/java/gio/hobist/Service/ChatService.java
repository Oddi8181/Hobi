package gio.hobist.Service;

import gio.hobist.Controller.DbFileTransferController;
import gio.hobist.Dto.CountryCityDto;
import gio.hobist.Dto.FriendshipDto;
import gio.hobist.Dto.MessageDto;
import gio.hobist.Dto.UserDto;
import gio.hobist.Entity.Message;
import gio.hobist.Enum.Status;
import gio.hobist.Repository.FriendshipRepository;
import gio.hobist.Repository.MessageRepository;
import gio.hobist.Repository.UserRepository;
import gio.hobist.utils.MessageEncryption;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    private final DbFileTransferController dbFileTransferController=new DbFileTransferController();
    private final UserService userService;

    public FriendshipDto getFriendshipId(UUID friend1Id, UUID friend2Id) {
       var friendship=friendshipRepository.findByUser1IdAndUser2Id(friend1Id,friend2Id);

      return new FriendshipDto(friendship.getId(),
              friendship.getUser1().getId(),
              friendship.getUser2().getId(),
              friendship.getStatus(),
              friendship.getDateOfBefriending()
              );
   }

  public List<UserDto> getUsers(UUID userId) {
       var friendships=friendshipRepository.findByUser1Id(userId);

        return friendships.stream().filter(f->f.getStatus().equals(Status.accepted))
                .map(f ->{
            var other=f.getUser1().getId().equals(userId)?f.getUser2():f.getUser1();
            var img=(other.getProfile_image()==null)?
                    "noImage.jpg"
                    :other.getProfile_image();
            String imageFileName=new String(img);


            return new UserDto(
                 other.getId(),
                 other.getName(),
                 other.getSurname(),
                 null,
                 null,
                 imageFileName,
                 Try.of(()-> new CountryCityDto(other.getCountry())).recover(NullPointerException.class, e->null).get(),
                 Try.of(()-> new CountryCityDto(other.getCity())).recover(NullPointerException.class,e->null).get(),
                 other.getUserPageDescription(),
                 userService.getNumberOfPosts(userId),
                 userService.getNumberOfFriends(userId)
            );

        }).toList();

  }


    public List<MessageDto> getAllMessages(UUID friendshipId) {
        var messages = messageRepository.findByFriendshipId(friendshipId);

        if (messages == null) {
            return null;
        }

        return messages.stream().map(message -> {
                try {
                    return new MessageDto(
                    message.getId(),
                            message.getFriendship().getId(),
                            message.getUser().getId(),
                            MessageEncryption.decrypt(message.getMessage().getBytes(StandardCharsets.UTF_8)),
                            message.getFile_name(),
                            message.getFile(),
                            message.getTimeSent().toLocalDateTime().toLocalTime().toString().substring(0, 5) //M.G: returns just hour and minutes
                    );
                }
                catch(InvalidKeyException e){
                    throw new RuntimeException(e);
                }
                catch(IllegalBlockSizeException e){
                    throw new RuntimeException(e);
                }
                catch (BadPaddingException e){
                    throw new RuntimeException(e);
                }
                }).toList();
    }

    public List<MessageDto> getLatestMessages(UUID friendshipId) {
        var messages = messageRepository.findTop36ByFriendshipIdOrderByTimeSentDesc(friendshipId);

        if (messages == null) {
            return null;
        }


            return messages.stream().map(message -> {
                try {
                    return new MessageDto(
                            message.getId(),
                            message.getFriendship().getId(),
                            message.getUser().getId(),
                            MessageEncryption.decrypt(message.getMessage().getBytes(StandardCharsets.UTF_8)),
                            message.getFile_name(),
                            message.getFile(),
                            message.getTimeSent().toLocalDateTime().toLocalTime().toString().substring(0, 5) //M.G: returns just hour and minutes
                    );
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                }
            }).toList().reversed();
    }

    public void createMessage(MessageDto messageDto) {
       var friendship=friendshipRepository.findById(messageDto.getFriendshipId());
       var user=userRepository.findById(messageDto.getUserId());

       try {
           var encryptedMessage= MessageEncryption.encrypt(messageDto.getMessage());
           messageDto.setMessage(new String(encryptedMessage));
       }
       catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
           System.out.println(e.getMessage());
       }

        var message = new Message();
        message.setFriendship(friendship.get());
        message.setUser(user.get());
        message.setMessage(messageDto.getMessage());
//        message.setFile_name(messageDto.getFileName());M.G: I will work on it later

        messageRepository.save(message);

    }

    public void deleteMessage(UUID messageId) {
        messageRepository.deleteById(messageId);
    }

    public void createFriendship(UUID userId, UUID friendId) {
        var user1 = userRepository.findById(userId).orElseThrow();
        var user2 = userRepository.findById(friendId).orElseThrow();
        
        var friendship = new gio.hobist.Entity.Friendship();
        friendship.setUser1(user1);
        friendship.setUser2(user2);
        friendship.setStatus(Status.valueOf("pending"));
        
        friendshipRepository.save(friendship);
    }
}
