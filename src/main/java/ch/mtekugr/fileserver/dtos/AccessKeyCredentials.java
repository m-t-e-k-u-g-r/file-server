package ch.mtekugr.fileserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccessKeyCredentials {
    UUID id;
    UUID key;
}
