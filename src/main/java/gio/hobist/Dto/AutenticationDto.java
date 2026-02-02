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
public class AutenticationDto {
    private UUID id;
    private String name;
    private String surname;
    private String email;
   private String password;
   private String confirmPassword;

    public void cleanOldData(){//M.G: cleans unnecessary data for better security
        id=null;
        name=null;
        surname=null;
        email =null;
        password=null;
        confirmPassword=null;
    }
}
