package ch.mtekugr.fileserver.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "config")
public class Config {
    @Id
    @Column(name = "key", nullable = false, length = Integer.MAX_VALUE)
    private String key;

    @Column(name = "value", nullable = false, length = Integer.MAX_VALUE)
    private String value;


}