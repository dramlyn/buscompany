package buscompany.dao;

import buscompany.dto.response.BusDtoResponse;
import buscompany.model.Bus;

import java.util.List;

public interface BusDao {

    List<Bus> getAllBusBrands();

    Bus getBusByBusName(String busName);

    Integer getPlaceCountByBusName(String busName);

}
