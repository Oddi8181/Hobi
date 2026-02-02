package gio.hobist.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private String token;
    private String newPassword;
    private String confirmPassword;
}
