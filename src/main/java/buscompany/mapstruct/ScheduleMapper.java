package buscompany.mapstruct;

import buscompany.dto.request.ScheduleDtoRequest;
import buscompany.dto.response.ScheduleDtoResponse;
import buscompany.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    Schedule scheduleDtoRequestToSchedule(ScheduleDtoRequest scheduleRequest);

    ScheduleDtoResponse scheduleToScheduleDtoResponse(Schedule schedule);
}
