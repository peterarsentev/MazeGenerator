package com.odesk.maze;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenerateMazeAction implements ActionListener {
    private volatile Thread generator;
    private final MazePanel mazePanel;

    public GenerateMazeAction(MazePanel mazePanel) {
        this.mazePanel = mazePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (generator != null) {
            generator.interrupt();
        }
        generator = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("Start generated");
                    mazePanel.cleanup();
                    mazePanel.generateMaze(mazePanel.desk[0][0]);
                    System.out.println("finished generated");
                } catch (InterruptedException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        };
        generator.start();
    }
}
