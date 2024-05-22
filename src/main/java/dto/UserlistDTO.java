package dto;

public class UserlistDTO extends DTO {
    private String[] userList;
    public UserlistDTO(String[] userList) {
        this.userList = userList;
    }
    public String[] getUserList() {return userList;}
}
