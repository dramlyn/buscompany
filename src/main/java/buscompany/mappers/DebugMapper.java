package buscompany.mappers;

import buscompany.model.Bus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface DebugMapper {

    @Insert("INSERT INTO `bus` VALUES(#{bus.busName}, #{bus.placeCount})")
    void insertBus(@Param("bus") Bus bus);
}
