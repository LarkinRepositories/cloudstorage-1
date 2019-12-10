package netty.file_transfer.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class FileRequest extends AbstractMessage {
    @Getter
    private String fileName;

}
