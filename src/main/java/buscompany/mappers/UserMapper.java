package buscompany.mappers;

import buscompany.dto.request.UpdateUserDtoRequest;
import buscompany.model.UserType;
import buscompany.model.User;
import org.apache.ibatis.annotations.*;

import javax.validation.constraints.Pattern;

public interface UserMapper {

    @Insert("INSERT INTO `user`(firstname, lastname, patronymic, login, `password`, user_type) VALUES " +
            "( #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password}, #{userType})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insertUser(@Param("user") User user, @Param("userType") UserType userType);


    @Select("SELECT user_type FROM `user` WHERE login = #{login}")
    UserType selectUserTypeByLogin(@Param("login") String login);

    @Select("SELECT user_type FROM `user` WHERE id = #{userId}")
    UserType selectUserTypeById(@Param("userId") int userId);

    @Update({"<script>",
            "UPDATE `user`",
            "<set>",
            "<if test='updateUser.firstName != null'> firstname = #{updateUser.firstName},",
            "</if>",
            "<if test='updateUser.lastName != null'> lastname = #{updateUser.lastName},",
            "</if>",
            "<if test='updateUser.patronymic != null'> patronymic = #{updateUser.patronymic},",
            "</if>",
            "<if test='updateUser.password != null'> `password` = #{updateUser.password}",
            "</if>",
            "</set>",
            "WHERE id = #{userId}",
            "</script>"
    })
    void updateUser(@Param("updateUser") User updateUser, @Param("userId") int userId);

    @Delete("DELETE FROM `user`")
    void deleteAll();

    @Select("SELECT `password` FROM `user` WHERE id = #{userID}")
    String selectPasswordByUserID(@Param("userID") int userID);


}
