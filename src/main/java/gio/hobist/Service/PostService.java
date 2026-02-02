package gio.hobist.Service;

import gio.hobist.Dto.ContentLikeDto;
import gio.hobist.Dto.PostDto;
import gio.hobist.Entity.ContentLike;
import gio.hobist.Entity.Post;
import gio.hobist.Repository.CommentRepository;
import gio.hobist.Repository.ContentLikeRepository;
import gio.hobist.Repository.PostRepository;
import gio.hobist.Repository.UserRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final DbFileTransferService dbFileTransferService;
    private final PostRepository postRepository;
    private final ContentLikeRepository contentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public PostDto getPost(UUID postId){
       var post=postRepository.findByid(postId);

       var contentLike=contentLikeRepository.findByPostId(postId);

       var likeNumber=contentLike.size();

        var image=(post.getImage().isBlank())?
               null:
               post.getImage();

        return new PostDto(
               post.getId(),
               post.getIdUser(),
               post.getMessage(),
               image,
               likeNumber,
               post.getCreatedAt()
       );
    }

    @Transactional
    public void createLike(ContentLikeDto contentLikeDto){

        var post=postRepository.findByid(contentLikeDto.getPostId());

        var user=userRepository.findById(contentLikeDto.getUserId()).get();

        var contentLike=new ContentLike(
                null,
                post,
                null,
                user
        );


        if(!isLiked(post.getId(),user.getId(),null)){
            contentLikeRepository.save(contentLike);
        }
        else {
            contentLikeRepository.deleteByPostIdAndUserIdAndCommentId(post.getId(),user.getId(),null);
        }
    }

    @Transactional
    public void deleteLike(UUID postId){
        contentLikeRepository.deleteByPostId(postId);
    }

    public boolean isLiked(UUID postId, UUID userId, UUID commentId){
        var isLiked=contentLikeRepository.findByPostIdAndUserIdAndCommentId(
                postId,
                userId,
                commentId);

        return !isLiked.isEmpty();//M.G: method is called isLiked so by default method should return true
    }

    public void createPost(PostDto postDto, MultipartFile file){
        String fileName=file.getOriginalFilename();

        try {
            dbFileTransferService.saveFile(postDto.getUserId(), file);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        var post=new Post();
        post.setIdUser(postDto.getUserId());
        post.setMessage(postDto.getMessage());
        post.setImage(fileName);

        postRepository.save(post);

    }

    public void deletePost(UUID postId){
        postRepository.deleteById(postId);
    }

}
