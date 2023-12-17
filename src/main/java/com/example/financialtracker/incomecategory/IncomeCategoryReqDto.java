package com.example.financialtracker.incomecategory;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomeCategoryReqDto {

    @NotEmpty(message = "Title is required !")
    @Size(min = 4, message = "title should have at least 4 characters")
    private String title;

    private String description;
}
