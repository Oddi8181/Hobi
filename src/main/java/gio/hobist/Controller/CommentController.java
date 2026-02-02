package gio.hobist.Controller;

import gio.hobist.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public String comment(Model model, @PathVariable UUID postId) {

        var comments=commentService.getComments(postId);

        model.addAttribute("comments", comments);

        return "common/commentSection.html";
    }


}
