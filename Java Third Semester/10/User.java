import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@FtlhForm(method = "post", action = "/")
public class User {
    @FtlhInput(name = "id", placeholder = "Your id")
    protected Long id;
    @FtlhInput(name = "firstName", placeholder = "Your firstName")
    protected String firstName;
    @FtlhInput(name = "lastName", placeholder = "Your lastName")
    protected String lastName;
    @FtlhInput(name = "middleName", placeholder = "Your middleName")
    protected String middleName;
    @FtlhInput(name = "eMail", type = "email", placeholder = "Your eMail")
    protected String eMail;
    @FtlhInput(name = "password", type = "password", placeholder = "Your password")
    protected String password;
    @FtlhInput(name = "age", placeholder = "Your age")
    protected Integer age;

}
