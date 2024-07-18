package it.project.cookcraft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementsInserter {
    @JsonProperty("strMeasure1")
    private String measure1;
    @JsonProperty("strMeasure2")
    private String measure2;
    @JsonProperty("strMeasure3")
    private String measure3;
    @JsonProperty("strMeasure4")
    private String measure4;
    @JsonProperty("strMeasure5")
    private String measure5;
    @JsonProperty("strMeasure6")
    private String measure6;
    @JsonProperty("strMeasure7")
    private String measure7;
    @JsonProperty("strMeasure8")
    private String measure8;
    @JsonProperty("strMeasure9")
    private String measure9;
    @JsonProperty("strMeasure10")
    private String measure10;
    @JsonProperty("strMeasure11")
    private String measure11;
    @JsonProperty("strMeasure12")
    private String measure12;
    @JsonProperty("strMeasure13")
    private String measure13;
    @JsonProperty("strMeasure14")
    private String measure14;
    @JsonProperty("strMeasure15")
    private String measure15;
    @JsonProperty("strMeasure16")
    private String measure16;
    @JsonProperty("strMeasure17")
    private String measure17;
    @JsonProperty("strMeasure18")
    private String measure18;
    @JsonProperty("strMeasure19")
    private String measure19;
    @JsonProperty("strMeasure20")
    private String measure20;
}
