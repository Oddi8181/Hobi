package gio.hobist.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserSettingsFormDto {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private Integer countryId;
    private Integer cityId;
    private String description;

    private List<UUID> hobbyIds;

}
