package ec.com.reactive.music.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class PlaylistDTO {
    private String idPlaylist= UUID.randomUUID().toString().substring(0, 10);
    private String name;
    private String username;
    private ArrayList<SongDTO> songs;
    private LocalTime duration = LocalTime.of(0,0,0);
}

