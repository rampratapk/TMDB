package com.example.tmdb.MVP.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "id",
        "logo_path",
        "origin_country"

})
public class ProductionCompany {

    @JsonProperty("name")
    public String name;
    @JsonProperty("id")
    public int id;
    @JsonProperty("logo_path")
    public String logoPath;
    @JsonProperty("origin_country")
    public String country;
}
