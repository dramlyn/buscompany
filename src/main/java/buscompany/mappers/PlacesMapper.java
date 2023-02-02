package buscompany.mappers;

import buscompany.model.Place;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

public interface PlacesMapper {

    @Select("SELECT place FROM `place` WHERE id_trip = #{tripID} AND `date` = #{date} AND id_passenger IS NULL")
    List<Integer> selectFreePlacesList(@Param("tripID") int tripID, @Param("date") LocalDate date);

    @Update("UPDATE `place` SET id_order = #{place.orderID}, id_passenger = #{passengerID} " +
            "WHERE id_trip = #{place.tripDate.tripID} AND `date` = #{place.tripDate.date} AND place = #{place.place} " +
            "AND id_order IS NULL AND id_passenger IS NULL")
    int choosePlace(@Param("place") Place place, @Param("passengerID") int passengerID);


    @Select("SELECT free_places FROM `trip_date` WHERE id_trip = #{tripID} AND `date` = #{date}")
    Integer getFreePlaces(@Param("tripID") int tripID, @Param("date") LocalDate date);


    @Update("UPDATE `trip_date` SET free_places = free_places - #{newOccupiedPlaces} WHERE id_trip = #{tripID} " +
            "AND `date` = #{date} AND free_places >= #{newOccupiedPlaces}")
    int updateFreePlaces(@Param("newOccupiedPlaces") int newOccupiedPlaces, @Param("tripID") int tripID, @Param("date") LocalDate date);

    @Select("SELECT count(*) FROM `place` WHERE id_order = #{orderID} AND id_passenger = #{passengerID}")
    int isPlaced(@Param("orderID") int orderID, @Param("passengerID") int passengerID);

    @Update("UPDATE `place` SET id_order = NULL, id_passenger = NULL WHERE id_order = #{orderID} AND id_passenger = #{passengerID}")
    void deletePassengerPlace(@Param("orderID") int orderID, @Param("passengerID") int passengerID);

    @Select("SELECT count(*) FROM `passenger` WHERE id_order = #{orderID} AND passport = #{passport}")
    int checkPassenger(@Param("orderID") int orderID, @Param("passport") String passport);

    @Select("SELECT count(*) FROM `place` WHERE place = #{place} AND id_trip = #{tripID} AND `date` = #{date}")
    int checkPlacedByAnotherPassenger(@Param("date") LocalDate date, @Param("tripID") int tripID, @Param("place") int place);

    @Select("SELECT count(*) FROM `passenger` WHERE id_order = #{orderID}")
    int selectPlacedPassengersByOrderID(@Param("orderID") int orderID);

    @Insert("INSERT INTO `place` VALUES(null, #{tripID}, #{date}, #{place}, null)")
    void insertPlaceByDate(@Param("date") LocalDate date, @Param("tripID") int tripId, @Param("place") int place);

    @Update("UPDATE `place` SET id_order = NULL, id_passenger = NULL " +
            "WHERE id_order = #{orderID}")
    void deleteOrderPassengersPlaces(@Param("orderID") int orderID);

    @Select("SELECT count(*) FROM `passenger` WHERE passport = #{passport} AND id_order = #{orderID} AND firstName = #{firstName} " +
            "AND lastName = #{lastName}")
    int checkPassportFirstAndLastName(@Param("passport") String passport, @Param("orderID") int orderID, @Param("firstName") String firstName,
                                      @Param("lastName") String lastName);

}
