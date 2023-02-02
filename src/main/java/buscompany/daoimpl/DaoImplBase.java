package buscompany.daoimpl;

import buscompany.mappers.*;
import buscompany.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class DaoImplBase {
    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected AdminMapper getAdminMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }

    protected ClientMapper getClientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ClientMapper.class);
    }

    protected BusMapper getBusMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(BusMapper.class);
    }

    protected TripMapper getTripMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(TripMapper.class);
    }

    protected OrderMapper getOrderMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(OrderMapper.class);
    }

    protected PlacesMapper getPlacesMapper(SqlSession sqlSession){
        return sqlSession.getMapper(PlacesMapper.class);
    }

    protected DebugMapper getDebugMapper(SqlSession sqlSession){
        return sqlSession.getMapper(DebugMapper.class);
    }
}
