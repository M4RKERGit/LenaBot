package UserJSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBase
{
    private long totalUsers;
    private String curDate;
    private List<UserModel> userList;
}
