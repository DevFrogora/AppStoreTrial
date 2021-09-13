/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstoretrial;

import gui.AppTemplateCard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

/**
 *
 * @author root
 */
public class AppStoreFrame extends JFrame {

    JPanel mainPanel = null;
    JScrollPane scrollPane = null;

    public AppStoreFrame() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 700));
        mainPanel = new JPanel();

        GridLayout gridLayout = new GridLayout(0, 3);
        gridLayout.setHgap(10);
        gridLayout.setVgap(10);

        mainPanel.setLayout(gridLayout);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setLayout(new ScrollPaneLayout());  // no needed its still working
        JButton jButton = new JButton("My Button");
        int a = 1;
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        add(jButton, BorderLayout.NORTH);

        scrollPane.setViewportBorder(
                BorderFactory.createLineBorder(Color.red));
        add(scrollPane, BorderLayout.CENTER);

//        pack();
//        setVisible(true);
    }

    public static JPanel addPanel(int appId, String appName, String appDescription, Image appImage, String appZipFileName, String appVersionNumber,String startPath) {
        AppTemplateCard newPanel = new AppTemplateCard(appId, appName, appDescription, appImage, appZipFileName, appVersionNumber,startPath);
//        newPanel.setPreferredSize(new Dimension(200, 200));
        return newPanel;
    }

    public void addAppInPanel(int appId, String appName, String appDescription, Image appImage, String appZipFileName, String appVersionNumber,String startPath) {
        mainPanel.add(addPanel(appId, appName, appDescription, appImage, appZipFileName, appVersionNumber,startPath));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

}
