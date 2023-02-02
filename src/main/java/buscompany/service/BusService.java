package buscompany.service;

import buscompany.daoimpl.BusDaoImpl;
import buscompany.dto.response.BusDtoResponse;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mapstruct.BusMapper;
import buscompany.model.Bus;
import buscompany.utils.CookieUtils;
import buscompany.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BusService {

    private BusDaoImpl busDao;

    private CookieUtils cookieUtils;

    private ServiceUtils serviceUtils;

    public List<BusDtoResponse> getAllBusBrands(String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }
        List<Bus> list = busDao.getAllBusBrands();
        return BusMapper.INSTANCE.busListToBusDtoResponseList(list);
    }
}
