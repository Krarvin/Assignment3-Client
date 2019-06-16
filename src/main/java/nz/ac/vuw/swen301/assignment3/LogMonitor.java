package nz.ac.vuw.swen301.assignment3;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.EventObject;

public class LogMonitor extends JFrame {
    public LogMonitor() {
        final JPanel jpanel = new JPanel(new GridBagLayout());
        jpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(jpanel);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 250);
        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,5,0,2);
        c.gridx = 0;
        c.gridy = 0;
        final JLabel minlevel = new JLabel("min level:");
        jpanel.add(minlevel, c);
        String[] levels = {"TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL"};
        final JComboBox levelList = new JComboBox(levels);
        levelList.setBounds(50,50,90,20);
        c.gridx = 1;
        c.gridy = 0;
        jpanel.add(levelList, c);
        final JLabel limit = new JLabel("limit:");
        c.gridx = 2;
        c.gridy = 0;
        jpanel.add(limit,c);
/*        String[] limits = new String[50];
        for(int i =1; i<=50; i++){
            limits[i] = i + "";
        }
        final JComboBox limitList = new JComboBox(limits);*/
//        limitList.setBounds(50,50,90,20);
        final JTextField limitfield = new JTextField("enter 1-50");
        limitfield.setPreferredSize(new Dimension(100,25));
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

        c.gridx = 4;
        c.gridy = 0;
        jpanel.add(fetchButton, c);
        final JButton downloadButton = new JButton("DOWNLOAD STATS");
        c.gridx = 5;
        c.gridy = 0;

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URIBuilder builder = new URIBuilder();
                    builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/stats");
                    URI getUri = builder.build();
                    HttpGet get = new HttpGet(getUri);
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse response1 = httpClient.execute(get);
                    System.out.println(response1.getEntity().getContent());
                    XSSFWorkbook workbook = new XSSFWorkbook(response1.getEntity().getContent());
                    FileOutputStream fileOut = new FileOutputStream("log-statistics.xlsx");
                    workbook.write(fileOut);
                    fileOut.close();
                    JOptionPane.showInternalMessageDialog(jpanel, "Excel stats created");
                }catch(Exception e2){
                    e2.printStackTrace();
                }
            }
        }

        );
        jpanel.add(downloadButton, c);
        String[] cols = {"","id","time", "level", "logger", "thread", "message"};
        String[][] rows = new String[50][7];
        final JTable tb = new JTable(rows, cols);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tb.setBounds(30,40,200,200);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 8;
        final JScrollPane scroll = new JScrollPane(tb);
        scroll.setPreferredSize(new Dimension(300,100));
        jpanel.add(scroll,c);

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String num = limitfield.getText();
                String level = levelList.getSelectedItem().toString();

                try {
                    URIBuilder builder = new URIBuilder();
                    builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs").setParameter("limit", num).setParameter("level",level);
                    URI getUri = builder.build();
                    HttpGet get = new HttpGet(getUri);
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse response = httpClient.execute(get);
                    String jsonString = EntityUtils.toString(response.getEntity());
                    int numlogs = Integer.parseInt(num);
                    if(numlogs <= 50 && numlogs >0) {
                    JSONArray jsonArray = new JSONArray(jsonString);
                        String[][] data = new String[numlogs][7];
                        TableModel model = tb.getModel();
                        for (int i = 0; i < numlogs && i < jsonArray.length(); i++) {
                            for (int j = 0; j < 7; j++) {
                                if (j == 0) {
//                                data[i][j] = jsonArray.getJSONObject(i).getString("id");
//                                model.setValueAt(jsonArray.getJSONObject(i).getString("id"),i,j);
                                    model.setValueAt(Integer.toString(i + 1), i, j);
                                }
                                if (j == 1) {
//                                data[i][j] = jsonArray.getJSONObject(i).getString("id");
                                    model.setValueAt(jsonArray.getJSONObject(i).getString("id"), i, j);
                                } else if (j == 2) {
                                    data[i][j] = jsonArray.getJSONObject(i).getString("timestamp");
                                    model.setValueAt(jsonArray.getJSONObject(i).getString("timestamp"), i, j);
                                } else if (j == 3) {
                                    data[i][j] = jsonArray.getJSONObject(i).getString("level");
                                    model.setValueAt(jsonArray.getJSONObject(i).getString("level"), i, j);
                                } else if (j == 4) {
                                    data[i][j] = jsonArray.getJSONObject(i).getString("logger");
                                    model.setValueAt(jsonArray.getJSONObject(i).getString("logger"), i, j);
                                } else if (j == 5) {
                                    data[i][j] = jsonArray.getJSONObject(i).getString("thread");
                                    model.setValueAt(jsonArray.getJSONObject(i).getString("thread"), i, j);
                                } else if (j == 6) {
                                    data[i][j] = jsonArray.getJSONObject(i).getString("message");
                                    model.setValueAt(jsonArray.getJSONObject(i).getString("message"), i, j);
                                }
                            }
                        }
                        for (int i = numlogs; i < 50; i++) {
                            for (int j = 0; j < 7; j++) {
                                if (j == 0) {
                                    model.setValueAt("", i, j);
                                } else if (j == 1) {
                                    model.setValueAt("", i, j);
                                } else if (j == 2) {
                                    model.setValueAt("", i, j);
                                } else if (j == 3) {
                                    model.setValueAt("", i, j);
                                } else if (j == 4) {
                                    model.setValueAt("", i, j);
                                } else if (j == 5) {
                                    model.setValueAt("", i, j);
                                } else {
                                    model.setValueAt("", i, j);
                                }
                            }
                        }
//                    String[] columnNames = {"id","time", "level", "logger", "thread", "message"};
                        tb.setModel(model);
                        tb.tableChanged(new TableModelEvent(tb.getModel()));
                        SwingUtilities.updateComponentTreeUI(scroll);
//                    JScrollPane scroll = new JScrollPane(tb);
//                    scroll.setPreferredSize(new Dimension(300,100));
                        c.gridwidth = 8;
                        c.gridy = 1;
                        c.gridx = 0;
                        jpanel.add(scroll, c);
                        jpanel.revalidate();
                        jpanel.repaint();
                    }
                }catch(Exception e1){
                    e1.printStackTrace();
                }

            }
        });
    }
    public static void main(String args[]){
        LogMonitor Gui = new LogMonitor();
        Gui.setTitle("Log Monitor");
        Gui.setVisible(true);
    }
}

