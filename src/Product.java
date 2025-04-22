import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int NAME_LEN = 35;
    private static final int DESC_LEN = 75;
    private static final int ID_LEN   = 6;

    private String name;
    private String description;
    private String id;
    private double cost;

    public Product(String name, String description, String id, double cost) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.cost = cost;
    }

    // Pad to fixed length
    private String pad(String s, int length) {
        if (s.length() > length) return s.substring(0, length);
        return String.format("%-" + length + "s", s);
    }

    // Return record ready for RandomAccessFile write
    public String toFixedRecord() {
        return pad(name, NAME_LEN)
                + pad(description, DESC_LEN)
                + pad(id, ID_LEN)
                + String.format("%8.2f", cost);
    }

    // Parse from a fixed-length record string
    public static Product fromFixedRecord(String rec) {
        String n  = rec.substring(0, NAME_LEN).trim();
        String d  = rec.substring(NAME_LEN, NAME_LEN+DESC_LEN).trim();
        String i  = rec.substring(NAME_LEN+DESC_LEN, NAME_LEN+DESC_LEN+ID_LEN).trim();
        double c = Double.parseDouble(rec.substring(NAME_LEN+DESC_LEN+ID_LEN).trim());
        return new Product(n, d, i, c);
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Desc: %s, ID: %s, Cost: %.2f", name, description, id, cost);
    }
}