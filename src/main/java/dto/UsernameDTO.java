package dto;

public class UsernameDTO extends DTO {
    private String userName;
    private boolean status; // true - клиент присоединился, false - клиент отключился
    public UsernameDTO(String userName) {
        this.userName = userName;
    }
    public UsernameDTO(String userName, boolean status) {
        this.userName = userName;
        this.status = status;
    }
    public String getUserName() {return userName;}
    public boolean getStatus() {return status;}
}
