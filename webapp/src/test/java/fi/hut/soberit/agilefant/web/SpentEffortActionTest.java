package fi.hut.soberit.agilefant.web;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.transfer.DailySpentEffort;
public class SpentEffortActionTest {

    private SpentEffortAction testable;
    private HourEntryBusiness heBusiness;
    
    @Before
    public void setUp() {
        testable = new SpentEffortAction();
        heBusiness = createMock(HourEntryBusiness.class);
        testable.setHourEntryBusiness(heBusiness);
        testable.setServerTimeZone(DateTimeZone.UTC);
    }
    @Test
    public void testInitializeWeekSelection() {
        DateTime middle = new DateTime(2009,6,1,0,0,0,0);
        final int firstWeek = 23 - SpentEffortAction.WEEKS_IN_WEEK_SELECTION/2;
        final int lastWeek = 23 + SpentEffortAction.WEEKS_IN_WEEK_SELECTION/2;
        
        assertEquals(0, testable.getWeeks().size());
        
        testable.initializeWeekSelection(middle);
        List<LocalDate> actual = testable.getWeeks();
        assertNotNull(actual);
        assertEquals(SpentEffortAction.WEEKS_IN_WEEK_SELECTION, actual.size());
        assertEquals(firstWeek, actual.get(0).getWeekOfWeekyear());
        assertEquals(lastWeek, actual.get(SpentEffortAction.WEEKS_IN_WEEK_SELECTION-1).getWeekOfWeekyear());
        
    }
    @Test
    public void getSelectedDate() {
        final int initWeek = 23;
        final int initYear = 2009;
        testable.setWeek(initWeek);
        testable.setYear(initYear);
        DateTime actual = testable.getSelectedDate();
        assertEquals(initYear, actual.getYear());
        assertEquals(initWeek, actual.getWeekOfWeekyear());
        assertEquals(initWeek + 1, testable.getNextWeek().getWeekOfWeekyear());
        assertEquals(initYear, testable.getNextWeek().getYear());
        assertEquals(initWeek - 1, testable.getPrevWeek().getWeekOfWeekyear());
        assertEquals(initYear, testable.getPrevWeek().getYear());
        
    }
    
    @Test
    public void getSelectedDate_currentDate() {
        LocalDate current = new LocalDate();
        int currentWeek = current.getWeekOfWeekyear();
        int nextWeek = current.plusWeeks(1).getWeekOfWeekyear();
        int previousWeek = current.minusWeeks(1).getWeekOfWeekyear();
        int nextWeekYear = current.withDayOfWeek(DateTimeConstants.MONDAY).plusWeeks(1).getYear();
        int previousWeekYear = current.minusWeeks(1).getYear();
        int currentYear = current.getYear();
        DateTime actual = testable.getSelectedDate();
        // If Monday is on or after 27 Dec, then 1 Jan of next year is in the same week
        if (actual.getMonthOfYear() == 12 && actual.getDayOfMonth() >= 27)
            currentYear--;
        assertEquals(currentYear, actual.getYear());
        assertEquals(currentWeek, actual.getWeekOfWeekyear());
        assertEquals(nextWeek, testable.getNextWeek().getWeekOfWeekyear());
        assertEquals(nextWeekYear, testable.getNextWeek().getYear());
        assertEquals(previousWeek, testable.getPrevWeek().getWeekOfWeekyear());
        assertEquals(previousWeekYear, testable.getPrevWeek().getYear());
    }
    
    @Test
    public void testGetDaySumsByWeek() {
        DateTime startDate = new DateTime(2009,6,1,0,0,0,0);
        List<DailySpentEffort> entries = Collections.emptyList();
        
        expect(heBusiness.getDailySpentEffortByWeek(startDate.toLocalDate(), 11, testable.getUserHourTimeZone(), testable.getUserMinuteTimeZone(), DateTimeZone.UTC)).andReturn(entries);
        expect(heBusiness.calculateWeekSum(startDate.toLocalDate(), 11, testable.getUserHourTimeZone(), testable.getUserMinuteTimeZone(), DateTimeZone.UTC)).andReturn(0L);
        replay(heBusiness);
        testable.setYear(2009);
        testable.setWeek(23);
        testable.setUserId(11);
        assertEquals(Action.SUCCESS, testable.getDaySumsByWeek());
        //check that initializeWeekSelection and getSelectedDate have been called
        assertEquals(24, testable.getNextWeek().getWeekOfWeekyear());
        assertEquals(22, testable.getPrevWeek().getWeekOfWeekyear());
        assertEquals(SpentEffortAction.WEEKS_IN_WEEK_SELECTION, testable.getWeeks().size());
        verify(heBusiness);
    }
    @Test
    public void testGetHourEntriesByUserAndDay() {
        testable.setUserId(42);
        testable.setYear(2009);
        testable.setDay(153);
        LocalDate day = new DateTime(2009,6,2,0,0,0,0).toLocalDate();
        expect(heBusiness.getEntriesByUserAndDay(day, 42,testable.getUserHourTimeZone(),testable.getUserMinuteTimeZone(), DateTimeZone.UTC)).andReturn(null);
        replay(heBusiness);
        assertEquals(Action.SUCCESS, testable.getHourEntriesByUserAndDay());
        assertEquals(null, testable.getEffortEntries());
        verify(heBusiness);
    }
}
