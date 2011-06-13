/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hawmetering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;

/**
 *
 * @author heitmann
 */
public class DialChart {

  private MeterPlot meterplot;
  private JFreeChart jfreechart;
  private ChartPanel chartpanel;


  public void createChart(String s) {
    ValueDataset valuedataset = new DefaultValueDataset(0D);
    meterplot = new MeterPlot(valuedataset);
    meterplot.setDialShape(DialShape.CIRCLE);
    meterplot.setRange(new Range(0.0D, 100D));
    meterplot.addInterval(new MeterInterval("Normal", new Range(0.0D, 50D), Color.lightGray, new BasicStroke(2.0F), new Color(0, 255, 0, 64)));
    meterplot.addInterval(new MeterInterval("Warning", new Range(50D, 80D), Color.lightGray, new BasicStroke(2.0F), new Color(255, 255, 0, 64)));
    meterplot.addInterval(new MeterInterval("Critical", new Range(80D, 100D), Color.lightGray, new BasicStroke(2.0F), new Color(255, 0, 0, 128)));
    meterplot.setNeedlePaint(Color.darkGray);
    meterplot.setDialBackgroundPaint(Color.white);
    meterplot.setDialOutlinePaint(Color.gray);
    meterplot.setMeterAngle(260);
    meterplot.setTickLabelsVisible(true);
    meterplot.setTickLabelFont(new Font("Dialog", 1, 10));
    meterplot.setTickLabelPaint(Color.darkGray);
    meterplot.setTickSize(5D);
    meterplot.setTickPaint(Color.lightGray);
    meterplot.setValuePaint(Color.black);
    meterplot.setValueFont(new Font("Dialog", 1, 14));
    jfreechart = new JFreeChart(s, JFreeChart.DEFAULT_TITLE_FONT, meterplot, true);
    chartpanel = new ChartPanel(jfreechart);
  }

  public JFreeChart getChart(){
    return jfreechart;
  }

  public MeterPlot getMeterPlot(){
    return meterplot;
  }

  public ChartPanel getChartPanel(){
    return chartpanel;
  }

}
