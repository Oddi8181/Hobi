package gio.hobist.Service;

import gio.hobist.Dto.FriendshipDto;
import gio.hobist.Entity.Friendship;
import gio.hobist.Repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    public FriendshipDto getCurrentFriendship(UUID friendshipId) {
        var friendship = friendshipRepository.findById(friendshipId).orElse(null);

        if (friendship == null) {
            return null;
        }

        return new FriendshipDto(
            friendship.getId(),
            friendship.getUser1().getId(),
            friendship.getUser2().getId(),
            friendship.getStatus(),
            friendship.getDateOfBefriending()
        );
    }

    public FriendshipDto updateFriendship(UUID friendshipId, FriendshipDto friendshipDto) {
        var friendship = friendshipRepository.findById(friendshipId).orElse(null);

        if (friendship == null) {
            return null;
        }

        friendship.setStatus(friendshipDto.getStatus());
        friendship.setDateOfBefriending(friendshipDto.getDateOfBefriending());

        friendshipRepository.save(friendship);

        return new FriendshipDto(
            friendship.getId(),
            friendship.getUser1().getId(),
            friendship.getUser2().getId(),
            friendship.getStatus(),
            friendship.getDateOfBefriending()
        );
    }

    public FriendshipDto createFriendship(FriendshipDto friendshipDto) {
        var friendship = new Friendship();
        friendship.setStatus(friendshipDto.getStatus());
        friendship.setDateOfBefriending(friendshipDto.getDateOfBefriending());

        friendshipRepository.save(friendship);

        return new FriendshipDto(
            friendship.getId(),
            friendship.getUser1().getId(),
            friendship.getUser2().getId(),
            friendship.getStatus(),
            friendship.getDateOfBefriending()
        );
    }

    public void deleteFriendship(UUID friendshipId) {
        friendshipRepository.deleteById(friendshipId);
    }
}