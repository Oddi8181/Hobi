package gio.hobist.Dto;

import gio.hobist.Entity.City;
import gio.hobist.Entity.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CountryCityDto {
    private Integer Id;
    private String name;

    public CountryCityDto(Country country) {
        this.Id = country.getId();
        this.name = country.getName();
    }
    public CountryCityDto(City city) {
        this.Id =city.getId();
        this.name =city.getName();
    }
}
