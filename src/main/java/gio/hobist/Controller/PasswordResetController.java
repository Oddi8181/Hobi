package gio.hobist.Controller;


import gio.hobist.Dto.ForgotPasswordDto;
import gio.hobist.Dto.ResetPasswordDto;
import gio.hobist.Service.PasswordResetService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {
    private final PasswordResetService passwordResetService;
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        model.addAttribute("form", new ForgotPasswordDto());
        return "common/forgot-password.html";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@ModelAttribute("form") ForgotPasswordDto form,
                                 HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        passwordResetService.requestReset(form.getEmail(), baseUrl);
        return "redirect:/forgot-password?sent=true";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        ResetPasswordDto form = new ResetPasswordDto();
        form.setToken(token);
        model.addAttribute("form", form);
        return "common/reset-password.html";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("form")ResetPasswordDto form,
                                BindingResult binding,
                                Model model) {
        passwordResetService.resetPassword(form.getToken(), form.getNewPassword(), form.getConfirmPassword(), binding);
        if (binding.hasErrors()) {
            return "common/reset-password.html";
        }

        return "redirect:/login?resetSuccess=true";
    }
}
