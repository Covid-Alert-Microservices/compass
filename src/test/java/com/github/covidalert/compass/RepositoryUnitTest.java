package com.github.covidalert.compass;

import com.github.covidalert.compass.models.Geolocation;
import com.github.covidalert.compass.repositories.GeolocationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class RepositoryUnitTest {

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

    final double deltaMeter = 0.00001; // Approximately 1 meter around Montpellier
    final double deltaKilometer = 1000 * deltaMeter;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GeolocationRepository geolocationRepository;


    @BeforeEach
    public void initialize(){
        entityManager.clear();
    }

    @Test
    public void givenOneGeolocation_whenGetPotentialInfectedGeolocations_shouldNotReturnItBecauseTooFar() {
        // given
        Geolocation geolocation = new Geolocation(axelId, latitudeBeach, longitudeBeach, heightOClock);
        entityManager.persist(geolocation);
        entityManager.flush();

        // when
        List<Geolocation> geolocations = geolocationRepository.potentialInfectedGeolocations(florentId, latitudePolytech, longitudePolytech, heightOClock);

        // then
        assertThat(geolocations).isEmpty();
    }

    @Test
    public void givenOneGeolocation_whenGetPotentialInfectedGeolocations_shouldNotReturnItBecauseTooEarly() {
        // given
        Geolocation geolocation = new Geolocation(axelId, latitudePolytech, longitudePolytech, heightOClock);
        entityManager.persist(geolocation);
        entityManager.flush();

        // when
        List<Geolocation> geolocations = geolocationRepository.potentialInfectedGeolocations(florentId, latitudePolytech, longitudePolytech, nineOClock);

        // then
        assertThat(geolocations).isEmpty();
    }

    @Test
    public void givenOneGeolocation_whenGetPotentialInfectedGeolocations_shouldReturnIt() {
        // given
        Geolocation geolocation = new Geolocation(axelId, latitudeBeach, longitudeBeach, heightOClock);
        entityManager.persist(geolocation);
        entityManager.flush();

        // when
        List<Geolocation> geolocations = geolocationRepository.potentialInfectedGeolocations(florentId, latitudeBeach + deltaMeter, longitudeBeach, heightOClock);

        // then
        assertThat(geolocations).hasSize(1).contains(geolocation);
    }

    @Test
    public void givenFourGeolocation_whenGetPotentialInfectedGeolocations_shouldReturnThreeOfThemCauseIdIsSame() {
        // given
        Geolocation geolocation1 = new Geolocation(axelId, latitudeBeach, longitudeBeach, heightOClock);
        Geolocation geolocation2 = new Geolocation(florentId, latitudeBeach, longitudeBeach, nineOClock);
        Geolocation geolocation3 = new Geolocation(kevenId, latitudePolytech, longitudePolytech, heightOClock);
        Geolocation geolocation4 = new Geolocation(axelId, latitudeBeach + deltaMeter, longitudeBeach + deltaMeter, nineOClock);
        entityManager.persist(geolocation1);
        entityManager.persist(geolocation2);
        entityManager.persist(geolocation3);
        entityManager.persist(geolocation4);
        entityManager.flush();

        // when
        List<Geolocation> geolocations = geolocationRepository.potentialInfectedGeolocations(kevenId, latitudeBeach, longitudeBeach, heightOClock);

        // then (does not contain keven's geolocation cause ids are same)
        assertThat(geolocations).hasSize(3).contains(geolocation1).contains(geolocation2).contains(geolocation4);
    }

    @Test
    public void givenTwoGeolocation_whenGetPotentialInfectedGeolocations_shouldReturnOneOfThemCauseOtherOneIsTooFar() {
        // given
        Geolocation geolocation1 = new Geolocation(axelId, latitudePolytech, longitudePolytech, heightOClock);
        Geolocation geolocation2 = new Geolocation(florentId, latitudePolytech + deltaKilometer * 10, longitudePolytech, heightOClock);
        entityManager.persist(geolocation1);
        entityManager.persist(geolocation2);
        entityManager.flush();

        // when
        List<Geolocation> geolocations = geolocationRepository.potentialInfectedGeolocations(kevenId, latitudePolytech, longitudePolytech, heightOClock);

        // then (does not contain florent's geolocation cause diff lat/long > 0.1)
        assertThat(geolocations).hasSize(1).contains(geolocation1);
    }

    @Test
    public void givenFiveGeolocation_whenGetPotentialInfectedGeolocations_shouldReturnThreeOfThemCauseOtherAreTooFarOrTooEarly() {
        // given
        Geolocation geolocation1 = new Geolocation(axelId, latitudePolytech, longitudePolytech, nineOClock); //Yes
        Geolocation geolocation2 = new Geolocation(florentId, latitudePolytech + deltaKilometer * 10, longitudePolytech, nineOClock); //No
        Geolocation geolocation3 = new Geolocation(kevenId, latitudePolytech , longitudePolytech, tenOClock); //Yes
        Geolocation geolocation4 = new Geolocation(florentId, latitudePolytech , longitudePolytech, tenOClock); //Yes
        Geolocation geolocation5 = new Geolocation(kevenId, latitudePolytech , longitudePolytech, heightOClock); //No
        entityManager.persist(geolocation1);
        entityManager.persist(geolocation2);
        entityManager.persist(geolocation3);
        entityManager.persist(geolocation4);
        entityManager.persist(geolocation5);
        entityManager.flush();

        // when
        List<Geolocation> geolocations = geolocationRepository.potentialInfectedGeolocations("someID", latitudePolytech, longitudePolytech, nineOClock);

        // then
        assertThat(geolocations).hasSize(3).contains(geolocation1).contains(geolocation3).contains(geolocation4);
    }

    @Test
    public void givenNineGeolocation_whenGetPotentialInfectedGeolocations_shouldReturnFourOfThemCauseOtherAreTooFarOrTooEarly() {
        // given
        Geolocation geolocation1 = new Geolocation(axelId, latitudePolytech + deltaKilometer * 10, longitudePolytech + deltaKilometer * 10, heightOClock); //No
        Geolocation geolocation2 = new Geolocation(axelId, latitudePolytech + deltaMeter, longitudePolytech, nineOClock); //Yes
        Geolocation geolocation3 = new Geolocation(axelId, latitudePolytech, longitudePolytech + deltaKilometer, tenOClock); //Yes

        Geolocation geolocation4 = new Geolocation(florentId, latitudePolytech, longitudePolytech, heightOClock); //Yes
        Geolocation geolocation5 = new Geolocation(florentId, latitudePolytech, longitudePolytech + deltaKilometer * 15, nineOClock); //No
        Geolocation geolocation6 = new Geolocation(florentId, latitudePolytech + deltaKilometer * 20, longitudePolytech + deltaKilometer * 15, tenOClock); //No

        Geolocation geolocation7 = new Geolocation(kevenId, latitudePolytech + deltaKilometer * 50, longitudePolytech + deltaKilometer * 15, heightOClock); //No
        Geolocation geolocation8 = new Geolocation(kevenId, latitudePolytech, longitudePolytech + deltaKilometer * 75, nineOClock); //No
        Geolocation geolocation9 = new Geolocation(kevenId, latitudePolytech, longitudePolytech, tenOClock); //Yes

        entityManager.persist(geolocation1);
        entityManager.persist(geolocation2);
        entityManager.persist(geolocation3);
        entityManager.persist(geolocation4);
        entityManager.persist(geolocation5);
        entityManager.persist(geolocation6);
        entityManager.persist(geolocation7);
        entityManager.persist(geolocation8);
        entityManager.persist(geolocation9);
        entityManager.flush();

        // when
        List<Geolocation> geolocations = geolocationRepository.potentialInfectedGeolocations("someID", latitudePolytech, longitudePolytech, heightOClock);

        // then
        assertThat(geolocations).hasSize(4).contains(geolocation2).contains(geolocation3).contains(geolocation4).contains(geolocation9);
    }

}
