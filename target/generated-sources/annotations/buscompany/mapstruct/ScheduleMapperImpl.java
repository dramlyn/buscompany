package buscompany.mapstruct;

import buscompany.dto.request.ScheduleDtoRequest;
import buscompany.dto.response.ScheduleDtoResponse;
import buscompany.model.Schedule;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-09T17:24:21+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class ScheduleMapperImpl implements ScheduleMapper {

    @Override
    public Schedule scheduleDtoRequestToSchedule(ScheduleDtoRequest scheduleRequest) {
        if ( scheduleRequest == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        if ( scheduleRequest.getFromDate() != null ) {
            schedule.setFromDate( LocalDate.parse( scheduleRequest.getFromDate() ) );
        }
        if ( scheduleRequest.getToDate() != null ) {
            schedule.setToDate( LocalDate.parse( scheduleRequest.getToDate() ) );
        }
        schedule.setPeriod( scheduleRequest.getPeriod() );

        return schedule;
    }

    @Override
    public ScheduleDtoResponse scheduleToScheduleDtoResponse(Schedule schedule) {
        if ( schedule == null ) {
            return null;
        }

        ScheduleDtoResponse scheduleDtoResponse = new ScheduleDtoResponse();

        if ( schedule.getFromDate() != null ) {
            scheduleDtoResponse.setFromDate( DateTimeFormatter.ISO_LOCAL_DATE.format( schedule.getFromDate() ) );
        }
        if ( schedule.getToDate() != null ) {
            scheduleDtoResponse.setToDate( DateTimeFormatter.ISO_LOCAL_DATE.format( schedule.getToDate() ) );
        }
        scheduleDtoResponse.setPeriod( schedule.getPeriod() );

        return scheduleDtoResponse;
    }
}
