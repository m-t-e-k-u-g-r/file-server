package ch.mtekugr.fileserver.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "log_entry")
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "access_key_id")
    private UUID accessKeyId;

    @Column(name = "file_id")
    private UUID fileId;

    @Column(name = "matches", nullable = false)
    private Boolean matches;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "revoked", nullable = false)
    private Boolean revoked;

    @Column(name = "authorized", insertable = false)
    private Boolean authorized;

    @Column(name = "received", nullable = false)
    private Instant received;

    public LogEntry(UUID accessKeyId, UUID fileId, boolean matches, boolean revoked, boolean expired) {
        this.accessKeyId = accessKeyId;
        this.fileId = fileId;
        this.matches = matches;
        this.revoked = revoked;
        this.expired = expired;
    }

    @PrePersist
    protected void onCreate() {
        this.received = Instant.now();
    }
}