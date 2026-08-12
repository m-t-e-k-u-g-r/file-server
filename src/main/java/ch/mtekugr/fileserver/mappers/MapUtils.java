package ch.mtekugr.fileserver.mappers;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MapUtils {
    @Named("mapInstant")
    public Long mapInstant(Instant instant) {
        if (instant == null) return null;
        return instant.toEpochMilli();
    }
}
