package buscompany.dao;

import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.model.Admin;

public interface AdminDao {
    Admin insert(Admin admin);

    void update(Admin updateAdmin, int adminId);

    Admin getAdminById(int adminId);


}
