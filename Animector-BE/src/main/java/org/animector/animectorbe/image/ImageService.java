package org.animector.animectorbe.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageService {
    private final Path uploadDir;
    private final ImageRepo imageRepo;

    public ImageService(@Value("${app.upload.dir}") String uploadDir, ImageRepo imageRepo) throws IOException {
        this.uploadDir = Paths.get(uploadDir);
        this.imageRepo = imageRepo;
        try {
            Files.createDirectories(this.uploadDir); // ensure exists at startup
        } catch (Exception e) {
            throw new RuntimeException("Cannot create upload dir: " + this.uploadDir, e);
        }
    }

    public ResponseEntity<String> saveImage(MultipartFile file, String message) {
        try {
            String filename = generateFilename(file);
            Path dest = Paths.get(uploadDir.toUri()).resolve(filename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }
            ImageData imageData = ImageData.builder()
                    .fileName(filename)
                    .fileSize(file.getSize())
                    .content("something something")
                    .message(message).build();
            imageRepo.save(imageData);
            return ResponseEntity.ok("Saved to: " + dest.toString() + " | message=" + message);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to save file: " + e.getMessage());
        }
    }

    private static String generateFilename(MultipartFile file) {
        String original = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot);
        String filename = Instant.now().toString().replace(":", "-")
                + "-" + UUID.randomUUID() + ext;
        return filename;
    }

    public List<ImageInfo> listImages(int page, int size, String host, String xfProto, String xfHost) throws IOException {
        if (page < 0) page = 0;
        if (size < 1) size = 1;
        if (size > 500) size = 500;

        String baseHost = (xfHost != null && !xfHost.isBlank()) ? xfHost : (host != null ? host : "localhost:8080");
        String scheme = (xfProto != null && !xfProto.isBlank()) ? xfProto : "http";
        String baseUrl = scheme + "://" + baseHost;

        try (Stream<Path> paths = Files.list(uploadDir)) {
            List<Path> files = paths
                    .filter(Files::isRegularFile)
                    .sorted((a, b) -> {
                        try {
                            return Files.getLastModifiedTime(b).compareTo(Files.getLastModifiedTime(a));
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .toList();

            int from = Math.min(page * size, files.size());
            int to = Math.min(from + size, files.size());

            return files.subList(from, to).stream().map(p -> {
                try {
                    String filename = p.getFileName().toString();
                    long fileSize = Files.size(p);
                    Instant lm = Files.getLastModifiedTime(p).toInstant();
                    // URL that streams the image via our controller:
                    String url = baseUrl + "/api/images/" + filename;
                    return new ImageInfo(filename, fileSize, lm, url);
                } catch (Exception e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }
}
