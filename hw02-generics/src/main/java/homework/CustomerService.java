package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customers = new TreeMap<>(new CustomerScoresComparator());

    public Map.Entry<Customer, String> getSmallest() {

        Map.Entry<Customer, String> smallest = customers.descendingMap().lastEntry();
        if (smallest == null) {
            return null;
        }
        return Map.entry(
                new Customer(
                        smallest.getKey().getId(),
                        smallest.getKey().getName(),
                        smallest.getKey().getScores()),
                smallest.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {

        Map.Entry<Customer, String> next = customers.higherEntry(customer);
        if (next == null) {
            return null;
        }

        return Map.entry(
                new Customer(
                        next.getKey().getId(),
                        next.getKey().getName(),
                        next.getKey().getScores()),
                next.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }

    private static class CustomerScoresComparator implements Comparator<Customer> {

        @Override
        public int compare(Customer c1, Customer c2) {
            var delta = c1.getScores() - c2.getScores();
            if (delta == 0) {
                return 0;
            }
            return delta > 0 ? 1 : -1;
        }
    }
}
