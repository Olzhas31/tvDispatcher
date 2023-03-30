package com.example.tvDispatcher.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_suranistar", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "suranis_id"})
})
public class UsersSuranistar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "suranis_id")
    private Suranis suranis;

    @Column(name = "employee_approved")
    private Boolean employeeApproved;

    @Column(name = "manager_approved")
    private Boolean managerApproved;

    @Column(name = "reason_not_approval")
    private String reasonNotApproval;

    @Column(name = "employee_approve_time")
    private LocalDateTime employeeApproveTime;

    @Column(name = "manager_approve_time")
    private LocalDateTime managerApproveTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsersSuranistar that = (UsersSuranistar) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
