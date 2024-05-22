package dto;

public class SettingsDTO extends DTO {
    private String userName, p1, p2;
    // client1 -> server: userName - имя получателя настроек (имя client2)
    // server -> client2: userName - имя источника настроек (имя client1)
    public SettingsDTO(String userName, String p1, String p2) {
        this.userName = userName;
        this.p1 = p1;
        this.p2 = p2;
    }
    public String getUserName() {return userName;}
    public String getP1() {return p1;}
    public String getP2() {return p2;}
}
