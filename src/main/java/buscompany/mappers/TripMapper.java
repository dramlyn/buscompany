package buscompany.mappers;

import buscompany.model.Schedule;
import buscompany.model.Trip;
import buscompany.model.TripDate;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public interface TripMapper {
    @Insert("INSERT INTO `trip`(busName, fromStation, toStation, `start`, duration, price) VALUES" +
            "(#{trip.bus.busName},#{trip.fromStation},#{trip.toStation},#{trip.start},#{trip.duration}," +
            "#{trip.price})")
    @Options(useGeneratedKeys = true, keyProperty = "trip.id")
    Integer insertTrip(@Param("trip") Trip trip);

    @Insert({
            "<script>",
            "INSERT INTO `trip_date`(id_trip, `date`, free_places) VALUES",
            "<foreach item='item' collection='dates' separator=','>",
            "( #{tripID}, #{item.date} , #{item.freePlaces})",
            "</foreach>",
            "</script>"
    })
    void insertTripDates(@Param("dates") LinkedList<TripDate> dates, @Param("tripID") int tripId);

    @Insert("INSERT INTO `schedule`(id_trip, fromDate, toDate, period) VALUES" +
            "(#{tripID}, #{schedule.fromDate}, #{schedule.toDate}, #{schedule.period})")
    void insertSchedule(@Param("tripID") int tripID, @Param("schedule") Schedule schedule);

    @Delete("DELETE FROM `trip_date` WHERE id_trip = #{tripId}")
    void deleteDatesById(@Param("tripId") int tripId);

    @Delete("DELETE FROM `schedule` WHERE id_trip = #{tripID}")
    void deleteScheduleByID(@Param("tripID") int tripID);

    @Update({
            "<script>",
            "UPDATE `trip`",
            "<set>",
            "<if test='trip.bus.busName != null'> busName = #{trip.bus.busName},",
            "</if>",
            "<if test='trip.fromStation != null'> fromStation = #{trip.fromStation},",
            "</if>",
            "<if test='trip.toStation != null'> toStation = #{trip.toStation},",
            "</if>",
            "<if test='trip.start != null'> start = #{trip.start},",
            "</if>",
            "<if test='trip.duration != 0'> duration = #{trip.duration},",
            "</if>",
            "<if test='trip.price != 0'> price = #{trip.price},",
            "</if>",
            "</set>",
            "WHERE id = #{trip.id}",
            "</script>"
    })
    void updateTrip(@Param("trip") Trip trip);

    @Delete("DELETE FROM `trip` WHERE id = #{tripID}")
    void deleteTrip(@Param("tripID") int tripID);

    @Select("SELECT id_trip AS tripID, `date`, free_places AS freePlaces FROM `trip_date` WHERE id_trip = #{tripID}")
    LinkedList<TripDate> selectTripDatesByTripID(@Param("tripID") int tripID);

    @Select("SELECT fromDate, toDate, period FROM `schedule` WHERE id_trip = #{tripID}")
    Schedule selectScheduleByTripID(@Param("tripID") int tripID);

    Trip selectTripByTripID(@Param("tripID") int tripID);

    @Update("UPDATE `trip` SET isapproved = true WHERE id = #{tripID}")
    void approveTrip(@Param("tripID") int tripID);

    List<Trip> selectTripList(@Param("fromStation") String fromStation,
                              @Param("toStation") String toStation,
                              @Param("busName") String busName,
                              @Param("approved") boolean approved);

    @Select("SELECT id_trip AS tripID, `date`, free_places AS freePlaces FROM `trip_date` WHERE id_trip = #{tripID} AND `date` = #{date}")
    TripDate selectTripDateByTripIDAndDate(@Param("tripID") int tripID, @Param("date") LocalDate date);

    @Delete("DELETE FROM `trip`")
    void deleteAll();
}
