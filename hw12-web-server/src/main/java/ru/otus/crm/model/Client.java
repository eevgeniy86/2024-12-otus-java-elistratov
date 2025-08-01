package ru.otus.crm.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "client")
@Data
public final class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    @Expose
    private Long id;

    @Column(name = "name")
    @Expose
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @Expose
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Expose
    private List<Phone> phones = new ArrayList<>();

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public <E> Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = new ArrayList<>();
        if (phones != null) {
            for (Phone p : phones) {
                Phone cloned = new Phone(p.getId(), p.getNumber());
                cloned.setClient(this);
                this.phones.add(cloned);
            }
        }
    }

    private void addPhone(Phone phone) {
        phones.add(phone);
        phone.setClient(this);
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        List<Phone> phonesCloned = new ArrayList<>();

        Client clientCloned = new Client(
                this.id,
                this.name,
                this.address == null ? null : new Address(this.address.getId(), this.address.getStreet()),
                phonesCloned);
        if (this.phones != null) {
            for (Phone p : this.phones) {
                clientCloned.addPhone(new Phone(p.getId(), p.getNumber()));
            }
        }
        return clientCloned;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
