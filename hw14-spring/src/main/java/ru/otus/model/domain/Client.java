package ru.otus.model.domain;

import jakarta.annotation.Nonnull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @Column("id")
    Long id;

    @Nonnull
    String name;

    @MappedCollection(idColumn = "client_id")
    Address address;

    @MappedCollection(idColumn = "client_id")
    Set<Phone> phones;

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
