package ru.otus.webserver.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private final transient DBServiceClient serviceClient;
    private final transient Gson gson;

    public ClientsApiServlet(DBServiceClient serviceClient, Gson gson) {
        this.serviceClient = serviceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Client> clients = serviceClient.findAll();
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = "";
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        }
        Client saved = serviceClient.saveClient(gson.fromJson(json, Client.class));
        Long id = saved.getId();
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(id));
    }
}
