import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ProductSearch extends JFrame {
    private JTextField tfSearch;
    private JTextArea taResults;
    private RandomAccessFile file;
    private static final String filename = "products.txt";

    public ProductSearch() throws Exception {
        super("Random Product Search");
        file = new RandomAccessFile(filename, "r");
        initGUI();
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        tfSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        top.add(new JLabel("Search by Name:"));
        top.add(tfSearch);
        top.add(btnSearch);
        add(top, BorderLayout.NORTH);

        taResults = new JTextArea();
        taResults.setEditable(false);
        add(new JScrollPane(taResults), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> onSearch());

        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void onSearch() {
        ArrayList<String> matches = new ArrayList<>();
        try {
            String query = tfSearch.getText().trim().toLowerCase();
            taResults.setText("");
            ArrayList<String> text = new ArrayList<String>();
            file.seek(0);
            try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
                String line;
                while((line = reader.readLine()) != null) {
                    text.add(line);
                }
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
            }
            for(String line: text){
                if(line.contains(query)){
                    matches.add(line);
                }
            }
            for(int i = 0; i<matches.size();i++) {
                taResults.append(matches.get(i));
                taResults.append("\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        new ProductSearch();
    }
}