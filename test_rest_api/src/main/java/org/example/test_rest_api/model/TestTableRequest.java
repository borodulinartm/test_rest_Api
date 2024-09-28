package org.example.test_rest_api.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestTableRequest {
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "description cannot be empty")
    private String description;
}
