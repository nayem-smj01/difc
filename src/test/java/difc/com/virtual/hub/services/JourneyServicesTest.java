package difc.com.virtual.hub.services;

import difc.com.virtual.hub.domain.journey.Journey;
import difc.com.virtual.hub.domain.journey.JourneyResponse;
import difc.com.virtual.hub.domain.journey.Location;
import difc.com.virtual.hub.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JourneyServicesTest {
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private StorageService s3Service;
    private JourneyServices journeyServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        journeyServices = new JourneyServices(routeRepository, s3Service);
    }

//    @Test
//    void shouldSaveImageToS3AndRouteToDatabase() {
//        MockMultipartFile file
//                = new MockMultipartFile("temp", "XYZ".getBytes());
//
//        Location place1 = new Location("23.4", "33.1", "Place1");
//        Location place2 = new Location("43.4", "39.1", "Place2");
//        Location place3 = new Location("27.4", "35.1", "Place3");
//        Location place4 = new Location("33.4", "37.1", "Place4");
//        Journey optimalJourney = new Journey(place1, place2, new ArrayList<>(List.of(place3, place4)));
//        String url = "http://s3.com/file2";
//        JourneyResponse expectedJourney = new JourneyResponse("middleEast", optimalJourney, url);
//
//        when(s3Service.uploadFile(any())).thenReturn(url);
//        when(routeRepository.save(any())).thenReturn(expectedJourney);
//
//        JourneyResponse journeyResponse = journeyServices.saveJourney(optimalJourney, file);
//
//        assertThat(journeyResponse, is(expectedJourney));
//        verify(s3Service, times(1)).uploadFile(file);
//        verify(routeRepository, times(1)).save(expectedJourney);
//    }
}
