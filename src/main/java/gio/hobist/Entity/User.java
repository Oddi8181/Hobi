package gio.hobist.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String surname;

    @Column(name = "e_mail")
    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "id_country")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private City city;

    @Column(name = "number_of_posts")
    private Integer numberOfPosts;

    @Column(name = "number_of_friends")
    private Integer numberOfFriends;

    @Column(name = "user_page_description")
    private String userPageDescription;

    @Column(name = "profile_image")
    private String profile_image;

    public User( String userName, String userSurname,  String email,  String password) {//M.G: this constructor is in use don't delete it again!!
        this.name = userName;
        this.surname = userSurname;
        this.email = email;
        this.password = password;
    }

    public User(User u){
       this.id = u.getId();
       this.name = u.getName();
       this.surname = u.getSurname();
       this.email = u.getEmail();
       this.password = u.getPassword();
       this.country = u.getCountry();
       this.city = u.getCity();
       this.numberOfPosts = u.getNumberOfPosts();
       this.numberOfFriends = u.getNumberOfFriends();
       this.userPageDescription = u.getUserPageDescription();
       this.profile_image = u.getProfile_image();
    }
}
