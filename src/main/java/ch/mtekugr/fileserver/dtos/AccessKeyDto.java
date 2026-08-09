package ch.mtekugr.fileserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccessKeyDto {
    UUID id;
    UUID key;
}
