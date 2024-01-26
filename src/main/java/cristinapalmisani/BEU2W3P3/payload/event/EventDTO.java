package cristinapalmisani.BEU2W3P3.payload.event;

import jakarta.validation.constraints.*;

public record EventDTO(@NotNull(message = "Title cannot be null")
                       @NotEmpty(message = "Title cannot be empty")
                       @Size(min = 3, max = 30, message = "Title must be between 3 e 30 chars")
                       String title,
                       @NotNull(message = "Description cannot be null")
                       @NotEmpty(message = "Description cannot be empty")
                       @Size(min = 3, max = 30, message = "Description must be between 3 e 30 chars")
                       String description,
                       @NotNull(message = "Location cannot be null")
                       @NotEmpty(message = "Location cannot be empty")
                       @Size(min = 3, max = 30, message = "Location must be between 3 e 30 chars")
                       String location,

                       @NotNull(message = "Date cannot be null")
                       @NotEmpty(message = "Date cannot be empty")
                       @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date not valid (yyyy-mm-dd)")
                       String date,

                       @Min(value = 2, message = "Max participants must be greater than 1")
                       @Max(value = 1000, message = "Max participants must be less than 1000")
                       int maxParticipants
) {
}
