package org.animector.animectorbe.image;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "image_data")
public class ImageData {
    @Id
    private String fileName;
    private long fileSize;
    private String content;
    private String message;
}
