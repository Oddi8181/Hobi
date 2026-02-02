package gio.hobist.utils;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {
    
    public static List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        
        if (password == null || password.isEmpty()) {
            errors.add("Password is required");
            return errors;
        }
        
        if (password.length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one number");
        }
        
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            errors.add("Password must contain at least one special character");
        }
        
        return errors;
    }
    
    public static String getPasswordRequirements() {
        return "Password must contain: at least 8 characters, one uppercase letter, one lowercase letter, one number, and one special character";
    }
}