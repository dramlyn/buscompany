package buscompany.mappers;

import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.model.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AdminMapper {

    @Insert("INSERT INTO `admin`(id, `position`) VALUES " +
            "(#{admin.id}, #{admin.position})")
    void insertAdmin(@Param("admin") Admin admin);

    Admin getAdminByLoginAndPassword(String login, String password);

    Admin getAdminById(int id);

    @Update({
            "<script>",
            "UPDATE `admin`",
            "<set>",
            "<if test='#{newPosition} != null'> `admin`.`position` = #{newPosition}",
            "</if>",
            "</set>",
            "WHERE id = #{adminId}",
            "</script>"
    })
    void updateAdmin(@Param("newPosition") String newPosition, @Param("adminId") int adminId);



}
