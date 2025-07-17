package ru.otus.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.model.domain.Client;
import ru.otus.model.domain.ClientForm;
import ru.otus.model.service.DBServiceClient;

@Controller
public class ClientController {

    private final DBServiceClient serviceClient;

    public ClientController(DBServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @GetMapping("/client/list")
    public String clientsListView(Model model) {
        List<Client> clients = serviceClient.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("form", new ClientForm());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(ClientForm form) {
        serviceClient.saveClient(form.convertToClient());
        return new RedirectView("/client/list", true);
    }
}
