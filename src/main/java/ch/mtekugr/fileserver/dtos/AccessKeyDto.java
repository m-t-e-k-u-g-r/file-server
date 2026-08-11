package ch.mtekugr.fileserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccessKeyDto {
    UUID id;
    String fileName;
    String description;
    Long createdAt;
    AccessKeyStatus status;
}
