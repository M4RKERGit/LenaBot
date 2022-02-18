package UserJSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel
{
    private String username;
    private String chatId;
    private List<String> citiesList;

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof UserModel)) return false;

        UserModel otherUser = (UserModel) obj;
        return (this.username.equals(otherUser.getUsername()) && this.chatId.equals(otherUser.getChatId()));
    }
}
