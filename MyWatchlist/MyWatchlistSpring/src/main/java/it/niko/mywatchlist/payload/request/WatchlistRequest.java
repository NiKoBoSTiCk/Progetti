package it.niko.mywatchlist.payload.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class WatchlistRequest {
    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 20)
    private String username;

    private String status;

    private int progress;

    private int score;

    @Size(max = 200)
    private String comment;
}
