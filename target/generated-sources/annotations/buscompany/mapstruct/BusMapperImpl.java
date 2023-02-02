package buscompany.mapstruct;

import buscompany.dto.request.BusDtoRequest;
import buscompany.dto.response.BusDtoResponse;
import buscompany.model.Bus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-09T17:24:21+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class BusMapperImpl implements BusMapper {

    @Override
    public Bus busNameToBus(String busName) {
        if ( busName == null ) {
            return null;
        }

        Bus bus = new Bus();

        bus.setBusName( busName );

        bus.setPlaceCount( 0 );

        return bus;
    }

    @Override
    public BusDtoResponse busToBusDtoResponse(Bus bus) {
        if ( bus == null ) {
            return null;
        }

        BusDtoResponse busDtoResponse = new BusDtoResponse();

        busDtoResponse.setBusName( bus.getBusName() );
        busDtoResponse.setPlaceCount( bus.getPlaceCount() );

        return busDtoResponse;
    }

    @Override
    public List<BusDtoResponse> busListToBusDtoResponseList(List<Bus> busList) {
        if ( busList == null ) {
            return null;
        }

        List<BusDtoResponse> list = new ArrayList<BusDtoResponse>( busList.size() );
        for ( Bus bus : busList ) {
            list.add( busToBusDtoResponse( bus ) );
        }

        return list;
    }

    @Override
    public Bus busDtoRequestToBus(BusDtoRequest busDtoRequest) {
        if ( busDtoRequest == null ) {
            return null;
        }

        Bus bus = new Bus();

        bus.setBusName( busDtoRequest.getBusName() );
        bus.setPlaceCount( busDtoRequest.getPlaceCount() );

        return bus;
    }
}
