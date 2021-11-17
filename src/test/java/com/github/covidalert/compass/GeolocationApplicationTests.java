package com.github.covidalert.compass;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class GeolocationApplicationTests
{

    final String axelId = "axel";
    final String florentId = "florent";
    final String kevenId = "keven";

    final Long heightOClock = 1636354800000L;
    final Long nineOClock = 1636358400000L;
    final Long tenOClock = 1636362000000L;

    final double latitudePolytech = 43.632719969358426;
    final double longitudePolytech = 3.8625356202522725;

    final double latitudeBeach = 43.55740660616729;
    final double longitudeBeach = 4.047099377730318;

    final double delta = 0.00001; // Approximately 1 meter around Montpellier

    @Test
    public void dummyTest() {}

    // Given a new position for user X, insert correctly link(s) into the graph

}
