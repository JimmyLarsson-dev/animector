package org.animector.animectorbe.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<ImageData, String> {
    Optional<ImageData> findByFileName(String imageName);
}
