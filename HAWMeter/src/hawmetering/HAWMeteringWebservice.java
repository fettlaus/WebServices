/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hawmetering;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author heitmann
 */
@WebService
@SOAPBinding(style = Style.RPC)
public class HAWMeteringWebservice {

  private MeterPlot meterplot;
  private DialChart dialchart;

  public HAWMeteringWebservice(DialChart dialchart) {
    this.dialchart = dialchart;
    this.meterplot = dialchart.getMeterPlot();
  }

  public void setRange(
          @WebParam(name = "min") double min,
          @WebParam(name = "max") double max) {
    meterplot.setRange(new Range(min, max));
  }

  public void setValue(
          @WebParam(name = "val") double val) {
    ((DefaultValueDataset) meterplot.getDataset()).setValue(val);
  }

  public void setIntervals(
          @WebParam(name = "label") String label,
          @WebParam(name = "min") double min,
          @WebParam(name = "max") double max,
          @WebParam(name = "color") WebColor color) {
    meterplot.addInterval(new MeterInterval(label, new Range(min, max),
            Color.lightGray, new BasicStroke(2.0F), new Color(color.red, color.green, color.blue, color.alpha)));
  }

  public void clearIntervals() {
    meterplot.clearIntervals();
  }

  public void setTitle(
          @WebParam(name = "title")String title) {
    dialchart.getChart().setTitle(title);
  }
}
