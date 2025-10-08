package org.animector.animectorbe.image;

import java.time.Instant;

public record ImageInfo(
     String filename,
     long size,
     Instant lastModified,
     String url
    )
{}
