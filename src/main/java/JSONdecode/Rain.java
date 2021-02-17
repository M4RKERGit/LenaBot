package JSONdecode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class Rain {
    public double getHour() {
        return hour;
    }

    public void setHour(double hour) {
        this.hour = hour;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    @JsonProperty("1h")
    double hour;
    @JsonProperty("3h")
    double hours;
}