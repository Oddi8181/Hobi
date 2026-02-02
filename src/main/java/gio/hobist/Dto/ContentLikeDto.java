package gio.hobist.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ContentLikeDto {
    private UUID id;
    private UUID postId;
    private UUID commentId;
    private UUID userId;
}
