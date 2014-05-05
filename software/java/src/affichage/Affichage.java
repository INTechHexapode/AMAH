package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import socket.SocketClient;
import socket.SocketClient.Request;

public class Affichage extends ApplicationFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Dynamic Series";
    private static final String START = "Start";
    private static final String STOP = "Stop";
//    private static final float MINMAX = 2;
    private static final int COUNT = 2 * 60;
    private static final int FAST = 100;
    private static final int SLOW = FAST * 5;
    private static final SocketClient socket = new SocketClient();
    private Timer timer;
    private static int plotCounter = 0;
    XYPlot plot;

    public Affichage(final String title, final Request request, int minMax) {
        super(title);
        final DynamicTimeSeriesCollection xAccelerometer =
            new DynamicTimeSeriesCollection(1, COUNT, new Second()); 
        xAccelerometer.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
        xAccelerometer.addSeries(zeroData(), 0, "ACCEL X");
        JFreeChart chart = createChart(xAccelerometer, "mm/s^-² ou rad/s-²", minMax);
        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
        renderer0.setSeriesShapesVisible(0, false);
        plot = chart.getXYPlot();
        plot.setRenderer(0, renderer0);
        
        final DynamicTimeSeriesCollection yAccelerometer =
                new DynamicTimeSeriesCollection(1, COUNT, new Second());
        addDataset(yAccelerometer, "ACCEL Y");
        
        final DynamicTimeSeriesCollection xGyro =
                new DynamicTimeSeriesCollection(1, COUNT, new Second());
        addDataset(xGyro, "GYRO X");

        
        final DynamicTimeSeriesCollection yGyro =
                new DynamicTimeSeriesCollection(1, COUNT, new Second());
        addDataset(yGyro, "GYRO Y");
        
        final DynamicTimeSeriesCollection xMagne =
                new DynamicTimeSeriesCollection(1, COUNT, new Second());
        addDataset(xMagne, "MAGNE X");
        
        final DynamicTimeSeriesCollection yMagne =
                new DynamicTimeSeriesCollection(1, COUNT, new Second());
        addDataset(yMagne, "MAGNE Y");
        

        final JButton run = new JButton(STOP);
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (STOP.equals(cmd)) {
                    timer.stop();
                    run.setText(START);
                } else {
                    timer.start();
                    run.setText(STOP);
                }
            }
        });

        final JComboBox<String> combo = new JComboBox<String>();
        combo.addItem("Fast");
        combo.addItem("Slow");
        combo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Fast".equals(combo.getSelectedItem())) {
                    timer.setDelay(FAST);
                } else {
                    timer.setDelay(SLOW);
                }
            }
        });

        this.add(new ChartPanel(chart), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(run);
        btnPanel.add(combo);
        this.add(btnPanel, BorderLayout.SOUTH);

        timer = new Timer(FAST, new ActionListener() {

            float[] newData = new float[1];

            @Override
            public void actionPerformed(ActionEvent e) {
                newData[0] = Float.parseFloat(socket.sendMessage(Request.LIN_ACC_X));
                xAccelerometer.advanceTime();
                xAccelerometer.appendData(newData);
                
                newData[0] = Float.parseFloat(socket.sendMessage(Request.LIN_ACC_Y));
                yAccelerometer.advanceTime();
                yAccelerometer.appendData(newData);
                
                newData[0] = Float.parseFloat(socket.sendMessage(Request.GYRO_X));
                xGyro.advanceTime();
                xGyro.appendData(newData);
                
                newData[0] = Float.parseFloat(socket.sendMessage(Request.GYRO_Y));
                yGyro.advanceTime();
                yGyro.appendData(newData);
                
                newData[0] = Float.parseFloat(socket.sendMessage(Request.AZIMUTH));
                xMagne.advanceTime();
                xMagne.appendData(newData);
                
                newData[0] = Float.parseFloat(socket.sendMessage(Request.PITCH));
                yMagne.advanceTime();
                yMagne.appendData(newData);
                
            }
        });
    }

    private void addDataset(DynamicTimeSeriesCollection newDTS, String name) {
    	newDTS.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
    	newDTS.addSeries(zeroData(), 0, name);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(); 
        renderer.setSeriesShapesVisible(0, false);
        plotCounter++;
        plot.setDataset(plotCounter, newDTS);
        plot.setRenderer(plotCounter, renderer);
    }
    private float[] zeroData() {
        float[] a = new float[COUNT];
        for (int i = 0; i < a.length; i++) {
            a[i] = 0;
        }
        return a;
    }
    
    public void setVisibility(boolean xAcc, boolean yAcc, boolean xGyro, boolean yGyro, boolean xMagne, boolean yMagne) {
    	if(!xAcc)
    		plot.setDataset(0, null);
    	if(!yAcc)
    		plot.setDataset(1, null);
    	if(!xGyro)
    		plot.setDataset(2, null);
    	if(!yGyro)
    		plot.setDataset(3, null);
    	if(!xMagne)
    		plot.setDataset(4, null);
    	if(!yMagne)
    		plot.setDataset(5, null);

    }
    
    private JFreeChart createChart(final XYDataset dataset,String units, int minMax) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            TITLE, "hh:mm:ss", units, dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(-minMax, minMax);
        return result;
    }

    public void start() {
        timer.start();
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Affichage speed_x = new Affichage("Accelero X", Request.LIN_ACC_X, 2);
                speed_x.pack();
                speed_x.setVisibility(true, false, true, false, true, false);
                RefineryUtilities.centerFrameOnScreen(speed_x);
                speed_x.setVisible(true);
                speed_x.start();
            }
        });
    }
}