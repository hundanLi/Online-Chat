package club.lzlbog.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 13:20
 **/
@Component
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotBlank
    @Size(min = 2, max = 16, message = "2-16位字符")
    private String username;
    @NotBlank
    @Size(min = 8, max = 16, message = "8-16位字符")
    private String password;
}
