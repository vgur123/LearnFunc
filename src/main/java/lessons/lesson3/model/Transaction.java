package lessons.lesson3.model;

import lombok.Data;

@Data
public class Transaction {
    private String id;
    private String description;

    public Transaction(String id, String description) {
        this.id = id;
        this.description = description;
    }
}
