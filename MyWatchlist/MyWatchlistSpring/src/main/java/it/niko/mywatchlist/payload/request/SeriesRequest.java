package it.niko.mywatchlist.payload.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SeriesRequest {

    @NotBlank
    @Size(max = 50)
    private String title;

    private int episodes;

    @Size(max = 200)
    private String plot;

    private Set<String> genres;
}
