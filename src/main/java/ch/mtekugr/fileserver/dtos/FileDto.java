package ch.mtekugr.fileserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class FileDto {
    UUID id;
    String originalFilename;
    Long size;
    Long createdAt;
}
