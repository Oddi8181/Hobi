package gio.hobist.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DbFileTransferController {


    @Value("${file.upload-dir}")//M.G: @value doesn't work like it should so i hardcoded uploads directory
    private final String uploadDir="C:/Users/Marko/Desktop/Java/Hobist/uploads";//M.G:change this on your pc!!

    @GetMapping("/media/{userId}/{imagePath}")//M.G: thymeleaf can access files only with url path so we put our local files first on site
    public ResponseEntity<Resource> GetImage(@PathVariable String imagePath,@PathVariable UUID userId) throws FileNotFoundException {

        Path path=null;
        if(imagePath.equals("noImage.jpg")){
            path= Paths.get(uploadDir).resolve("defaultImage.jpg");
        }
        else {
            path = Paths.get(uploadDir).resolve(userId + "/" + imagePath);
            if (!Files.exists(path)) {
                path= Paths.get(uploadDir).resolve("defaultImage.jpg");
            }
        }
        Resource resource = new FileSystemResource(path.toFile());


        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

}