package ru.otus.model.repository;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.model.domain.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    List<Client> findByName(String name);

    List<Client> findByOrderById();
}
