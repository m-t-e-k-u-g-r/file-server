package ch.mtekugr.fileserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "storage_key", nullable = false)
    private UUID storageKey;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;


}