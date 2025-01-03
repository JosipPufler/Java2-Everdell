package hr.algebra.everdell.models;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class Marker implements Serializable {
    public double x;
    public double y;
    public String name;
    public Location location;
}
