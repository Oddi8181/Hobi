package gio.hobist.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID friendshipId;
    private UUID userId;
    private String message;
    private String fileName;
    private String filePath;
    private String timeSent;


}