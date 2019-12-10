package netty.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MyMessage implements Serializable {
    private static final long serialVersionUID = 5193392663742561680L;

    private String text;

}
