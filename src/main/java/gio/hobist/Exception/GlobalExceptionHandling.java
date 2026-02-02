package gio.hobist.Exception;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.FileNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(AutenticationException.class)
    public ResponseEntity<String> handleAutenticationException(AutenticationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NullPointerException.class)
    public String
    NullPointerException(NullPointerException e, HttpSession session) {//M.G: still catches all NullpointerExceptions. need to fix later.
        if (session.getAttribute("user")==null ) {
            return new String("redirect:/login");
        }

        nullPointerException(e);
        return null;
    }

    public ResponseEntity<String>
    nullPointerException(NullPointerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        if (e.getMessage().contains("user_e_mail_key")) {
            return new ResponseEntity<>("Email address is already registered", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Data validation error", HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(FileNotFoundException.class) //M.G: tmp
//    public ResponseEntity<String>
//    FileNotFoundException(FileNotFoundException e) {
//
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//
//    }

}