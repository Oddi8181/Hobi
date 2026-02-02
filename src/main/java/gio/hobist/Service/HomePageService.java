package gio.hobist.Service;

import gio.hobist.Dto.PostDto;
import gio.hobist.Enum.Status;
import gio.hobist.Repository.ContentLikeRepository;
import gio.hobist.Repository.PostRepository;
import gio.hobist.Repository.FriendshipRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final PostRepository postRepository;
    private final ContentLikeRepository contentLikeRepository;
    private final FriendshipRepository friendshipRepository;

   public List<PostDto> findAllPosts(UUID userId){
       var postList=postRepository.findAllByIdUser(userId);

      return postList.stream().map( post ->{

          var contentLike=contentLikeRepository.findByPostId(post.getId());

          var likeNumber=contentLike.size();

          return new PostDto(
                  post.getId(),
               post.getIdUser(),
               post.getMessage(),
               post.getImage(),
               likeNumber,
               post.getCreatedAt()
          );

      }).toList();
   }
    public List<PostDto> findFriendPosts(UUID userId) {
        var friendIds = friendshipRepository.findFriendIds(userId, Status.accepted);
        if (friendIds == null || friendIds.isEmpty()) {
            return List.of();
        }
        var postList = postRepository.findAllByIdUserInOrderByCreatedAtDesc(friendIds);

        return postList.stream().map(post -> {
            var contentLike = contentLikeRepository.findByPostId(post.getId());
            var likeNumber = contentLike != null ? contentLike.size() : 0;
            return new PostDto(
                    post.getId(),
                    post.getIdUser(),
                    post.getMessage(),
                    post.getImage(),
                    likeNumber,
                    post.getCreatedAt()
            );
        }).toList();
    }
}
