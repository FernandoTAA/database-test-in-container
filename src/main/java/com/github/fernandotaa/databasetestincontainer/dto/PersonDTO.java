package com.github.fernandotaa.databasetestincontainer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("PERSON")
public class PersonDTO {

    @Id
    private Long id;
    private String name;
}
