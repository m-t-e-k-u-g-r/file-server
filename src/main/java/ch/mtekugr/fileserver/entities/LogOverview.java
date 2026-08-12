package ch.mtekugr.fileserver.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.Instant;

@Getter
@Entity
@Immutable
@Table(name = "log_overview")
public class LogOverview {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "access_time")
    private Instant accessTime;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "key_description", length = Integer.MAX_VALUE)
    private String keyDescription;

    @Column(name = "authorized")
    private Boolean authorized;

    @Column(name = "key_valid")
    private Boolean keyValid;

    @Column(name = "key_expired")
    private Boolean keyExpired;

    @Column(name = "key_revoked")
    private Boolean keyRevoked;


}