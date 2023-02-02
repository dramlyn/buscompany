package buscompany.mapstruct;

import buscompany.dto.request.BusDtoRequest;
import buscompany.dto.response.BusDtoResponse;
import buscompany.model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BusMapper {

    BusMapper INSTANCE = Mappers.getMapper(BusMapper.class);

    @Mapping(source = "busName", target = "busName")
    @Mapping(target = "placeCount", constant = "0")
    Bus busNameToBus(String busName);

    BusDtoResponse busToBusDtoResponse(Bus bus);

    List<BusDtoResponse> busListToBusDtoResponseList(List<Bus> busList);

    Bus busDtoRequestToBus(BusDtoRequest busDtoRequest);



}
