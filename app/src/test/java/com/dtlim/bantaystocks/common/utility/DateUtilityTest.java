package com.dtlim.bantaystocks.common.utility;

import com.dtlim.bantaystocks.common.utility.DateUtility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dale on 7/14/16.
 */
public class DateUtilityTest {
    @Test
    public void validateParseUnixTimestampToDisplay() throws Exception {
        assertEquals("07/14 02:53PM", DateUtility.parseUnixTimestampToDisplay("1468479189000"));
    }

    @Test
    public void validateApiToUnixTimestamp() throws Exception {
        // TODO figure out how to change Android to not use Java 1.6
        //assertEquals("1468488909000", DateUtility.parseApiToUnixTimestamp("2016-07-14T17:35:09+08:00"));
    }
}
