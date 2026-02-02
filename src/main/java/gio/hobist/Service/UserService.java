package gio.hobist.Service;

import gio.hobist.Dto.CountryCityDto;
import gio.hobist.Dto.UserDto;
import gio.hobist.Enum.Status;
import gio.hobist.Repository.FriendshipRepository;
import gio.hobist.Repository.PostRepository;
import gio.hobist.Repository.UserRepository;
import gio.hobist.Controller.DbFileTransferController;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import gio.hobist.Repository.HobbyUserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;
    private final DbFileTransferController dbFileTransferController;


    public UserDto getUser(UUID userId){
       var user= userRepository.findByid(userId);

       var img=(user.getProfile_image()==null)?
               "noImage.jpg"
               :user.getProfile_image();
        String imageFileName=new String(img);


        return new UserDto(user.getId(),
               user.getName(),
                user.getSurname(),
                null,
                user.getEmail(),
               imageFileName,
               Try.of(()-> new CountryCityDto(user.getCountry())).recover(NullPointerException.class,e->null).get(),
               Try.of(()-> new CountryCityDto(user.getCity())).recover(NullPointerException.class,e->null).get(),
               user.getUserPageDescription(),
               getNumberOfPosts(user.getId()),
               getNumberOfFriends(user.getId())
               );
    }

    public List<UserDto> searchByQuery(String query) {
        var users = userRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(query, query);


        return users.stream().map(user -> {

            var img=(user.getProfile_image()==null)?
                    "noImage.jpg"
                    :user.getProfile_image();
            String imageFileName=new String(img);

            return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                null,
                user.getEmail(),
                imageFileName,
                Try.of(()-> new CountryCityDto(user.getCountry())).recover(NullPointerException.class,e->null).get(),
                Try.of(()-> new CountryCityDto(user.getCity())).recover(NullPointerException.class,e->null).get(),
                user.getUserPageDescription(),
                getNumberOfPosts(user.getId()),
                getNumberOfFriends(user.getId())
        );}

        ).toList();

    }


    @Autowired
    private HobbyUserRepository hobbyUserRepository;

    public List<UserDto> searchByQueryWithSharedHobby(String query, UUID currentUserId) {

        var users = userRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(query, query);

        var myHobbyIds = hobbyUserRepository.findByUser_Id(currentUserId)
                .stream()
                .map(hu -> hu.getHobby().getId())
                .toList();

        if (myHobbyIds.isEmpty()) {
            return List.of();
        }


        return users.stream()
                .filter(u -> !u.getId().equals(currentUserId))
                .filter(u -> {
                    for (var hu : hobbyUserRepository.findByUser_Id(u.getId())) {
                        if (myHobbyIds.contains(hu.getHobby().getId())) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(user -> {

                    var img=(user.getProfile_image()==null)?//M.G:tweaked for new version of fileTransfer
                            "noImage.jpg"
                            :user.getProfile_image();
                    String finalImageFileName=new String(img);

                    return new UserDto(
                            user.getId(),
                            user.getName(),
                            user.getSurname(),
                            null,
                            user.getEmail(),
                            finalImageFileName,
                            Try.of(() -> new CountryCityDto(user.getCountry())).recover(NullPointerException.class,e->null).get(),
                            Try.of(() -> new CountryCityDto(user.getCity())).recover(NullPointerException.class,e->null).get(),
                            user.getUserPageDescription(),
                            getNumberOfPosts(user.getId()),
                            getNumberOfFriends(user.getId())
                    );}
                ).toList();
//                .toList();
    }


    public int getNumberOfPosts(UUID userId){
       var posts=Try.of(()->postRepository.findAllByIdUser(userId)).getOrElse(ArrayList::new);
       return posts.size();
   }

   public int getNumberOfFriends(UUID userId){
       var friendships=Try.of(()->friendshipRepository.findByUser1Id(userId)).getOrElse(ArrayList::new);
       var friends=friendships.stream().filter(f->f.getStatus().equals(Status.accepted)).toList();

       return friends.size();
   }

}
