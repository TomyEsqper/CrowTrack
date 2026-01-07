package com.tuplataforma.core.infrastructure.persistence.identity;

import com.tuplataforma.core.domain.identity.Email;
import com.tuplataforma.core.domain.identity.PasswordHash;
import com.tuplataforma.core.domain.identity.Role;
import com.tuplataforma.core.domain.identity.User;
import com.tuplataforma.core.domain.identity.UserId;
import com.tuplataforma.core.infrastructure.persistence.shared.TenantAwareEntity;
import com.tuplataforma.core.infrastructure.persistence.shared.TenantEntityListener;
import com.tuplataforma.core.shared.tenant.TenantId;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@EntityListeners(TenantEntityListener.class)
public class UserEntity extends TenantAwareEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean active;

    // Default constructor for JPA
    public UserEntity() {}

    // Mapper constructor
    public UserEntity(User user) {
        this.id = user.getId().getValue();
        this.setTenantId(user.getTenantId().getValue());
        this.email = user.getEmail().getValue();
        this.passwordHash = user.getPasswordHash().getValue();
        this.roles = new HashSet<>(user.getRoles());
        this.active = user.isActive();
    }

    public User toDomain() {
        return User.restore(
            new UserId(this.id),
            TenantId.fromString(this.getTenantId()),
            new Email(this.email),
            new PasswordHash(this.passwordHash),
            this.roles,
            this.active
        );
    }

    public UUID getId() {
        return id;
    }
}
