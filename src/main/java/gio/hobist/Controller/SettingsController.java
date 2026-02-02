package gio.hobist.Controller;

import gio.hobist.Dto.UserSettingsFormDto;
import gio.hobist.Entity.User;
import gio.hobist.Repository.CityRepository;
import gio.hobist.Repository.CountryRepository;
import gio.hobist.Repository.HobbyRepository;
import gio.hobist.Repository.HobbyUserRepository;
import gio.hobist.Service.SettingsService;
import gio.hobist.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;
    private final UserService userService;

    private final HobbyRepository hobbyRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final HobbyUserRepository hobbyUserRepository;



    @GetMapping("/settings")
    public String settings(Model model, HttpSession session,
                           @RequestParam(required = false) String tab) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        var user =userService.getUser(userId);
        model.addAttribute("user", user);

        UserSettingsFormDto form = new UserSettingsFormDto();
        form.setEmail(user.getEmail());

        if (user.getCountry() != null) form.setCountryId(user.getCountry().getId());
        if (user.getCity() != null) form.setCityId(user.getCity().getId());
        if (user.getUserPageDescription() != null) form.setDescription(user.getUserPageDescription());

        var selectedHobbyIds = hobbyUserRepository.findByUser_Id(userId)
                .stream()
                .map(hu -> hu.getHobby().getId())
                .toList();
        form.setHobbyIds(selectedHobbyIds);
        model.addAttribute("settingsForm", form);

        model.addAttribute("allCountries", countryRepository.findAll());

        if (form.getCountryId() != null) {
            model.addAttribute("cities", cityRepository.findByCountryId(form.getCountryId()));
        } else {
            model.addAttribute("cities", List.of());
        }


        model.addAttribute("allHobbies", hobbyRepository.findAll());

        model.addAttribute("tab", tab != null ? tab : "account");

        return "settings";
    }

    @PostMapping("/settings/email")
    public String updateEmail(@ModelAttribute("settingsForm") UserSettingsFormDto form,
                              BindingResult binding,
                              Model model,
                              HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        settingsService.changeEmail(userId, form.getEmail(), binding);

        if (binding.hasErrors()) {
            refillModel(model, userId, form, "account");
            return "settings";
        }

        return "redirect:/settings?tab=account&saved=true";
    }


    @PostMapping("/settings/password")
    public String updatePassword(@ModelAttribute("settingsForm") UserSettingsFormDto form,
                                 BindingResult binding,
                                 Model model,
                                 HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        settingsService.changePassword(
                userId,
                form.getCurrentPassword(),
                form.getNewPassword(),
                form.getConfirmPassword(),
                binding
        );

        if (binding.hasErrors()) {
            refillModel(model, userId, form, "security");
            return "settings";
        }

        return "redirect:/settings?tab=security&saved=true";
    }


    @PostMapping("/settings/location")
    public String updateLocation(@ModelAttribute("settingsForm") UserSettingsFormDto form,
                                 BindingResult binding,
                                 Model model,
                                 HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        settingsService.updateLocation(userId, form.getCountryId(), form.getCityId(), binding);

        if (binding.hasErrors()) {
            refillModel(model, userId, form, "location");
            return "settings";
        }

        return "redirect:/settings?tab=location&saved=true";
    }

    @PostMapping("/settings/hobbies")
    public String updateHobbies(@ModelAttribute("settingsForm") UserSettingsFormDto form,
                                BindingResult binding,
                                Model model,
                                HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        settingsService.updateHobbies(userId, form.getHobbyIds(), binding);
        if (binding.hasErrors()) {
            refillModel(model, userId, form, "hobbies");
            return "settings";
        }
        return "redirect:/settings?tab=hobbies&saved=true";
    }

    @PostMapping("/settings/profile-picture")
    public String updateProfilePicture(@RequestParam("profileImage") MultipartFile file,
                                       @ModelAttribute("settingsForm") UserSettingsFormDto form,
                                       BindingResult binding,
                                       Model model,
                                       HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        settingsService.updateProfilePicture(userId, file, binding);

        if (binding.hasErrors()) {
            refillModel(model, userId, form, "profile");
            return "settings";
        }

        return "redirect:/settings?tab=profile&saved=true";
    }


    @GetMapping("/settings/cities")
    public String citiesByCountry(@RequestParam Integer countryId, Model model) {
        model.addAttribute("cities", cityRepository.findByCountryId(countryId));
        return "common/city-options";
    }

    @PostMapping("/settings/description")
    public String updateDescription(@ModelAttribute("settingsForm") UserSettingsFormDto form,
                                    BindingResult binding,
                                    Model model,
                                    HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        settingsService.updateDescription(userId, form.getDescription(), binding);
        if (binding.hasErrors()) {
            refillModel(model, userId, form, "description");
            return "settings";
        }
        return "redirect:/settings?tab=description&saved=true";
    }



    private void refillModel(Model model, UUID userId, UserSettingsFormDto form, String tab) {
        var user = userService.getUser(userId);
        model.addAttribute("user", user);

        // settingsForm treba ostati isti (da ostanu user inputi)
        model.addAttribute("settingsForm", form);

        model.addAttribute("allCountries", countryRepository.findAll());

        if (form.getCountryId() != null) {
            model.addAttribute("cities", cityRepository.findByCountryId(form.getCountryId()));
        } else {
            model.addAttribute("cities", List.of());
        }

        model.addAttribute("allHobbies", hobbyRepository.findAll());
        model.addAttribute("tab", tab);
    }

}
