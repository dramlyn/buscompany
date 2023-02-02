package buscompany.service;

import buscompany.daoimpl.DebugDaoImpl;
import buscompany.dto.request.BusDtoRequest;
import buscompany.mapstruct.BusMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DebugService {

    private DebugDaoImpl debugDao;

    public void deleteDataBase(){
        debugDao.deleteDataBase();
    }

    public void addBus(BusDtoRequest busDtoRequest){
        debugDao.insertBus(BusMapper.INSTANCE.busDtoRequestToBus(busDtoRequest));
    }

}
