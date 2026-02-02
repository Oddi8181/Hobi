package gio.hobist.Service;

import gio.hobist.Entity.*;
import gio.hobist.Repository.*;
import gio.hobist.utils.PasswordHasher;
import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final HobbyRepository hobbyRepository;
    private final HobbyUserRepository hobbyUserRepository;
    private final DbFileTransferService dbFileTransferService;

    private final PasswordHasher passwordHasher = new PasswordHasher();


//    public User getCurrentUser(UUID userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }


    @Transactional
    public void changeEmail(UUID userId, String newEmail, BindingResult binding) {
        User user = userRepository.findByid(userId);

        if (isBlank(newEmail)) {
            binding.rejectValue("email", "email.empty", "Email cannot be empty.");
            return;
        }
        if (!newEmail.contains("@")) {
            binding.rejectValue("email", "email.invalid", "Invalid email format.");
            return;
        }


        if (newEmail.equalsIgnoreCase(user.getEmail())) {
            return;
        }


        if (userRepository.existsByEmail(newEmail)) {
            binding.rejectValue("email", "email.exists", "Email already in use.");
            return;
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }


    @Transactional
    public void changePassword(
            UUID userId,
            String currentPassword,
            String newPassword,
            String confirmNewPassword,
            BindingResult binding
    ) {
        User user =userRepository.findByid(userId);

        if (isBlank(currentPassword)) {
            binding.rejectValue("currentPassword", "password.empty", "Current password is required.");
            return;
        }
        if (isBlank(newPassword)) {
            binding.rejectValue("newPassword", "password.empty", "New password is required.");
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            binding.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match.");
            return;
        }
        if (newPassword.length() < 8) {
            binding.rejectValue("newPassword", "password.weak", "Password must be at least 8 characters.");
            return;
        }


        if (!passwordHasher.verifyPassword(currentPassword, user.getPassword())) {
            binding.rejectValue("currentPassword", "password.wrong", "Current password is incorrect.");
            return;
        }

        user.setPassword(passwordHasher.hashPassword(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateLocation(UUID userId, Integer countryId, Integer cityId, BindingResult binding) {
        User user =userRepository.findByid(userId);


        if (countryId == null) {
            binding.rejectValue("countryId", "country.required", "Country is required.");
            return;
        }
        if (cityId == null) {
            binding.rejectValue("cityId", "city.required", "City is required.");
            return;
        }

        Country country = countryRepository.findById(countryId).orElse(null);
        if (country == null) {
            binding.rejectValue("countryId", "country.invalid", "Invalid country.");
            return;
        }

        City city = cityRepository.findById(cityId).orElse(null);
        if (city == null) {
            binding.rejectValue("cityId", "city.invalid", "Invalid city.");
            return;
        }


        if (city.getCountry() == null || !Objects.equals(city.getCountry().getId(), countryId)) {
            binding.rejectValue("cityId", "city.mismatch", "City does not belong to selected country.");
            return;
        }

        user.setCountry(country);
        user.setCity(city);
        userRepository.save(user);
    }


    @Transactional
    public void updateHobbies(UUID userId, List<UUID> hobbyIds, BindingResult binding) {


        if (hobbyIds == null || hobbyIds.isEmpty()) {
            hobbyUserRepository.deleteByUser_Id(userId);
            return;
        }


        var hobbies = hobbyRepository.findAllById(hobbyIds);
        if (hobbies.size() != hobbyIds.size()) {
            binding.rejectValue("hobbyIds", "hobby.invalid", "One or more selected hobbies are invalid.");
            return;
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        hobbyUserRepository.deleteByUserId(userId);


        List<HobbyUser> links = new ArrayList<>();
        for (Hobby hobby : hobbies) {
            HobbyUser link = new HobbyUser();
            link.setUser(user);
            link.setHobby(hobby);
            links.add(link);
        }
        hobbyUserRepository.saveAll(links);
    }



    @Transactional
    public void updateProfilePicture(UUID userId, MultipartFile file, BindingResult binding) {

        User user =userRepository.findByid(userId);

        if(file == null || file.isEmpty()){
            binding.reject("profileImage.empty", "Please provide a profile picture.");
            return;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            binding.reject("profileImage.type", "Only image files are supported.");
            return;
        }
        if (file.getSize() > 2_000_000) {
            binding.reject("profileImage.size", "Image is too large (max 2MB).");
            return;
        }




        try {
            dbFileTransferService.saveFile(user.getId(), file);
        }
        catch (IOException e) {
            e.printStackTrace();
            binding.reject("profileImage.io", "Failed to save image.");
            return;
        }

        user.setProfile_image(file.getOriginalFilename());
        userRepository.save(user);

    }

    @Transactional
    public void updateDescription(UUID userId, String description, BindingResult binding) {
        User user =userRepository.findByid(userId);

        if (description == null) description = "";
        if (description.length() > 500) {
            binding.rejectValue("description", "description.tooLong", "Description can be max 500 characters.");
            return;
        }

        user.setUserPageDescription(description.trim());
        userRepository.save(user);
    }


    private String getSafeExtension(String originalName) {
        if (originalName == null) return ".png";
        String lower = originalName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return ".jpg";
        if (lower.endsWith(".png")) return ".png";
        if (lower.endsWith(".webp")) return ".webp";
        return ".png";
    }

}
