package gio.hobist.Service;

import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DbFileTransferService {

    @Value("${file.upload-dir}")//M.G:doesn't work like it should so i hardcoded uploads directory
    private final String uploadDir="C:/Users/Marko/Desktop/Java/Hobist/uploads";//M.G:change this on your pc!!


   public void saveFile(UUID userId, MultipartFile file) throws IOException {

       var fileName=Paths.get(file.getOriginalFilename()).getFileName().toString();

       var userDir= Paths.get(uploadDir,userId.toString());//M.G: works for new or already existing dir
       Files.createDirectories(userDir);

       var targetFile=userDir.resolve(fileName);
       file.transferTo(targetFile.toFile());
   }
}
