package hr.algebra.everdell.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
public class Marker implements Serializable {
    double x;
    double y;
    String name;
    Location location;
}
