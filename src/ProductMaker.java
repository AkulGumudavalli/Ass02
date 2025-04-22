import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class ProductMaker extends JFrame {
    private JTextField tfName, tfDesc, tfID, tfCost, tfCount;
    private RandomAccessFile file;
    private static String filename = "products.dat";
    private int count;
    private int iCount;
    private ArrayList<Product> allItems;
    public ProductMaker() throws Exception {
        super("Random Product Maker");
        file = new RandomAccessFile(filename, "rw");
        initGUI();
        count = -1;

        updateCount("");
        updateId(count);
        allItems = new ArrayList<Product>();
    }

    private void initGUI() {
        setLayout(new GridLayout(7, 2));
        tfName = new JTextField();
        tfDesc = new JTextField();
        tfID = new JTextField();
        tfCost = new JTextField();
        tfCount = new JTextField();
        tfCount.setEditable(false);
        tfID.setEditable(false);
        add(new JLabel("Name:")); add(tfName);
        add(new JLabel("Description:")); add(tfDesc);
        add(new JLabel("ID:")); add(tfID);
        add(new JLabel("Cost:")); add(tfCost);
        add(new JLabel("Record Count:")); add(tfCount);

        JButton btnAdd = new JButton("Add");
        JButton btnSave = new JButton("Save");
        JButton btnQuit = new JButton("Quit");
        add(btnAdd); add(btnSave);
        add(btnQuit); add(new JLabel());

        btnAdd.addActionListener(e -> onAdd());
        btnSave.addActionListener(e -> onSave());
        btnQuit.addActionListener(e -> onQuit());

        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void onAdd() {
        try {
            String name = tfName.getText().trim();
            String desc = tfDesc.getText().trim();
            String id = tfID.getText().trim();
            String cost = tfCost.getText().trim();
            if (name.isEmpty() || desc.isEmpty() || id.isEmpty() || cost.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            double c = Double.parseDouble(cost);
            Product p = new Product(name, desc, id, c);
            allItems.add(p);
            file.seek(file.length());
            file.writeChars(p.toFixedRecord());
            clearFields();
            updateCount("");
            updateId(count);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onSave() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        int res = chooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            try {
                File f = chooser.getSelectedFile();
                file.close();
                filename = f.getAbsolutePath();
                file = new RandomAccessFile(filename, "rw");
                for(Product p: allItems){
                    String total = p.toString();
                    file.write(total.getBytes());
                    file.write("\n".getBytes());
                }
                updateCount("clear");
                updateId(count);
                JOptionPane.showMessageDialog(this, "Now saving to: " + filename);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
    }

    private void onQuit() {
        try { file.close(); } catch (Exception ignored) {}
        dispose();
    }
    private void updateId(Integer count) throws Exception{
        String currInt = String.valueOf(count+1);
        String fullint = "";
        for(int i =0; i<6-currInt.length();i++){
            fullint = fullint + "0";
        }
        fullint += currInt;
        tfID.setText(fullint);
    }
    private void updateCount(String type) throws Exception {
        if(type.equalsIgnoreCase("clear")){
         count = -1;
        }
        count += 1;
        tfCount.setText(String.valueOf(count));
    }

    private void clearFields() {
        tfName.setText(""); tfDesc.setText(""); tfID.setText(""); tfCost.setText("");
    }

    public static void main(String[] args) throws Exception {
        new ProductMaker();
    }
}