package gio.hobist.Dto;

import gio.hobist.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class FriendshipDto {
    private UUID id;
    private UUID user1Id;
    private UUID user2Id;
    private Status status;
    private Timestamp dateOfBefriending;


}