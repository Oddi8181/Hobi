package gio.hobist.Service;

import gio.hobist.Dto.CommentDto;
import gio.hobist.Entity.Comment;
import gio.hobist.Entity.User;
import gio.hobist.Repository.CommentRepository;
import gio.hobist.Repository.PostRepository;
import gio.hobist.Repository.UserRepository;
import io.vavr.control.Try;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {//M.G: table is wrong in database, until fixed we can't work with comments

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public List<CommentDto> getComments(UUID postId) {

        var comments=commentRepository.findByPostId(postId);

        return comments.stream().map(c-> {

            var user=userRepository.findById(c.getUser().getId()).get();
            var userNameAndSurname=user.getName()+" "+user.getSurname();

           return new CommentDto(
               c.getId(),
               c.getPost().getId(),
               c.getUser().getId(),
               userNameAndSurname,
               c.getMessage(),
               null
        );

        }).toList();
    }

    public void updateComment(UUID commentId, CommentDto commentDto) {
        var comment = commentRepository.findById(commentId).orElse(null);


        comment.setMessage(commentDto.getMessage());
        comment.setLikeNumber(commentDto.getLikeNumber());

        commentRepository.save(comment);

    }

    public void createComment(CommentDto commentDto) {

        var user = Try.of(()->userRepository.findById(commentDto.getUserId()))
                .onFailure(ex -> {throw new EntityNotFoundException("User not found");}).get().get();
        var post= Try.of(()->postRepository.findByid(commentDto.getPostId()))
                .onFailure(ex -> {throw new EntityNotFoundException("post not found");}).get();

        var comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setMessage(commentDto.getMessage());

        commentRepository.save(comment);

    }

    @Transactional
    public void deleteComment(UUID postId) {
        commentRepository.deleteByPostId(postId);
    }
}