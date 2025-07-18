package ru.otus.model.domain;

import java.util.HashSet;

public record ClientForm(String name, String street, String phonesString) {

    public Client convertToClient() {
        HashSet<Phone> phones = new HashSet<>();
        if (phonesString != null && !phonesString.equals("")) {
            for (String phone : phonesString.split(",")) {
                phones.add(new Phone(null, phone.trim()));
            }
        }
        return new Client(null, name, (street == null || street.isEmpty()) ? null : new Address(null, street), phones);
    }
}
