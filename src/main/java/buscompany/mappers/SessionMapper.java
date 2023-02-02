package buscompany.mappers;

import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

public interface SessionMapper {


    @Insert("INSERT INTO `session` VALUES (#{sessionID}, #{userID}, #{lastAccessTime})")
    void initSession(@Param("userID") int userId, @Param("sessionID") String sessionID, @Param("lastAccessTime") LocalDateTime lastAccessTime);

    @Delete("DELETE FROM `session` WHERE id = #{sessionId}")
    void logout(@Param("sessionId") String sessionId);

    @Select("SELECT id_user FROM `session` WHERE id = #{sessionID}")
    Integer getUserIdBySessionId(@Param("sessionID") String sessionID);

    @Select("SELECT id FROM `session` WHERE id_user = #{userId}")
    Integer getSessionIdByUserId(@Param("userId") int userId);

    @Delete("DELETE FROM `user` WHERE id = #{userId}")
    void leave(@Param("userId") int userId);

    @Select("SELECT last_access_time FROM `session` WHERE id = #{sessionID}")
    LocalDateTime getLastAccessTimeBySessionID(@Param("sessionID") String sessionID);

    @Update("UPDATE `session` SET last_access_time = #{newAccessTime} WHERE id = #{sessionID}")
    void updateLastAccessTimeBySessionID(@Param("newAccessTime") LocalDateTime lastAccessTime, @Param("sessionID") String sessionID);

}
