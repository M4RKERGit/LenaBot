package JSONdecode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RainModel
{
    @JsonProperty("1h")
    double oneHour;
    @JsonProperty("3h")
    double threeHours;
}