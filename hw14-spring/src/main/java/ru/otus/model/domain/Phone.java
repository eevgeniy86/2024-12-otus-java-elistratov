package ru.otus.model.domain;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "phone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    @Id
    @Column("id")
    Long id;

    @Nonnull
    String number;
}
