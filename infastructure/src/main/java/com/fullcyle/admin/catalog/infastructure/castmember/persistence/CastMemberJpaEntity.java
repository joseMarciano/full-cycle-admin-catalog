package com.fullcyle.admin.catalog.infastructure.castmember.persistence;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "CastMember")
@Table(name = "CAST_MEMBERS")
public class CastMemberJpaEntity {

    @Id
    @Column(name = "ID")
    private String id;


    @Column(name = "NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private CastMemberType type;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public CastMemberJpaEntity() {
    }

    private CastMemberJpaEntity(
            final String id,
            final String name,
            final CastMemberType type,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CastMemberJpaEntity from(final CastMember aCastMember) {
        return new CastMemberJpaEntity(
                aCastMember.getId().getValue(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt(),
                aCastMember.getUpdatedAt()
        );
    }

    public CastMember toAggregate() {
        return CastMember.with(
                CastMemberID.from(getId()),
                getName(),
                getType(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public CastMemberType getType() {
        return type;
    }

    public void setType(final CastMemberType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
