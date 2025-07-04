package ru.otus.crm.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "address")
public class Address {

    @Id
    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    @Column(name = "id")
    @Expose
    private Long id;

    @Column(name = "street")
    @Expose
    private String street;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}
