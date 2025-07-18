package ru.otus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import ru.otus.model.domain.Client;
import ru.otus.model.service.DBServiceClient;

@RestController
@RequestMapping("${rest.api.prefix}${rest.api.version}")
public class ClientRestController {

    private final DBServiceClient dbServiceClient;

    public ClientRestController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Operation(summary = "Get client by id")
    @GetMapping("/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return dbServiceClient.getClient(id).orElse(null);
    }

    @Operation(summary = "Get client by name")
    @GetMapping("/client")
    public List<Client> getClientByName(
            @Parameter(description = "Имя клиента", required = true) @RequestParam(name = "name") String name) {
        return dbServiceClient.findByName(name);
    }

    @Operation(summary = "Get all clients")
    @GetMapping("/client/all")
    public List<Client> getClients() {
        return dbServiceClient.findAll();
    }

    @Operation(summary = "Create/update client")
    @PostMapping("/client")
    public Client saveClient(@RequestBody Client client) {
        return dbServiceClient.saveClient(client);
    }
}
