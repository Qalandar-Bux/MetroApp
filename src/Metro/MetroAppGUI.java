package Metro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MetroAppGUI extends JFrame {
    private Graph_M metroGraph;
    private JTextArea mapTextArea;

    public MetroAppGUI() {
        mapTextArea = new JTextArea();
        mapTextArea.setEditable(false);

        metroGraph = new Graph_M();
        Graph_M.Create_Metro_Map(metroGraph);

        setTitle("Metro App");
        setSize(500, 500); // Set an initial size

        // Center the frame on the screen
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Apply the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Load the image
            Image backgroundImage = new ImageIcon("C:/Users/DELL/Desktop/FinalPro/FinalPro/src/Metro/imag.jpg").getImage();
            JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
            backgroundLabel.setLayout(new BorderLayout());

            // Create a JPanel with GridBagLayout and set it to be transparent
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false); // Make the panel transparent
            backgroundLabel.add(panel, BorderLayout.CENTER);

            setContentPane(backgroundLabel);
            addButtons(panel);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading background image: " + e.getMessage());
        }
    }

    private void addButtons(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 15, 15, 15); // Add spacing

        JButton listStationsButton = new JButton("List All Stations");
        JButton showMapButton = new JButton("Show Metro Map");
        JButton shortestDistanceButton = new JButton("Shortest Distance");
        JButton shortestTimeButton = new JButton("Shortest Time");
        JButton shortestPathDistanceButton = new JButton("Shortest Path (Distance)");
        JButton shortestPathTimeButton = new JButton("Shortest Path (Time)");
        JButton exitButton = new JButton("Exit");

        // Set preferred size and background color for each button
        Dimension buttonSize = new Dimension(180, 35); // Adjust the width and height as needed
        Color buttonColor = new Color(106, 90, 205); // You can set your desired color using RGB values

        listStationsButton.setPreferredSize(buttonSize);
        listStationsButton.setBackground(buttonColor);

        showMapButton.setPreferredSize(buttonSize);
        showMapButton.setBackground(buttonColor);

        shortestDistanceButton.setPreferredSize(buttonSize);
        shortestDistanceButton.setBackground(buttonColor);

        shortestTimeButton.setPreferredSize(buttonSize);
        shortestTimeButton.setBackground(buttonColor);

        shortestPathDistanceButton.setPreferredSize(buttonSize);
        shortestPathDistanceButton.setBackground(buttonColor);

        shortestPathTimeButton.setPreferredSize(buttonSize);
        shortestPathTimeButton.setBackground(buttonColor);

        exitButton.setPreferredSize(buttonSize);
        exitButton.setBackground(buttonColor);
        addActionButton(panel, listStationsButton, gbc);
        addActionButton(panel, showMapButton, gbc);
        addActionButton(panel, shortestDistanceButton, gbc);
        addActionButton(panel, shortestTimeButton, gbc);
        addActionButton(panel, shortestPathDistanceButton, gbc);
        addActionButton(panel, shortestPathTimeButton, gbc);
        addActionButton(panel, exitButton, gbc);

        pack(); // Adjust the frame size
    }

    private void addActionButton(JPanel panel, JButton button, GridBagConstraints gbc) {
        gbc.gridx = 0;
        panel.add(Box.createVerticalStrut(40), gbc); // Add vertical space
        gbc.gridy++;
        panel.add(button, gbc);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(createActionListener(button.getText()));
    }

    private ActionListener createActionListener(String buttonText) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(buttonText);
            }
        };
    }

    private void handleButtonClick(String buttonText) {
        switch (buttonText) {
            case "List All Stations":
                ArrayList<String> stationList = new ArrayList<>(metroGraph.vtces.keySet());
                String stationString = String.join("\n", stationList);
                JOptionPane.showMessageDialog(null, "List of Stations:\n" + stationString);
                break;
            case "Show Metro Map":
                mapTextArea.setText("");
                metroGraph.display_Map(mapTextArea);
                JFrame mapFrame = new JFrame("Metro Map");
                mapFrame.setSize(400, 300);
                mapFrame.add(new JScrollPane(mapTextArea));
                mapFrame.setVisible(true);
                break;
            case "Shortest Distance":
                handleShortestDistanceButtonClick();
                break;
            case "Shortest Time":
                handleShortestTimeButtonClick();
                break;
            case "Shortest Path (Distance)":
                handleShortestPathDistanceButtonClick();
                break;
            case "Shortest Path (Time)":
                handleShortestPathTimeButtonClick();
                break;
            case "Exit":
                System.exit(0);
                break;
            default:
                // Handle unknown button
                break;
        }
    }

    private void handleShortestDistanceButtonClick() {
        JComboBox<String> stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.add(new JLabel("Select source station:"));
        inputPanel.add(stationComboBox);

        int sourceOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Source Station", JOptionPane.OK_CANCEL_OPTION);
        if (sourceOption == JOptionPane.OK_OPTION) {
            String sourceStation = (String) stationComboBox.getSelectedItem();

            stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
            inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Select destination station:"));
            inputPanel.add(stationComboBox);

            int destOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Destination Station", JOptionPane.OK_CANCEL_OPTION);
            if (destOption == JOptionPane.OK_OPTION) {
                String destinationStation = (String) stationComboBox.getSelectedItem();

                int distance = metroGraph.dijkstra(sourceStation, destinationStation, false);
                JOptionPane.showMessageDialog(null, "Shortest Distance from " + sourceStation + " to " +
                        destinationStation + " is " + distance + " KM");
            }
        }
    }
    private void handleShortestTimeButtonClick() {
        JComboBox<String> stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.add(new JLabel("Select source station:"));
        inputPanel.add(stationComboBox);

        int sourceOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Source Station", JOptionPane.OK_CANCEL_OPTION);
        if (sourceOption == JOptionPane.OK_OPTION) {
            String sourceStation = (String) stationComboBox.getSelectedItem();

            stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
            inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Select destination station:"));
            inputPanel.add(stationComboBox);

            int destOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Destination Station", JOptionPane.OK_CANCEL_OPTION);
            if (destOption == JOptionPane.OK_OPTION) {
                String destinationStation = (String) stationComboBox.getSelectedItem();

                int time = metroGraph.dijkstra(sourceStation, destinationStation, true);
                JOptionPane.showMessageDialog(null, "Shortest Time from " + sourceStation + " to " +
                        destinationStation + " is " + time / 60 + " minutes");
            }
        }
    }

    private void handleShortestPathDistanceButtonClick() {
        JComboBox<String> stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.add(new JLabel("Select source station:"));
        inputPanel.add(stationComboBox);

        int sourceOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Source Station", JOptionPane.OK_CANCEL_OPTION);
        if (sourceOption == JOptionPane.OK_OPTION) {
            String sourceStation = (String) stationComboBox.getSelectedItem();

            stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
            inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Select destination station:"));
            inputPanel.add(stationComboBox);

            int destOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Destination Station", JOptionPane.OK_CANCEL_OPTION);
            if (destOption == JOptionPane.OK_OPTION) {
                String destinationStation = (String) stationComboBox.getSelectedItem();

                ArrayList<String> pathList = metroGraph.get_Interchanges(metroGraph.Get_Minimum_Distance(sourceStation, destinationStation));
                JOptionPane.showMessageDialog(null, formatPathResult1(pathList, "Distance"));
            }
        }
    }

    private String formatPathResult1(ArrayList<String> pathList, String type) {
        StringBuilder result = new StringBuilder();
        result.append("Source Station: ").append(pathList.get(0)).append("\n");
        result.append("Destination Station: ").append(pathList.get(pathList.size() - 1)).append("\n");
        result.append("Distance: ").append(pathList.get(pathList.size() - 2)).append("\n");
        result.append("Number of Interchanges: ").append(pathList.get(pathList.size() - 3)).append("\n");
        result.append("Path:\n");

        for (int i = 1; i <= pathList.size() - 4; i++) {
            result.append(pathList.get(i)).append("\n");
        }

        return result.toString();
    }

    private void handleShortestPathTimeButtonClick() {
        JComboBox<String> stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.add(new JLabel("Select source station:"));
        inputPanel.add(stationComboBox);

        int sourceOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Source Station", JOptionPane.OK_CANCEL_OPTION);
        if (sourceOption == JOptionPane.OK_OPTION) {
            String sourceStation = (String) stationComboBox.getSelectedItem();

            stationComboBox = new JComboBox<>(metroGraph.vtces.keySet().toArray(new String[0]));
            inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Select destination station:"));
            inputPanel.add(stationComboBox);

            int destOption = JOptionPane.showConfirmDialog(null, inputPanel, "Select Destination Station", JOptionPane.OK_CANCEL_OPTION);
            if (destOption == JOptionPane.OK_OPTION) {
                String destinationStation = (String) stationComboBox.getSelectedItem();

                ArrayList<String> pathList = metroGraph.get_Interchanges(metroGraph.Get_Minimum_Time(sourceStation, destinationStation));
                JOptionPane.showMessageDialog(null, formatPathResult1(pathList, "Time"));
            }
        }
    }


    private String formatPathResult(ArrayList<String> pathList, String type) {
        StringBuilder result = new StringBuilder();
        result.append("Source Station: ").append(pathList.get(0)).append("\n");
        result.append("Destination Station: ").append(pathList.get(pathList.size() - 1)).append("\n");
        result.append(type).append(": ").append(pathList.get(pathList.size() - 2)).append("\n");
        result.append("Number of Interchanges: ").append(pathList.get(pathList.size() - 3)).append("\n");
        result.append("Path:\n");

        for (int i = 1; i <= pathList.size() - 4; i++) {
            result.append(pathList.get(i)).append("\n");
        }

        return result.toString();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MetroAppGUI metroApp = new MetroAppGUI();
                metroApp.setLocationRelativeTo(null); // Center the frame
                // metroApp.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full-screen
                metroApp.setVisible(true);
            }
        });
    }
}
