package com.example.tvDispatcher.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "suranistar")
public class Suranis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "VARCHAR(500)")
    private String description;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "meeting_time", nullable = false)
    private LocalDateTime meetingTime;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "suranis")
    @ToString.Exclude
    private List<UsersSuranistar> employees;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Suranis suranis = (Suranis) o;
        return getId() != null && Objects.equals(getId(), suranis.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
