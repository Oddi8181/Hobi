package gio.hobist.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String profilePicture;
    private CountryCityDto country;
    private CountryCityDto city;
    private String userPageDescription;
    private int numberOfPosts;
    private int numberOfFriends;
}

