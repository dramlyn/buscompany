package buscompany.mappers;

import buscompany.dto.response.BusDtoResponse;
import buscompany.model.Bus;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BusMapper {

    @Select("SELECT * FROM bus")
    List<Bus> getAllBusBrands();

    @Select("SELECT * FROM bus WHERE busName = #{busName}")
    Bus getBusByBusName(@Param("busName") String busName);

    @Select("SELECT placeCount FROM `bus` WHERE busName = #{busName}")
    Integer getPlaceCountByBusName(@Param("busName") String busName);

    @Delete("DELETE FROM `bus`")
    void deleteAll();


}
