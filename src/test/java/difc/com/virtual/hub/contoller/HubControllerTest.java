package difc.com.virtual.hub.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import difc.com.virtual.hub.domain.journey.optimalpath.LocationPoint;
import difc.com.virtual.hub.domain.journey.optimalpath.OptimalPath;
import difc.com.virtual.hub.domain.journey.optimalpath.RouteInfo;
import difc.com.virtual.hub.services.JourneyServices;
import difc.com.virtual.hub.services.RoutesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HubController.class)
class HubControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RoutesService routesService;
    @MockBean
    private JourneyServices journeyServices;

    @Test
    void shouldGetShortestRouteForGivenLocations() throws Exception {
        LocationPoint place1 = new LocationPoint(12.34, 23.12, "Place 1");
        LocationPoint place2 = new LocationPoint(32.34, 29.12, "Place 2");
        LocationPoint place3 = new LocationPoint(52.34, 35.12, "Place 3");
        LocationPoint place4 = new LocationPoint(42.34, 27.12, "Place 4");

        RouteInfo routeInfo = new RouteInfo(place1, List.of(place2, place3, place4));

        mockMvc
                .perform(
                        post("/journey/optimal-path")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(routeInfo))
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldGiveBadRequestWhenRequestBodyIsMissing() throws Exception {
        LocationPoint place1 = new LocationPoint(12.34, 23.12, "Place 1");
        LocationPoint place2 = new LocationPoint(32.34, 29.12, "Place 2");
        LocationPoint place3 = new LocationPoint(52.34, 35.12, "Place 3");
        LocationPoint place4 = new LocationPoint(42.34, 27.12, "Place 4");

        RouteInfo routeInfo = new RouteInfo(null, List.of(place2, place3, place4));

        OptimalPath expectedPath = new OptimalPath(place1, place3, List.of(place1, place4, place1, place3));
        when(routesService.getOptimalPath(any())).thenReturn(expectedPath);

        MvcResult mvcResult = mockMvc
                .perform(
                        post("/journey/optimal-path")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), is(""));
        verify(routesService, never()).getOptimalPath(any());
    }

    private String toJson(RouteInfo routeInfo) throws JsonProcessingException {
        return objectMapper.writeValueAsString(routeInfo);
    }
}