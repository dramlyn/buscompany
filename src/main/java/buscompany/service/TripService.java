package buscompany.service;

import buscompany.daoimpl.BusDaoImpl;
import buscompany.daoimpl.TripDaoImpl;
import buscompany.dto.request.TripDtoRequest;
import buscompany.dto.request.UpdateTripDtoRequest;
import buscompany.dto.response.TripDtoResponse;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mapstruct.TripMapper;
import buscompany.model.Bus;
import buscompany.model.Trip;
import buscompany.utils.CookieUtils;
import buscompany.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TripService {

    private TripDaoImpl tripDao;


    private ServiceUtils serviceUtils;

    private CookieUtils cookieUtils;

    private BusDaoImpl busDao;

    public TripDtoResponse addTrip(TripDtoRequest tripDtoRequest, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }

        serviceUtils.validateTrip(tripDtoRequest);

        Bus bus = busDao.getBusByBusName(tripDtoRequest.getBusName());
        serviceUtils.validateBus(bus);
        int placeCount = bus.getPlaceCount();

        Trip trip = TripMapper.INSTANCE.tripDtoRequestToTrip(tripDtoRequest, placeCount);

        if (tripDtoRequest.getSchedule() == null) {
            tripDao.addTrip(trip);
            return TripMapper.INSTANCE.tripToTripDtoResponse(trip);
        }

        serviceUtils.makeDatesBySchedule(trip);
        if (trip.getDates().isEmpty()) {
            throw new ServerException(ServerErrorCode.WRONG_SCHEDULE, "schedule", "Can't make dates by schedule");
        }

        tripDao.addTrip(trip);
        return TripMapper.INSTANCE.tripToTripDtoResponse(trip);
    }

    public TripDtoResponse updateTrip(UpdateTripDtoRequest tripDtoRequest, int tripID, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }

        Trip trip = tripDao.selectTripById(tripID);
        serviceUtils.validateTrip(tripDtoRequest, trip);


        String busName = tripDtoRequest.getBusName();
        if(busName != null) {
            Bus bus = busDao.getBusByBusName(busName);
            serviceUtils.validateBus(bus);
            trip.setBus(bus);
        }

        serviceUtils.updateTrip(trip, tripDtoRequest);
        System.out.println(trip);

        if (tripDtoRequest.getDates() != null || tripDtoRequest.getSchedule() != null) {

            if (tripDtoRequest.getSchedule() == null) {
                tripDao.updateTrip(trip);
                return TripMapper.INSTANCE.tripToTripDtoResponse(tripDao.selectTripById(tripID));
            }

            serviceUtils.makeDatesBySchedule(trip);
            if (trip.getDates().isEmpty()) {
                throw new ServerException(ServerErrorCode.WRONG_SCHEDULE, "schedule", "Can't make dates by schedule");
            }
        }

        tripDao.updateTrip(trip);
        return TripMapper.INSTANCE.tripToTripDtoResponse(tripDao.selectTripById(tripID));
    }

    public void deleteTrip(int tripID, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }

        Trip checkTrip = tripDao.selectTripById(tripID);
        serviceUtils.validateTrip(checkTrip);

        if (checkTrip.isApproved()) {
            throw new ServerException(ServerErrorCode.APPROVED_TRIP, "approved", "You can't delete trip. This trip is approved.");
        }

        tripDao.deleteTrip(tripID);
    }

    public TripDtoResponse getTripByID(int tripID, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }

        Trip trip = tripDao.selectTripById(tripID);
        serviceUtils.validateTrip(trip);

        return TripMapper.INSTANCE.tripToTripDtoResponse(trip);
    }

    public TripDtoResponse approveTrip(int tripID, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }

        Trip trip = tripDao.selectTripById(tripID);
        serviceUtils.validateTrip(trip);

        tripDao.approveTrip(tripID);
        return TripMapper.INSTANCE.tripToTripDtoResponse(tripDao.selectTripById(tripID));
    }

    public List<TripDtoResponse> getTripList(String fromStation, String toStation,
                                             String busName, String fromDate,
                                             String toDate, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);
        if (serviceUtils.isAdmin(userID)) {
            List<Trip> tripList = serviceUtils.makeTripList(tripDao.getTripList(
                    fromStation, toStation, busName, false), fromDate, toDate);
            return TripMapper.INSTANCE.tripListToTripDtoResponseList(tripList);
        }

        List<Trip> tripList = serviceUtils.makeTripList(tripDao.getTripList(
                fromStation, toStation, busName, true), fromDate, toDate);
        return TripMapper.INSTANCE.tripListToTripDtoResponseList(tripList);
    }
}

