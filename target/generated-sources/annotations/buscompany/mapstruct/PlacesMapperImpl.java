package buscompany.mapstruct;

import buscompany.dto.request.PlaceDtoRequest;
import buscompany.dto.response.PlaceDtoResponse;
import buscompany.model.Place;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-09T17:24:21+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class PlacesMapperImpl implements PlacesMapper {

    @Override
    public Place placeDtoRequestToPlace(PlaceDtoRequest placeDtoRequest) {
        if ( placeDtoRequest == null ) {
            return null;
        }

        Place place = new Place();

        place.setOrderID( placeDtoRequest.getOrderID() );
        place.setLastName( placeDtoRequest.getLastName() );
        place.setFirstName( placeDtoRequest.getFirstName() );
        place.setPassport( placeDtoRequest.getPassport() );
        place.setPlace( placeDtoRequest.getPlace() );

        return place;
    }

    @Override
    public PlaceDtoResponse placeToPlaceDtoResponse(Place place) {
        if ( place == null ) {
            return null;
        }

        PlaceDtoResponse placeDtoResponse = new PlaceDtoResponse();

        placeDtoResponse.setOrderID( place.getOrderID() );
        placeDtoResponse.setTicket( place.getTicket() );
        placeDtoResponse.setLastName( place.getLastName() );
        placeDtoResponse.setFirstName( place.getFirstName() );
        placeDtoResponse.setPassport( place.getPassport() );
        placeDtoResponse.setPlace( place.getPlace() );

        return placeDtoResponse;
    }
}
