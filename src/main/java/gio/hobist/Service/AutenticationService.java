package gio.hobist.Service;

import gio.hobist.Dto.AutenticationDto;
import gio.hobist.Entity.User;
import gio.hobist.Exception.AutenticationException;
import gio.hobist.Repository.UserRepository;
import gio.hobist.utils.PasswordHasher;
import gio.hobist.utils.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticationService {

    @Autowired
    private UserRepository userRepository;

    public AutenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void signUpUser(AutenticationDto DtoUser){
        if(DtoUser.getName()==null || DtoUser.getName().equals("") ){
            throw new AutenticationException("name missing");
        }
        else if(DtoUser.getSurname()==null || DtoUser.getSurname().equals("") ){
            throw new AutenticationException("surname missing");

        }
        else if(DtoUser.getPassword()==null || DtoUser.getPassword().equals("") ){
            throw new AutenticationException("password missing");
        }
        
        // Validate password strength
        var passwordErrors = PasswordValidator.validatePassword(DtoUser.getPassword());
        if (!passwordErrors.isEmpty()) {
            throw new AutenticationException(String.join(", ", passwordErrors));
        }
        
        if(DtoUser.getEmail()==null || DtoUser.getEmail().equals("") ){
            throw new AutenticationException("email missing");
        }
        else if(DtoUser.getConfirmPassword()==null || DtoUser.getConfirmPassword().equals("") ){
            throw new AutenticationException("confirmed password missing");
        }
        else if(!DtoUser.getPassword().equals(DtoUser.getConfirmPassword())){
            throw new AutenticationException("passwords do not match");
        }

        PasswordHasher hashingObject=new PasswordHasher();
        var hashedPassword=hashingObject.hashPassword(DtoUser.getPassword());


        var user = new User(
                DtoUser.getName(),
                DtoUser.getSurname(),
                DtoUser.getEmail(),
                hashedPassword
        );

      userRepository.save(user);
    }

    public AutenticationDto logInUser(AutenticationDto DtoUser){

        if(DtoUser.getEmail()==null || DtoUser.getEmail().equals("") ){
            throw new AutenticationException("email missing");
        }
        else if(DtoUser.getPassword()==null || DtoUser.getPassword().equals("") ){
            throw new AutenticationException("password missing");
       }

        var user =userRepository.findByEmail(DtoUser.getEmail());
        if(user==null){
            throw new AutenticationException("Email doesn't exist");
        }

       if (PasswordHasher.verifyPassword(DtoUser.getPassword(),user.getPassword())){

           DtoUser.cleanOldData();
           DtoUser.setName(user.getName());
           DtoUser.setId(user.getId());
           return DtoUser;
       }
       else{
           throw new AutenticationException("invalid input");

       }

    }


}
