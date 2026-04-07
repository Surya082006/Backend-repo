package com.klu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "educators")
@Data
@EqualsAndHashCode(callSuper = true)
public class Educator extends User {
    private String qualification;
    private String specialization;
}
