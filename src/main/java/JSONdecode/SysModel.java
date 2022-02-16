package JSONdecode;

import lombok.Data;

@Data
public class SysModel
{
    private int type;
    private int id;
    private String message;
    private String country;
    private long sunrise;
    private long sunset;
}
