package buscompany.mappers;

import buscompany.model.Client;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ClientMapper {

    @Insert("INSERT INTO `client`(id, email, phone) VALUES " +
            "(#{client.id}, #{client.email}, #{client.phone})")
    void insertClient(@Param("client") Client client);

    Client getClientByLoginAndPassword(String login, String password);

    Client getClientById(int id);

    List<Client> getAllClients();

    @Update({
            "<script>",
            "UPDATE `client`",
            "<set>",
            "<if test='newEmail != null'> email = #{newEmail},",
            "</if>",
            "<if test='newPhone != null'> phone = #{newPhone}",
            "</if>",
            "</set>",
            "WHERE id = #{clientId}",
            "</script>"
    })
    void updateClient(@Param("newEmail") String newEmail, @Param("newPhone") String newTelephone, @Param("clientId") int clientId);



}
