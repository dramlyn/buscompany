package buscompany.mapstruct;

import buscompany.dto.request.PlaceDtoRequest;
import buscompany.dto.response.PlaceDtoResponse;
import buscompany.model.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlacesMapper {

    PlacesMapper INSTANCE = Mappers.getMapper(PlacesMapper.class);

    @Mapping(ignore = true, target = "tripDate")
    Place placeDtoRequestToPlace(PlaceDtoRequest placeDtoRequest);

    PlaceDtoResponse placeToPlaceDtoResponse(Place place);

}
