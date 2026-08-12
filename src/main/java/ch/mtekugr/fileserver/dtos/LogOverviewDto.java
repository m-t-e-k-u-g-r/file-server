package ch.mtekugr.fileserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOverviewDto {
    private Long accessTime;
    private String fileName;
    private String keyDescription;
    private Boolean authorized;
}
