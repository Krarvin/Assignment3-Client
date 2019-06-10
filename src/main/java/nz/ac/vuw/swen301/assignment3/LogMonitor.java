package nz.ac.vuw.swen301.assignment3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LogMonitor extends JFrame {
    public LogMonitor() {
        final JPanel jpanel = new JPanel(new GridBagLayout());
        jpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(jpanel);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,5,0,2);
        c.gridx = 0;
        c.gridy = 0;
        final JLabel minlevel = new JLabel("min level:");
        jpanel.add(minlevel, c);
        String[] levels = {"TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL"};
        final JComboBox levelList = new JComboBox(levels);
        levelList.setSelectedItem("enter level");
        levelList.setBounds(50,50,90,20);
        c.gridx = 1;
        c.gridy = 0;
        jpanel.add(levelList, c);
        final JLabel limit = new JLabel("limit:");
        c.gridx = 2;
        c.gridy = 0;
        jpanel.add(limit,c);
        final JTextField limitfield = new JTextField("enter 0-50");
        limitfield.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                limitfield.setText("");
            }

            public void focusLost(FocusEvent e) {
                // nothing
            }
        });
        c.gridx = 3;
        c.gridy = 0;
        jpanel.add(limitfield, c);
        final JButton fetchButton = new JButton("FETCH DATA");
//        fetchButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("entered");
//                int numlogs = Integer.parseInt(limitfield.getText());
//                System.out.println(numlogs);
//                String[][] data = new String[50][5];
//                for(int i =0; i < numlogs; i++){
//                    data[i] = new String[]{"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "...","asdf"};
//                }
//                String[] columnNames = {"time", "level", "logger", "thread", "message"};
//                JTable table = new JTable(data,columnNames);
//                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//                table.setBounds(30,40,200,200);
//                JScrollPane scroll = new JScrollPane(table);
//                scroll.setPreferredSize(new Dimension(300,100));
//                c.gridwidth = 8;
//                c.gridy = 1;
//                c.gridx = 0;
//                jpanel.add(scroll,c);
//            }
//        });
        c.gridx = 4;
        c.gridy = 0;
        jpanel.add(fetchButton, c);
        final JButton downloadButton = new JButton("DOWNLOAD STATS");
        c.gridx = 5;
        c.gridy = 0;
        jpanel.add(downloadButton, c);
        String[][] data = {
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."},
                {"2019-07-29T09:12:33.001Z", "WARN", "example", "main", "..."}
            };
        String[] columnNames = {"time", "level", "logger", "thread", "message"};
        JTable table = new JTable(data,columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(300,250));
        c.gridwidth = 8;
        c.gridheight = 5;
        c.gridy = 1;
        c.gridx = 0;
        jpanel.add(scroll,c);
    }
    public static void main(String args[]){
        LogMonitor Gui = new LogMonitor();
        Gui.setTitle("Log Monitor");
        Gui.setVisible(true);
    }
}

