package gio.hobist.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Setter
@RequiredArgsConstructor
public class CommentDto {
    private UUID id;
    private UUID postId;
    private UUID userId;
    private String userNameAndSurname;
    private String message;
    private Integer likeNumber;

}