package org.animector.animectorbe.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("api/")
public class ImageController {
//    private final Path uploadDir;
    ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
//        this.uploadDir = Paths.get(uploadDir);
//        try {
//            Files.createDirectories(this.uploadDir); // ensure exists at startup
//        } catch (Exception e) {
//            throw new RuntimeException("Cannot create upload dir: " + this.uploadDir, e);
//        }
    }
//
//    @PostMapping("buttonTest")
//    public String test(@RequestBody Message body) {
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + body.message);
//        return "All ok!";
//    }

    @PostMapping(path="upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
    @RequestParam("message") String message) {
        return imageService.saveImage(file, message);
    }

    @GetMapping("/images")
    public List<ImageInfo> listImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestHeader(value = "Host", required = false) String host,
            @RequestHeader(value = "X-Forwarded-Proto", required = false) String xfProto,
            @RequestHeader(value = "X-Forwarded-Host", required = false) String xfHost
    ) throws Exception{
        return imageService.listImages(page, size, host, xfProto, xfHost);
    }

}
