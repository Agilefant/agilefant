package fi.hut.soberit.agilefant.business;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import fi.hut.soberit.agilefant.business.impl.IterationBurndownBusinessImpl;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.HourEntry;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.IterationHistoryEntry;
import fi.hut.soberit.agilefant.transfer.DailySpentEffort;
import fi.hut.soberit.agilefant.util.ExactEstimateUtils;
import fi.hut.soberit.agilefant.util.Pair;

/**
 * Test class for <code>IterationBurndownBusiness</code>.
 * <p>
 * Extends the class to be able to test protected methods.
 * 
 * @author rjokelai, jsorvett, ahoffman
 *
 */
public class IterationBurndownBusinessTest extends IterationBurndownBusinessImpl {

    IterationBurndownBusinessImpl iterationBurndownBusiness;
    IterationHistoryEntryBusiness iterationHistoryEntryBusiness;
    HourEntryBusiness hourEntryBusiness;
    IterationBusiness iterationBusiness;
    SettingBusiness settingBusiness;
    
    Iteration iteration;
    DateTime startDate;
    DateTime endDate;
    ExactEstimate originalEstimateSum;
    IterationHistoryEntry entry;
    JFreeChart chart;
    
    List<DailySpentEffort> dailySpentEffortList;
    List<HourEntry> hourEntryList;
    
    IterationHistoryEntry entry1;
    IterationHistoryEntry entry2;
    List<IterationHistoryEntry> entriesList;
    
    IterationHistoryEntry startEntry;
    IterationHistoryEntry endEntry;
       
    @Before
    public void setUp() {
        iterationBurndownBusiness = new IterationBurndownBusinessImpl();
        iterationHistoryEntryBusiness = createMock(IterationHistoryEntryBusiness.class);
        iterationBurndownBusiness.setIterationHistoryEntryBusiness(iterationHistoryEntryBusiness);
        super.setIterationHistoryEntryBusiness(iterationHistoryEntryBusiness);
        
        iterationBusiness = createMock(IterationBusiness.class);
        iterationBurndownBusiness.setIterationBusiness(iterationBusiness);
        super.setIterationBusiness(iterationBusiness);
        
        hourEntryBusiness = createMock(HourEntryBusiness.class);
        iterationBurndownBusiness.setHourEntryBusiness(hourEntryBusiness);
        super.setHourEntryBusiness(hourEntryBusiness);
        
        settingBusiness = createMock(SettingBusiness.class);
        iterationBurndownBusiness.setSettingBusiness(settingBusiness);
        super.setSettingBusiness(settingBusiness);
        
        startDate = new DateTime(2009,1,1,0,0,0,0);
        endDate = new DateTime(2009,1,10,0,0,0,0);
        iteration = new Iteration();
        iteration.setId(123);
        iteration.setStartDate(startDate);
        iteration.setEndDate(endDate);
        originalEstimateSum = new ExactEstimate(100 * 60);
        
        chart = ChartFactory.createTimeSeriesChart(EFFORT_LEFT_SERIES_NAME,
                DATE_AXIS_LABEL,
                EFFORT_AXIS_LABEL,
                null, true, true, false);
        
        entry = new IterationHistoryEntry();
        entry.setTimestamp(new LocalDate(2009,1,1));
        entry.setEffortLeftSum(240);
        
        
        entry1 = new IterationHistoryEntry();
        entry1.setTimestamp(startDate.toLocalDate());
        entry1.setEffortLeftSum(100);
        entry1.setOriginalEstimateSum(100);
        entry2 = new IterationHistoryEntry();
        entry2.setTimestamp(startDate.plusDays(1).toLocalDate());
        entry2.setEffortLeftSum(0);
        entry2.setOriginalEstimateSum(100);
        
        entriesList = Arrays.asList(entry1, entry2);
        
        dailySpentEffortList = new ArrayList<DailySpentEffort>();
        hourEntryList = new ArrayList<HourEntry>();
        
        
    }
    
    @Test
    public void testGetIterationBurndown() {
        expect(iterationHistoryEntryBusiness.getHistoryEntriesForIteration(iteration)).andReturn(Arrays.asList(entry));
        expect(iterationHistoryEntryBusiness.calculateExpectedEffortDoneDate(isA(LocalDate.class), isA(ExactEstimate.class), isA(ExactEstimate.class))).andReturn(null);
        
        expect(iterationBusiness.calculateDailyVelocity(isA(LocalDate.class), isA(LocalDate.class), isA(IterationHistoryEntry.class))).andReturn(ExactEstimate.ZERO);
        
        expect(hourEntryBusiness.getDailySpentEffortByIteration(iteration)).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getHourEntriesForIteration(iteration)).andReturn(hourEntryList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), iteration.getEndDate().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate(), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        replay(iterationHistoryEntryBusiness, iterationBusiness, hourEntryBusiness, settingBusiness);
        
        assertNotNull(iterationBurndownBusiness.getIterationBurndown(iteration, 0));
        
        // TODO: Verify hourEntryBusiness
        verify(iterationHistoryEntryBusiness, iterationBusiness);
    }
    
    @Test
    public void testGetSmallIterationBurndown() {
        expect(iterationHistoryEntryBusiness.getHistoryEntriesForIteration(iteration)).andReturn(Arrays.asList(entry));
        expect(iterationHistoryEntryBusiness.calculateExpectedEffortDoneDate(isA(LocalDate.class), isA(ExactEstimate.class), isA(ExactEstimate.class))).andReturn(null);
        
        expect(iterationBusiness.calculateDailyVelocity(isA(LocalDate.class), isA(LocalDate.class), isA(IterationHistoryEntry.class))).andReturn(ExactEstimate.ZERO);
        
        expect(hourEntryBusiness.getHourEntriesForIteration(iteration)).andReturn(hourEntryList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), iteration.getEndDate().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate(), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        replay(iterationHistoryEntryBusiness, iterationBusiness, hourEntryBusiness, settingBusiness);

        assertNotNull(iterationBurndownBusiness.getSmallIterationBurndown(iteration, 0));

        // TODO: Verify hourEntryBusiness
        verify(iterationHistoryEntryBusiness, iterationBusiness);
    }
    
    @Test
    public void testCustomIterationBurndown() {
        expect(iterationHistoryEntryBusiness.getHistoryEntriesForIteration(iteration)).andReturn(Arrays.asList(entry));
        expect(iterationHistoryEntryBusiness.calculateExpectedEffortDoneDate(isA(LocalDate.class), isA(ExactEstimate.class), isA(ExactEstimate.class))).andReturn(null);
        
        expect(iterationBusiness.calculateDailyVelocity(isA(LocalDate.class), isA(LocalDate.class), isA(IterationHistoryEntry.class))).andReturn(ExactEstimate.ZERO);
        
        expect(hourEntryBusiness.getHourEntriesForIteration(iteration)).andReturn(hourEntryList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), iteration.getEndDate().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate(), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        replay(iterationHistoryEntryBusiness, iterationBusiness, hourEntryBusiness, settingBusiness);
        
        assertNotNull(iterationBurndownBusiness.getCustomIterationBurndown(iteration, 1024, 768, 0));
        
        // TODO: Verify hourEntryBusiness
        verify(iterationHistoryEntryBusiness, iterationBusiness);
    }
    
    @Test
    public void testConstructChart() {
        expect(iterationHistoryEntryBusiness.getHistoryEntriesForIteration(iteration)).andReturn(Arrays.asList(entry));
        expect(iterationHistoryEntryBusiness.calculateExpectedEffortDoneDate(isA(LocalDate.class), isA(ExactEstimate.class), isA(ExactEstimate.class))).andReturn(null);
        
        expect(iterationBusiness.calculateDailyVelocity(isA(LocalDate.class), isA(LocalDate.class), isA(IterationHistoryEntry.class))).andReturn(ExactEstimate.ZERO);
        
        expect(hourEntryBusiness.getHourEntriesForIteration(iteration)).andReturn(hourEntryList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), iteration.getEndDate().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate(), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        
        replay(iterationHistoryEntryBusiness, iterationBusiness, hourEntryBusiness, settingBusiness);
        
        JFreeChart newChart = super.constructChart(iteration, 0);
        
        assertEquals(REFERENCE_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(REFERENCE_SERIES_NO));
        assertEquals(EFFORT_LEFT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(EFFORT_LEFT_SERIES_NO));
        assertEquals(EFFORT_SPENT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_SPENT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_LEFT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(SCOPING_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(SCOPING_SERIES_NO));
        
        // TODO: Verify hourEntryBusiness
        verify(iterationHistoryEntryBusiness, iterationBusiness);
    }
    
    @Test
    public void testConstructSmallChart() {
        expect(iterationHistoryEntryBusiness.getHistoryEntriesForIteration(iteration)).andReturn(Arrays.asList(entry));
        expect(iterationHistoryEntryBusiness.calculateExpectedEffortDoneDate(isA(LocalDate.class), isA(ExactEstimate.class), isA(ExactEstimate.class))).andReturn(null);
        
        expect(iterationBusiness.calculateDailyVelocity(isA(LocalDate.class), isA(LocalDate.class), isA(IterationHistoryEntry.class))).andReturn(ExactEstimate.ZERO);
        
        expect(hourEntryBusiness.getHourEntriesForIteration(iteration)).andReturn(hourEntryList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), iteration.getEndDate().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate(), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        replay(iterationHistoryEntryBusiness, iterationBusiness, hourEntryBusiness, settingBusiness);
        
        JFreeChart newChart = super.constructSmallChart(iteration, 0);
        
        assertEquals(REFERENCE_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(REFERENCE_SERIES_NO));
        assertEquals(EFFORT_LEFT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(EFFORT_LEFT_SERIES_NO));
        assertEquals(EFFORT_SPENT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_SPENT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_LEFT_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(SCOPING_SERIES_NAME,
                newChart.getXYPlot().getDataset().getSeriesKey(SCOPING_SERIES_NO));
               
        testSmallChartFormating(newChart);
        
        // TODO: Verify hourEntryBusiness
        verify(iterationHistoryEntryBusiness, iterationBusiness);
    }
    
    private void testSmallChartFormating(JFreeChart chart) {
        assertEquals(PLOT_BACKGROUND_COLOR, chart.getPlot().getBackgroundPaint());
       
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer();
        assertEquals(EFFORT_LEFT_SERIES_COLOR, rend.getSeriesPaint(EFFORT_LEFT_SERIES_NO));
        assertEquals(EFFORT_SPENT_SERIES_COLOR, rend.getSeriesPaint(EFFORT_SPENT_SERIES_NO));        
        assertEquals(EFFORT_LEFT_SERIES_COLOR, rend.getSeriesPaint(SCOPING_SERIES_NO));
        assertEquals(EFFORT_LEFT_SERIES_COLOR, rend.getSeriesPaint(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(EFFORT_SPENT_SERIES_COLOR, rend.getSeriesPaint(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(REFERENCE_SERIES_COLOR, rend.getSeriesPaint(REFERENCE_SERIES_NO));
        
        assertEquals(SMALL_BURNDOWN_STROKE, rend.getSeriesStroke(EFFORT_LEFT_SERIES_NO));
        assertEquals(SMALL_BURNDOWN_STROKE, rend.getSeriesStroke(EFFORT_SPENT_SERIES_NO));
        assertEquals(SMALL_BURNDOWN_STROKE, rend.getSeriesStroke(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(SMALL_BURNDOWN_STROKE, rend.getSeriesStroke(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(SMALL_BURNDOWN_STROKE, rend.getSeriesStroke(SCOPING_SERIES_NO));
        assertEquals(SMALL_BURNDOWN_STROKE, rend.getSeriesStroke(REFERENCE_SERIES_NO));
        
        assertFalse(plot.getDomainAxis().isVisible());
        assertFalse(plot.getRangeAxis().isVisible());
        assertFalse(plot.isDomainGridlinesVisible());
        assertFalse(plot.isRangeGridlinesVisible());
        
        assertFalse(rend.getSeriesShapesVisible(EFFORT_LEFT_SERIES_NO));
        assertFalse(rend.getSeriesShapesVisible(EFFORT_SPENT_SERIES_NO));
        assertFalse(rend.getSeriesShapesVisible(REFERENCE_SERIES_NO));
        assertFalse(rend.getSeriesShapesVisible(SCOPING_SERIES_NO));
        assertFalse(rend.getSeriesShapesVisible(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertFalse(rend.getSeriesShapesVisible(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        
        assertEquals(null, plot.getDomainAxis().getLabel());
        assertEquals(null, plot.getRangeAxis().getLabel());
        
        assertNull(chart.getLegend());
    }
    
    @Test
    public void testTransformToSmallChart() {
        JFreeChart newChart = super.transformToSmallChart(chart);
        testSmallChartFormating(newChart);
    }
    
    @Test
    public void testFormatChartAxes() {
        super.formatChartAxes(chart, startDate, endDate);
        
        DateAxis actualAxis = (DateAxis)chart.getXYPlot().getDomainAxis();
        
        assertEquals("Start date not same", startDate.toDateMidnight().toDate(), actualAxis.getMinimumDate());
        assertEquals("End date not same", endDate.plusDays(1).toDateMidnight().toDate(), actualAxis.getMaximumDate());
        
        assertEquals(GRIDLINE_COLOR, chart.getXYPlot().getRangeGridlinePaint());
        assertEquals(GRIDLINE_COLOR, chart.getXYPlot().getDomainGridlinePaint());
    } 
    
    @Test
    public void testFormatChartStyle() {
        super.formatChartStyle(chart);
        assertEquals(CHART_BACKGROUND_COLOR, chart.getBackgroundPaint());
        assertEquals(PLOT_BACKGROUND_COLOR, chart.getPlot().getBackgroundPaint());
        
        testSetSeriesStyles();
    }
    
    private void testSetSeriesStyles() {
        XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) ((XYPlot) chart
                .getPlot()).getRenderer();
        
        assertEquals(EFFORT_LEFT_SERIES_COLOR, rend.getSeriesPaint(EFFORT_LEFT_SERIES_NO));
        assertEquals(EFFORT_LEFT_SERIES_SHAPE, rend.getSeriesShape(EFFORT_LEFT_SERIES_NO));
        assertEquals(EFFORT_LEFT_SERIES_SHAPE_VISIBLE, rend.getSeriesShapesVisible(EFFORT_LEFT_SERIES_NO));
        
        assertEquals(EFFORT_SPENT_SERIES_COLOR, rend.getSeriesPaint(EFFORT_SPENT_SERIES_NO));
        assertEquals(EFFORT_SPENT_SERIES_SHAPE, rend.getSeriesShape(EFFORT_SPENT_SERIES_NO));
        assertEquals(EFFORT_SPENT_SERIES_SHAPE_VISIBLE, rend.getSeriesShapesVisible(EFFORT_SPENT_SERIES_NO));
        
        assertEquals(REFERENCE_SERIES_COLOR, rend.getSeriesPaint(REFERENCE_SERIES_NO));
        
        assertEquals(CURRENT_DAY_EFFORT_LEFT_SERIES_COLOR, rend.getSeriesPaint(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(CURRENT_DAY_SERIES_STROKE, rend.getSeriesStroke(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_LEFT_SERIES_SHAPE, rend.getSeriesShape(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_LEFT_SERIES_SHAPE_VISIBLE, rend.getSeriesShapesVisible(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_LEFT_SERIES_SHAPE_FILLED, rend.getSeriesShapesFilled(CURRENT_DAY_EFFORT_LEFT_SERIES_NO));
        
        assertEquals(CURRENT_DAY_EFFORT_SPENT_SERIES_COLOR, rend.getSeriesPaint(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_SERIES_STROKE, rend.getSeriesStroke(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_SPENT_SERIES_SHAPE, rend.getSeriesShape(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_SPENT_SERIES_SHAPE_VISIBLE, rend.getSeriesShapesVisible(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        assertEquals(CURRENT_DAY_EFFORT_SPENT_SERIES_SHAPE_FILLED, rend.getSeriesShapesFilled(CURRENT_DAY_EFFORT_SPENT_SERIES_NO));
        
        assertEquals(SCOPING_SERIES_STROKE, rend.getSeriesStroke(SCOPING_SERIES_NO));
        assertEquals(SCOPING_SERIES_COLOR, rend.getSeriesPaint(SCOPING_SERIES_NO));
    }
    
    @Test
    public void testGetDataset() {
        expect(iterationHistoryEntryBusiness.getHistoryEntriesForIteration(iteration)).andReturn(Arrays.asList(entry));
        expect(iterationHistoryEntryBusiness.calculateExpectedEffortDoneDate(isA(LocalDate.class), isA(ExactEstimate.class), isA(ExactEstimate.class))).andReturn(null);
        
        expect(iterationBusiness.calculateDailyVelocity(isA(LocalDate.class), isA(LocalDate.class), isA(IterationHistoryEntry.class))).andReturn(ExactEstimate.ZERO);
        
        expect(hourEntryBusiness.getHourEntriesForIteration(iteration)).andReturn(hourEntryList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), iteration.getEndDate().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate().minusDays(1), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        expect(hourEntryBusiness.getDailySpentEffortForHourEntries(hourEntryList, iteration.getStartDate(), new DateTime().toDateMidnight().toDateTime().plusDays(1))).andReturn(dailySpentEffortList);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(true);
        expect(settingBusiness.isHourReportingEnabled()).andReturn(true);
        
        replay(iterationHistoryEntryBusiness, iterationBusiness, hourEntryBusiness, settingBusiness);
        
        TimeSeriesCollection actualTimeSeries = super.getDataset(iteration);
        assertNotNull(actualTimeSeries.getSeries(REFERENCE_SERIES_NAME));
        assertNotNull(actualTimeSeries.getSeries(EFFORT_LEFT_SERIES_NAME));
        assertNotNull(actualTimeSeries.getSeries(CURRENT_DAY_EFFORT_LEFT_SERIES_NAME));
        assertNotNull(actualTimeSeries.getSeries(SCOPING_SERIES_NAME));
        
        // TODO: Verify hourEntryBusiness
        verify(iterationHistoryEntryBusiness, iterationBusiness);
    }
    
    @Test
    public void testDetermineEndDate_currentDateBeforeEndDate() {
        assertEquals(new LocalDate(), super.determineEndDate(new LocalDate().plusDays(3)));
    }
    
    @Test
    public void testDetermineEndDate_currentDateEqualsEndDate() {
        assertEquals(new LocalDate(), super.determineEndDate(new LocalDate().plusDays(3)));
    }
    
    @Test
    public void testDetermineEndDate_currentDateAfterEndDate() {
        assertEquals(new LocalDate().minusDays(2), super.determineEndDate(new LocalDate().minusDays(3)));
    }
    
    @Test
    public void testGetHistoryEntryForDate_dateFound() {
        assertEquals(entry1, super.getHistoryEntryForDate(entriesList, entry1.getTimestamp()));
    }
    
    @Test
    public void testGetHistoryEntryForDate_entryFromYesterday() {
        entry2.setDeltaOriginalEstimate(228);
        IterationHistoryEntry actualEntry = super.getHistoryEntryForDate(
                entriesList, entry2.getTimestamp().plusDays(1));
        
        assertEquals(entry2.getOriginalEstimateSum(), actualEntry.getOriginalEstimateSum());
        assertEquals(entry2.getEffortLeftSum(), actualEntry.getEffortLeftSum());
        assertEquals(0l, actualEntry.getDeltaOriginalEstimate());
    }
    
    @Test
    public void testGetHistoryEntryForDate_entryFromFarBehind() {
        entry2.setDeltaOriginalEstimate(228);
        IterationHistoryEntry actualEntry = super.getHistoryEntryForDate(
                entriesList, entry2.getTimestamp().plusDays(102));
        
        assertEquals(entry2.getOriginalEstimateSum(), actualEntry.getOriginalEstimateSum());
        assertEquals(entry2.getEffortLeftSum(), actualEntry.getEffortLeftSum());
        assertEquals(0l, actualEntry.getDeltaOriginalEstimate());
    }
    
    @Test
    public void testGetHistoryEntryForDate_endDateBetweenEntries() {
        IterationHistoryEntry forecomingEntry = new IterationHistoryEntry();
        forecomingEntry.setTimestamp(new LocalDate().plusDays(66));
        
        List<IterationHistoryEntry> newEntries = new ArrayList<IterationHistoryEntry>();
        newEntries.addAll(entriesList);
        newEntries.add(forecomingEntry);
        
        entry2.setDeltaOriginalEstimate(228);
        IterationHistoryEntry actualEntry = super.getHistoryEntryForDate(
                entriesList, entry2.getTimestamp().plusDays(16));
        
        assertEquals(entry2.getOriginalEstimateSum(), actualEntry.getOriginalEstimateSum());
        assertEquals(entry2.getEffortLeftSum(), actualEntry.getEffortLeftSum());
        assertEquals(0l, actualEntry.getDeltaOriginalEstimate());
    }
    
    @Test
    public void testGetHistoryEntryForDate_emptyEntry() {
        List<IterationHistoryEntry> emptyList = new ArrayList<IterationHistoryEntry>();
        IterationHistoryEntry actualEntry = super.getHistoryEntryForDate(emptyList, startDate.toLocalDate());
        assertEquals(0l , actualEntry.getEffortLeftSum());
        assertEquals(0l , actualEntry.getOriginalEstimateSum());
        assertEquals(0l , actualEntry.getDeltaOriginalEstimate());
    }
    
    @Test
    public void testGetIterationHistoryEntries() {
        expect(iterationHistoryEntryBusiness.getHistoryEntriesForIteration(iteration))
            .andReturn(entriesList);
        replay(iterationHistoryEntryBusiness);
        
        assertSame(entriesList, super.getIterationHistoryEntries(iteration));
        
        verify(iterationHistoryEntryBusiness);
    }
    
    private void initializeEntriesForScopingTest() {
        startEntry = new IterationHistoryEntry();
        startEntry.setOriginalEstimateSum(120);
        startEntry.setEffortLeftSum(120);
        startEntry.setDeltaOriginalEstimate(0);
        startEntry.setTimestamp(new LocalDate(2009,1,1));
        
        endEntry = new IterationHistoryEntry();
        endEntry.setOriginalEstimateSum(120);
        endEntry.setEffortLeftSum(60);
        endEntry.setDeltaOriginalEstimate(0);
        endEntry.setTimestamp(new LocalDate(2009,1,2));
    }
    
    @Test
    public void testGetTodaysStartValueWithScoping_noScope() {
        initializeEntriesForScopingTest();
        assertEquals(new Long(120),
                super.getTodaysStartValueWithScoping(startEntry, endEntry).getMinorUnits());
    }
    
    @Test
    public void testGetTodaysStartValueWithScoping_minusScope() {
        initializeEntriesForScopingTest();
        endEntry.setDeltaOriginalEstimate(-20);
        endEntry.setOriginalEstimateSum(100);
        assertEquals(new Long(100),
                super.getTodaysStartValueWithScoping(startEntry, endEntry).getMinorUnits());
    }
    
    @Test
    public void testGetTodaysStartValueWithScoping_plusScope() {
        initializeEntriesForScopingTest();
        endEntry.setDeltaOriginalEstimate(97);
        endEntry.setOriginalEstimateSum(217);
        assertEquals(new Long(217),
                super.getTodaysStartValueWithScoping(startEntry, endEntry).getMinorUnits());   
    }
    
    @Test
    public void testGetReferenceVelocityTimeSeries_noWeekends() {
        
        TimeSeriesDataItem startPoint = new TimeSeriesDataItem(new Second(
                startDate.toDate()), 100.0);
        TimeSeriesDataItem endPoint = new TimeSeriesDataItem(new Second(endDate
                .plusDays(1).toDate()), 0.0);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(false);
        
        replay(settingBusiness);
        TimeSeries actualSeries
            = super.getReferenceVelocityTimeSeries(startDate, endDate, originalEstimateSum);
        verify(settingBusiness);
        
        assertEquals("Reference series name incorrect",
                REFERENCE_SERIES_NAME, actualSeries.getKey());
        
        assertEquals("Reference start value not correct",
                startPoint.getValue(), actualSeries.getDataItem(0).getValue());
        assertEquals("Reference start instant not correct", startPoint
                .getPeriod(), actualSeries.getDataItem(0).getPeriod());
        assertEquals("Reference end value not correct", endPoint.getValue(),
                actualSeries.getDataItem(1).getValue());
        assertEquals("Reference end instant not correct", endPoint.getPeriod(),
                actualSeries.getDataItem(1).getPeriod());
        
    }
    
    @Test
    public void testGetReferenceVelocityTimeSeries_withWeekends() {
        
        TimeSeriesDataItem startPoint = new TimeSeriesDataItem(new Second(
                startDate.toDate()), 100.0);
        TimeSeriesDataItem endPoint = new TimeSeriesDataItem(
                new Second(endDate.toDateMidnight().plusDays(1).toDate()), 0.0);
        
        expect(settingBusiness.isWeekendsInBurndown()).andReturn(true);
        
        replay(settingBusiness);
        TimeSeries actualSeries
            = super.getReferenceVelocityTimeSeries(startDate, endDate, originalEstimateSum);
        verify(settingBusiness);
        
        assertEquals("Reference series name incorrect",
                REFERENCE_SERIES_NAME, actualSeries.getKey());
        
        assertEquals("Reference start value not correct",
                startPoint.getValue(), actualSeries.getDataItem(0).getValue());
        assertEquals("Reference start instant not correct", startPoint
                .getPeriod(), actualSeries.getDataItem(0).getPeriod());
        assertEquals("Reference end value not correct", endPoint.getValue(),
                actualSeries.getDataItem(actualSeries.getItemCount() - 1).getValue());
        assertEquals("Reference end instant not correct", endPoint.getPeriod(),
                actualSeries.getDataItem(actualSeries.getItemCount() - 1).getPeriod());
        
    }
    
    @Test
    public void testGetBurndownTimeSeries() {
        replay(iterationHistoryEntryBusiness);
        
        TimeSeries actualSeries = super.getBurndownTimeSeries(entriesList,
                startDate.toLocalDate(), endDate.toLocalDate());
        
        Second startInstant = new Second(startDate.plusDays(1).toDateMidnight().toDate());
        Second secondInstant = new Second(startDate.plusDays(2).toDateMidnight().toDate());
        Second endInstant = new Second(endDate.toDateMidnight().toDate());
        Second afterEndInstant = new Second(endDate.plusDays(1).toDateMidnight().toDate());
        
        
        assertEquals(ExactEstimateUtils.extractMajorUnits(new ExactEstimate(entry1.getEffortLeftSum())),
                actualSeries.getDataItem(startInstant).getValue());
        assertEquals(ExactEstimateUtils.extractMajorUnits(new ExactEstimate(entry2.getEffortLeftSum())),
                actualSeries.getDataItem(secondInstant).getValue());
        assertEquals(ExactEstimateUtils.extractMajorUnits(new ExactEstimate(entry2.getEffortLeftSum())),
                actualSeries.getDataItem(endInstant).getValue());
        
        assertNull(actualSeries.getDataItem(afterEndInstant));
        
        verify(iterationHistoryEntryBusiness);
    }
    
    @Test
    public void testGetBurndownTimeSeries_scoped() {
        DateTime startTime = new DateTime(2012, 7, 4, 12, 38, 12, 57);
        IterationHistoryEntry firstEntry = new IterationHistoryEntry();
        firstEntry.setTimestamp(startTime.toLocalDate());
        firstEntry.setOriginalEstimateSum(130);
        firstEntry.setDeltaOriginalEstimate(0);
        IterationHistoryEntry secondEntry = new IterationHistoryEntry();
        secondEntry.setTimestamp(startTime.plusDays(1).toLocalDate());
        secondEntry.setOriginalEstimateSum(156);
        secondEntry.setDeltaOriginalEstimate(26);
        IterationHistoryEntry thirdEntry = new IterationHistoryEntry();
        thirdEntry.setTimestamp(startTime.plusDays(2).toLocalDate());
        thirdEntry.setOriginalEstimateSum(88);
        thirdEntry.setDeltaOriginalEstimate(-68);
        
        List<IterationHistoryEntry> entries = Arrays.asList(firstEntry, secondEntry, thirdEntry);
        
        TimeSeries actualSeries = super.getBurndownTimeSeries(entries,
                startTime.toLocalDate(), startTime.toLocalDate().plusDays(5));
        
        assertEquals(10, actualSeries.getItemCount());
    }
    
    @Test
    public void testGetBurndownDataItemForDay() {
        TimeSeriesDataItem actualItem = super.getBurndownDataItemForDay(entry1);
        
        assertEquals(ExactEstimateUtils.extractMajorUnits(
                new ExactEstimate(entry1.getEffortLeftSum())),
                actualItem.getValue());
    }
    
    @Test
    public void testGetBurndownScopedDataItemForDay() {
        initializeEntriesForScopingTest();
        
        startEntry.setTimestamp(new LocalDate(2008, 4, 2));
        endEntry.setTimestamp(new LocalDate(2008, 4, 3));
        startEntry.setEffortLeftSum(128);
        endEntry.setDeltaOriginalEstimate(150);
        DateTime expectedTimestamp = new DateTime(2008, 4, 3, 0, 0, 2, 0);
        
        Pair<TimeSeriesDataItem, TimeSeriesDataItem> scopedEntries
            = getBurndownScopedDataItemForDay(startEntry, endEntry);
        TimeSeriesDataItem nullEntry = scopedEntries.getFirst();
        TimeSeriesDataItem actualEntry = scopedEntries.getSecond();
        ExactEstimate estimatedValue = new ExactEstimate(128 + 150);
        
        assertEquals(new Second(expectedTimestamp.minusSeconds(1).toDate()), nullEntry.getPeriod());
        assertEquals(null, nullEntry.getValue());
        assertEquals(new Second(expectedTimestamp.toDate()), actualEntry.getPeriod());
        assertEquals(ExactEstimateUtils.extractMajorUnits(estimatedValue), actualEntry.getValue());
    }
    
    @Test
    public void testIsScopingDone() {
        IterationHistoryEntry entryWithoutScoping = new IterationHistoryEntry();
        IterationHistoryEntry entryWithScoping = new IterationHistoryEntry();
        entryWithScoping.setDeltaOriginalEstimate(-200);
        
        assertFalse(super.isScopingDone(entryWithoutScoping));
        assertTrue(super.isScopingDone(entryWithScoping));
    }
    
    @Test
    public void testGetCurrentDayTimeSeries() {
        initializeEntriesForScopingTest();
        endEntry.setOriginalEstimateSum(endEntry.getOriginalEstimateSum() - 100);
        endEntry.setEffortLeftSum(endEntry.getEffortLeftSum() - 80);
                
        ExactEstimate expectedStartValue = new ExactEstimate(
                startEntry.getEffortLeftSum() + endEntry.getDeltaOriginalEstimate());
        ExactEstimate expectedEndvalue = new ExactEstimate(endEntry.getEffortLeftSum());
        
        TimeSeries actualSeries = super.getCurrentDayEffortLeftSeries(startEntry, endEntry);
        testSeriesStartAndEndCorrect(actualSeries, expectedStartValue, expectedEndvalue);
        
        assertEquals(CURRENT_DAY_EFFORT_LEFT_SERIES_NAME, actualSeries.getKey());
        assertEquals(ExactEstimateUtils.extractMajorUnits(expectedStartValue),
                actualSeries.getDataItem(0).getValue());
    }
    
    
    @Test
    public void testGetScopingTimeSeries() {
        DateTime startTime = new DateTime(2012, 7, 4, 12, 38, 12, 57);
        IterationHistoryEntry firstEntry = new IterationHistoryEntry();
        firstEntry.setTimestamp(startTime.toLocalDate());
        firstEntry.setOriginalEstimateSum(130);
        firstEntry.setDeltaOriginalEstimate(0);
        IterationHistoryEntry secondEntry = new IterationHistoryEntry();
        secondEntry.setTimestamp(startTime.plusDays(1).toLocalDate());
        secondEntry.setOriginalEstimateSum(156);
        secondEntry.setDeltaOriginalEstimate(26);
        IterationHistoryEntry thirdEntry = new IterationHistoryEntry();
        thirdEntry.setTimestamp(startTime.plusDays(2).toLocalDate());
        thirdEntry.setOriginalEstimateSum(88);
        thirdEntry.setDeltaOriginalEstimate(-68);
        
        List<IterationHistoryEntry> entries = Arrays.asList(firstEntry, secondEntry, thirdEntry);
        
        TimeSeries actualSeries = super.getScopingTimeSeries(entries,
                startDate.toLocalDate(), startTime.plusDays(5).toLocalDate());
        
        assertEquals(SCOPING_SERIES_NAME, actualSeries.getKey());
        assertEquals(6, actualSeries.getItemCount());
    }
    
    
    @Test
    public void testGetSeriesByStartAndEndPoints() {
        ExactEstimate value1 = new ExactEstimate(entry1.getEffortLeftSum());
        ExactEstimate value2 = new ExactEstimate(entry2.getEffortLeftSum());
        TimeSeries actualSeries = super.getSeriesByStartAndEndPoints(
                "Test series",
                new DateTime(entry1.getTimestamp().toDateMidnight().toDateTime()), value1,
                new DateTime(entry2.getTimestamp().toDateMidnight().toDateTime()), value2);
        
        testSeriesStartAndEndCorrect(actualSeries, value1, value2);
        assertEquals("Test series", actualSeries.getKey());
    }
    
    @Test
    public void testGetScopeSeriesDataItem() {
        initializeEntriesForScopingTest();
        startEntry.setTimestamp(new LocalDate(2008, 4, 2));
        endEntry.setTimestamp(new LocalDate(2008, 4, 3));
        startEntry.setEffortLeftSum(128);
        endEntry.setDeltaOriginalEstimate(150);
        DateTime expectedTimestamp = new DateTime(2008, 4, 3, 0, 0, 0, 0);
        
        List<TimeSeriesDataItem> actualItems
            = super.getScopeSeriesDataItems(startEntry, endEntry);
        
        TimeSeriesDataItem firstItem = actualItems.get(0);
        TimeSeriesDataItem secondItem = actualItems.get(1);
        TimeSeriesDataItem nullItem = actualItems.get(2);
        ExactEstimate expectedFirstValue = new ExactEstimate(128);
        ExactEstimate expectedSecondValue = new ExactEstimate(128 + 150);
        
        assertEquals(new Second(expectedTimestamp.toDate()), firstItem.getPeriod());
        assertEquals(ExactEstimateUtils.extractMajorUnits(expectedFirstValue), firstItem.getValue());
        assertEquals(new Second(expectedTimestamp.plusSeconds(2).toDate()), secondItem.getPeriod());
        assertEquals(ExactEstimateUtils.extractMajorUnits(expectedSecondValue), secondItem.getValue());
        assertEquals(new Second(expectedTimestamp.plusSeconds(3).toDate()), nullItem.getPeriod());
        assertNull(nullItem.getValue());
    }

    private void testSeriesStartAndEndCorrect(TimeSeries series, ExactEstimate value1, ExactEstimate value2) {
        assertEquals(ExactEstimateUtils.extractMajorUnits(value1), series.getDataItem(0).getValue());
        assertEquals(ExactEstimateUtils.extractMajorUnits(value2), series.getDataItem(1).getValue());
    }
    
    @Test
    public void testAddTimeSeriesItem() {
        ExactEstimate expectedValue = new ExactEstimate(120);
        DateTime expectedInstant = new DateTime(2010, 1, 15, 12, 56, 42, 0);
        
        TimeSeries timeSeries = createMock(TimeSeries.class);
        
        expect(timeSeries.addOrUpdate(
                new Second(new DateTime(2010, 1, 15, 0,0, 0, 0).toDate()), 2))
                .andReturn(new TimeSeriesDataItem(new Second(), 0));
        
        replay(timeSeries);
        
        this.addTimeSeriesItem(expectedInstant, expectedValue, timeSeries);        
        
        verify(timeSeries);
        

        
    }
    
}
