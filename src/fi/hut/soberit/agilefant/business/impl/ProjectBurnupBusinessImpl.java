package fi.hut.soberit.agilefant.business.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.ui.RectangleInsets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.ProjectBurnupBusiness;
import fi.hut.soberit.agilefant.db.BacklogHistoryEntryDAO;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.util.CustomXYStepRenderer;
import fi.hut.soberit.agilefant.util.ProjectBurnupData;
import fi.hut.soberit.agilefant.util.Triple;

@Service("projectBurnupBusiness")
public class ProjectBurnupBusinessImpl implements ProjectBurnupBusiness {

    @Autowired
    private BacklogHistoryEntryDAO backlogHistoryEntryDAO;

    /* Chart sizes */
    protected static final int DEFAULT_WIDTH = 780;
    protected static final int DEFAULT_HEIGHT = 600;
    protected static final int SMALL_WIDTH = 110;
    protected static final int SMALL_HEIGHT = 85;

    /* Chart backgrounds */
    protected static final Color CHART_BACKGROUND_COLOR = Color.white;
    protected static final Color PLOT_BACKGROUND_COLOR = Color.white;
    protected static final Color GRIDLINE_COLOR = new Color(0xcc, 0xcc, 0xcc);

    /* Axis titles */
    protected static final String DATE_AXIS_LABEL = "Date";
    protected static final String STORYPOINTS_AXIS_LABEL = "Story points";

    /* Names */
    protected static final String CHART_NAME = "Project burnup";
    protected static final String PLANNED_NAME = "Points in leaf stories";
    protected static final String BRANCH_MAX_NAME = "Points in project";
    protected static final String DONE_NAME = "Points done";

    
    @Transactional(readOnly = true)
    public byte[] getBurnup(Project project) {
        return getChartImageByteArray(constructChart(project));
    }
    
    @Transactional(readOnly = true)
    public byte[] getSmallBurnup(Project project) {
        return getChartImageByteArray(constructSmallChart(project), SMALL_WIDTH, SMALL_HEIGHT);
    }
    
    
    /**
     * Generates a byte array (a png image file) from a JFreeChart object
     * 
     * @param chart
     *            A chart object from which the image is created
     * @param width
     *            Width of the created image
     * @param height
     *            Height of the created image
     * @return Byte array representing a png image file
     */
    protected byte[] getChartImageByteArray(JFreeChart chart, int width,
            int height) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsPNG(out, chart, width, height);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected byte[] getChartImageByteArray(JFreeChart chart) {
        return getChartImageByteArray(chart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }


    protected JFreeChart constructChart(Project project) {
        ProjectBurnupData data = backlogHistoryEntryDAO
                .retrieveBurnupData(project.getId());

        JFreeChart burnup = ChartFactory.createXYStepChart("'" + project.getName() + "' burnup",
                DATE_AXIS_LABEL, STORYPOINTS_AXIS_LABEL, null,
                PlotOrientation.VERTICAL, true, true, false);

        Triple<TimeSeriesCollection, TimeSeriesCollection, TimeSeriesCollection> datasets = convertToDatasets(data);

        XYPlot plot = burnup.getXYPlot();
        plot.setRenderer(0, getPlannedRenderer());
        plot.setDataset(0, datasets.first);
        plot.setRenderer(1, getDoneRenderer());
        plot.setDataset(1, datasets.second);
        plot.setRenderer(2, getBranchMaxRenderer());
        plot.setDataset(2, datasets.third);

        formatChartAxes(burnup, new DateTime(project.getStartDate()),
                new DateTime(project.getEndDate()));

        formatChartStyle(burnup);

        return burnup;
    }
    
    protected JFreeChart constructSmallChart(Project project) {
        JFreeChart burndown = this.constructChart(project);
        return transformToSmallChart(burndown);
    }
    
    /**
     * Trims and transforms a big burndown chart to a small one.
     */
    protected JFreeChart transformToSmallChart(JFreeChart burndownChart) {
        JFreeChart chart = burndownChart;
        XYPlot plot = chart.getXYPlot();
        
        chart.setBackgroundPaint(CHART_BACKGROUND_COLOR);
        plot.setBackgroundPaint(PLOT_BACKGROUND_COLOR);
        
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.getRangeAxis().setVisible(false);
        
        plot.getDomainAxis().setLabel(null);
        plot.getRangeAxis().setLabel(null);
        
        RectangleInsets ins = new RectangleInsets(-6, -8, -3, -7);
        chart.setPadding(ins);

        chart.removeLegend();
        chart.setTitle("");
        
        return chart;
    }

    protected Triple<TimeSeriesCollection, TimeSeriesCollection, TimeSeriesCollection> convertToDatasets(
            ProjectBurnupData data) {
        TimeSeries planned = new TimeSeries(PLANNED_NAME);
        TimeSeries done = new TimeSeries(DONE_NAME);
        TimeSeries branchMax = new TimeSeries(BRANCH_MAX_NAME);
        DateTime now = new DateTime();

        ProjectBurnupData.Entry lastEntry = null;
        ProjectBurnupData.Entry lastDoneEntry = null;
        for (ProjectBurnupData.Entry entry : data) {
            if (entry.timestamp.isAfter(now)) {
                break;
            }
            Second second = new Second(entry.timestamp.toDate());
            if (lastDoneEntry == null || entry.doneSum != lastDoneEntry.doneSum) {
                TimeSeriesDataItem item = new TimeSeriesDataItem(second,
                        entry.doneSum);
                done.add(item);
                lastDoneEntry = entry;
            }
            TimeSeriesDataItem item = new TimeSeriesDataItem(second,
                    entry.estimateSum);
            planned.add(item);
            TimeSeriesDataItem branchItem = new TimeSeriesDataItem(second, 
                    entry.branchMax);
            branchMax.add(branchItem);
            lastEntry = entry;
        }

        Second nowSecond = new Second(now.toDate());
        if (lastEntry != null && !lastEntry.timestamp.isEqual(now)) {
            TimeSeriesDataItem nowItem = new TimeSeriesDataItem(nowSecond,
                    lastEntry.estimateSum);
            planned.add(nowItem);
            TimeSeriesDataItem branchItem = new TimeSeriesDataItem(nowSecond,
                    lastEntry.branchMax);
            branchMax.add(branchItem);
        }
        if (lastDoneEntry != null && !lastDoneEntry.timestamp.isEqual(now)) {
            TimeSeriesDataItem nowItem = new TimeSeriesDataItem(nowSecond,
                    lastEntry.doneSum);
            done.add(nowItem);
        }

        return Triple.create(new TimeSeriesCollection(planned),
                new TimeSeriesCollection(done), new TimeSeriesCollection(branchMax));
    }

    /**
     * Sets the chart's and plot's background colors.
     */
    protected void formatChartStyle(JFreeChart chart) {
        chart.setBackgroundPaint(CHART_BACKGROUND_COLOR);
        chart.getPlot().setBackgroundPaint(PLOT_BACKGROUND_COLOR);
    }

    /**
     * Sets the correct start and end dates and date format.
     */
    protected void formatChartAxes(JFreeChart chart, DateTime start,
            DateTime end) {
        XYPlot plot = chart.getXYPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setMaximumDate(end.toDateMidnight().plusDays(1).toDate());
        axis.setMinimumDate(start.toDateMidnight().toDate());
        axis.setDateFormatOverride(new SimpleDateFormat("EEE d.M."));
        plot.setDomainGridlinePaint(GRIDLINE_COLOR);
        plot.setRangeGridlinePaint(GRIDLINE_COLOR);
    }

    protected XYAreaRenderer getDoneRenderer() {
        XYAreaRenderer result = new XYAreaRenderer();
        result.setSeriesShape(0, new Rectangle(-2, -2, 4, 4));
        result.setSeriesPaint(0, new Color(0, 187, 0, 100));
        return result;
    }

    protected XYStepRenderer getPlannedRenderer() {
        CustomXYStepRenderer result = new CustomXYStepRenderer();
        result.setSeriesPaint(0, new Color(220, 100, 87));
        Stroke stroke = new BasicStroke(1.5f);
        Stroke stepStroke = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 1.0f, new float[] { 6.0f, 6.0f }, 0.0f);
        result.setSeriesStroke(0, stroke);
        result.setSeriesShape(0, null);
        result.setStepStroke(stepStroke);
        return result;
    }

    protected XYStepRenderer getBranchMaxRenderer() {
        CustomXYStepRenderer result = new CustomXYStepRenderer();
        result.setSeriesPaint(0, new Color(255, 0, 255));
        Stroke stroke = new BasicStroke(1.5f);
        Stroke stepStroke = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 1.0f, new float[] { 6.0f, 6.0f }, 0.0f);
        result.setSeriesStroke(0, stroke);
        result.setSeriesShape(0, null);
        result.setStepStroke(stepStroke);
        return result;
    }
    
}
