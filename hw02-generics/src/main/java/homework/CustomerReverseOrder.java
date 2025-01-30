package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {

    private final LinkedList<Customer> customers = new LinkedList<>();

    public void add(Customer customer) {
        customers.push(customer);
    }

    public Customer take() {
        return customers.pop();
    }
}
