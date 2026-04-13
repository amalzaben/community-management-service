package com.discuessit.communityManagemnet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "privilege")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="type",nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ModeratorPrivilegeType type;

    @Column(name = "description")
    private String description;
}
