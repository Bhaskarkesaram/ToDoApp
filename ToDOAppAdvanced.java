import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ToDoAppAdvanced extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInputField;
    private JButton addButton, deleteButton, saveButton, loadButton, clearButton;

    private static final String FILE_NAME = "tasks.txt";

    public ToDoAppAdvanced() {
        setTitle("Advanced To-Do List");
        setSize(450, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskInputField = new JTextField();
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        clearButton = new JButton("Clear All");

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(taskInputField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 4));
        bottomPanel.add(deleteButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> {
            String task = taskInputField.getText().trim();
            if (!task.isEmpty()) {
                taskListModel.addElement("[ ] " + task);
                taskInputField.setText("");
            }
        });

        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) taskListModel.remove(index);
        });

        clearButton.addActionListener(e -> {
            taskListModel.clear();
        });

        saveButton.addActionListener(e -> saveTasksToFile());
        loadButton.addActionListener(e -> loadTasksFromFile());

        // Mark task as done on double-click
        taskList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = taskList.locationToIndex(e.getPoint());
                    String task = taskListModel.get(index);
                    if (task.startsWith("[ ]")) {
                        taskListModel.set(index, "[✓] " + task.substring(4));
                    } else if (task.startsWith("[✓]")) {
                        taskListModel.set(index, "[ ] " + task.substring(4));
                    }
                }
            }
        });
    }

    private void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                writer.println(taskListModel.get(i));
            }
            JOptionPane.showMessageDialog(this, "Tasks saved!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving tasks!");
        }
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            taskListModel.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                taskListModel.addElement(line);
            }
            JOptionPane.showMessageDialog(this, "Tasks loaded!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading tasks!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ToDoAppAdvanced().setVisible(true);
        });
    }
}
