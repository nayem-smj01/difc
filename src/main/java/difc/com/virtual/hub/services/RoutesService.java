package difc.com.virtual.hub.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ortools.constraintsolver.*;
import difc.com.virtual.hub.domain.journey.optimalpath.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class RoutesService {

    @Value("${google.map.api.key}")
    private String googleMapKey;

    public OptimalPath getOptimalPath(RouteInfo routeInfo) {

        //1. create google route matrix api request
        DistanceMatrixRequest distanceMatrixRequest = constructDistanceMatrixRequest(routeInfo);
        //2. Call api

        List<DistancePoint> responses = getDistances(distanceMatrixRequest);
        //3. a.construct distance matrix from response
        //3. b.reset all incoming-distances to origin to 0
        long[][] distanceMatrix = constructDistanceMatrix(responses);
        //4. get shortest path from distance matrix using google OR-tools tsp implementation
        List<Integer> orderOfWayPoints = getShortestPath(distanceMatrix);
        //5. construct response body
        return constructOptimalPath(orderOfWayPoints, routeInfo);

    }

    DistanceMatrixRequest constructDistanceMatrixRequest(RouteInfo routeInfo) {
        List<Origins> originsList = new ArrayList<>();
        //origin is set to index 0
        originsList.add(getOrigin(routeInfo.getOrigin()));
        for (int i = 0; i < routeInfo.getWayPoints().size(); i++)
            originsList.add(getOrigin(routeInfo.getWayPoints().get(i)));

        //destinations are the same as origins here

        List<Destinations> destinationsList = new ArrayList<>();
        destinationsList.add(getDestination(routeInfo.getOrigin()));
        for (int i = 0; i < routeInfo.getWayPoints().size(); i++)
            destinationsList.add(getDestination(routeInfo.getWayPoints().get(i)));

        return DistanceMatrixRequest.builder()
                .travelMode("DRIVE")
                .origins(originsList)
                .destinations(destinationsList)
                .build();

    }

    Origins getOrigin(LocationPoint locationPoint) {
        return Origins.builder().waypoint(Waypoint.builder().location(Location.builder().latLng(LatLng.builder().latitude(locationPoint.getLat()).longitude(locationPoint.getLng()).build()).build()).build()).build();
    }

    Destinations getDestination(LocationPoint locationPoint) {
        return Destinations.builder().waypoint(Waypoint.builder().location(Location.builder().latLng(LatLng.builder().latitude(locationPoint.getLat()).longitude(locationPoint.getLng()).build()).build()).build()).build();
    }

    List<DistancePoint> getDistances(DistanceMatrixRequest distanceMatrixRequest) {
        try {
            String routeMatrixUri = "https://routes.googleapis.com/distanceMatrix/v2:computeRouteMatrix";
            // create headers
            HttpHeaders headers = new HttpHeaders();
            // set `content-type` header
            headers.setContentType(MediaType.APPLICATION_JSON);
            // set `accept` header
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-Goog-Api-Key", googleMapKey);
            headers.set("X-Goog-FieldMask", "originIndex,destinationIndex,duration,distanceMeters");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(distanceMatrixRequest);
            HttpEntity<String> request =
                    new HttpEntity<String>(jsonString, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntityStr = restTemplate.
                    postForEntity(routeMatrixUri, request, String.class);
            String responseBody = responseEntityStr.getBody();
            ObjectMapper mapper1 = new ObjectMapper();
            return mapper1.readValue(responseBody, new TypeReference<List<DistancePoint>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    long[][] constructDistanceMatrix(List<DistancePoint> distancePoints) {
        int arraySize = (int) Math.sqrt(distancePoints.size());
        long[][] distanceMatrix = new long[arraySize][arraySize];
        for (int i = 0; i < distancePoints.size(); i++) {
            DistancePoint distancePoint = distancePoints.get(i);
            distanceMatrix[distancePoint.getOriginIndex()][distancePoint.getDestinationIndex()] = (int) distancePoint.getDistanceMeters();
        }
        //reset in-going arcs to zero
        for(int i=0; i<arraySize;i++)
            distanceMatrix[i][0] = 0;

        return distanceMatrix;
    }

    List<Integer> getOrder(
            RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
        long routeDistance = 0;
        List<Integer> routeOrder = new ArrayList<Integer>();
        long index = routing.start(0);
        while (!routing.isEnd(index)) {
            routeOrder.add(manager.indexToNode(index));
            long previousIndex = index;
            index = solution.value(routing.nextVar(index));
            routeDistance += routing.getArcCostForVehicle(previousIndex, index, 0);
        }
        return routeOrder;
    }

    List<Integer> getShortestPath(long[][] distanceMatrix) {
        {
            final DataModel data = DataModel.builder()
                    .depot(0)
                    .vehicleNumber(1)
                    .distanceMatrix(distanceMatrix)
                    .build();
            // Create Routing Index Manager
            RoutingIndexManager manager =
                    new RoutingIndexManager(data.distanceMatrix.length, data.vehicleNumber, data.depot);

            // Create Routing Model.
            RoutingModel routing = new RoutingModel(manager);

            // Create and register a transit callback.
            final int transitCallbackIndex =
                    routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                        // Convert from routing variable Index to user NodeIndex.
                        int fromNode = manager.indexToNode(fromIndex);
                        int toNode = manager.indexToNode(toIndex);
                        return data.distanceMatrix[fromNode][toNode];
                    });

            // Define cost of each arc.
            routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

            // Setting first solution heuristic.
            RoutingSearchParameters searchParameters =
                    main.defaultRoutingSearchParameters()
                            .toBuilder()
                            .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                            .build();

            // Solve the problem.
            Assignment solution = routing.solveWithParameters(searchParameters);
            return getOrder(routing, manager, solution);
        }
    }

    OptimalPath constructOptimalPath(List<Integer> orderOfWayPoints, RouteInfo routeInfo) {
        List<LocationPoint> wayPoints = new ArrayList<>();
        int size = orderOfWayPoints.size();
        //i=0 is origin i = size-1 is destination
        for(int i=1;i<size-1;i++ )
            wayPoints.add(routeInfo.getWayPoints().get(orderOfWayPoints.get(i) - 1));

        LocationPoint destination = routeInfo.getWayPoints().get(orderOfWayPoints.get(size-1) - 1);
        OptimalPath path
        = OptimalPath.builder()
                .origin(routeInfo.getOrigin())
                .destination(destination)
                .wayPoints(wayPoints)
                .build();
        log.info("OptimalPath::"+ path);
        return path;
    }
}
