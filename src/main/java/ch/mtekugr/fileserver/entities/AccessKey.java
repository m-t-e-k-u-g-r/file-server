package ch.mtekugr.fileserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "access_key")
public class AccessKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @Column(name = "key_hash", nullable = false, length = Integer.MAX_VALUE)
    private String keyHash;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false, insertable = false, updatable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;


}