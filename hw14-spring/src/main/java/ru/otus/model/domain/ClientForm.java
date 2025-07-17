package ru.otus.model.domain;

import java.util.HashSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientForm {
    private String name;
    private String street;
    private String phonesString;

    public Client convertToClient() {
        HashSet<Phone> phones = new HashSet<>();
        if (phonesString != null && !phonesString.equals("")) {
            for (String phone : phonesString.split(",")) {
                phones.add(new Phone(null, phone.trim()));
            }
        }
        return new Client(null, name, (street == null || street.equals("")) ? null : new Address(null, street), phones);
    }
}
