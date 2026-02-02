package gio.hobist.Dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDto {

    private UUID id;
    private UUID userId;
    private String message;
    private String rawImage;
    private Integer likeNumber;
    private Timestamp createdAt;
}
