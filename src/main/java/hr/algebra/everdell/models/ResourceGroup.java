package hr.algebra.everdell.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.MessageFormat;

@XmlRootElement
@Getter
@NoArgsConstructor
public class ResourceGroup implements Comparable<ResourceGroup>, Serializable {
    @XmlElement
    private int berries;
    @XmlElement
    private int twigs;
    @XmlElement
    private int resin;
    @XmlElement
    private int pebbles;

    public ResourceGroup(int berries, int twigs, int resin, int pebbles) {
        this.berries = berries;
        this.twigs = twigs;
        this.resin = resin;
        this.pebbles = pebbles;
    }

    public void addBerries(int berries) {
        this.berries = berries >= 0 ? this.berries + berries : this.berries;
    }

    public void addTwigs(int twigs) {
        this.twigs = twigs >= 0 ? this.twigs + twigs : this.twigs;
    }

    public void addResin(int resin) {
        this.resin = resin >= 0 ? this.resin + resin : this.resin;
    }

    public void addPebbles(int pebbles) {
        this.pebbles = pebbles >= 0 ? this.pebbles + pebbles : this.pebbles;
    }

    public boolean removeBerries(int berries) {
        if (berries>this.berries) {
            return false;
        }  else {
            this.berries -= berries;
            return true;
        }
    }

    public boolean removeTwigs(int twigs) {
        if (twigs > this.twigs){
            return false;
        }  else {
            this.twigs -= twigs;
            return true;
        }
    }

    public boolean removeResin(int resin) {
        if (resin>this.resin){
            return false;
        }  else {
            this.resin -= resin;
            return true;
        }
    }

    public boolean removePebbles(int pebbles) {
        if (pebbles>this.pebbles){
            return false;
        }  else {
            this.pebbles -= pebbles;
            return true;
        }
    }

    public Boolean subtract(ResourceGroup other) {
        if (this.berries < other.berries
            || this.pebbles < other.pebbles
            || this.resin < other.resin
            || this.twigs < other.twigs) {
            return false;
        } else {
            this.berries -= other.berries;
            this.pebbles -= other.pebbles;
            this.resin -= other.resin;
            this.twigs -= other.twigs;
            return true;
        }
    }

    public void merge(ResourceGroup resourceGroup){
        addBerries(resourceGroup.getBerries());
        addTwigs(resourceGroup.getTwigs());
        addResin(resourceGroup.getResin());
        addPebbles(resourceGroup.getPebbles());
        resourceGroup.clear();
    }

    public void replace(ResourceGroup resourceGroup){
        berries = resourceGroup.getBerries();
        twigs = resourceGroup.getTwigs();
        resin = resourceGroup.getResin();
        pebbles = resourceGroup.getPebbles();
    }

    public void clear(){
        this.berries = 0;
        this.twigs = 0;
        this.resin = 0;
        this.pebbles = 0;
    }

    @Override
    public int compareTo(ResourceGroup o) {
        if (berries >= o.berries
            && twigs >= o.twigs
            && resin >= o.resin
            && pebbles >= o.pebbles){
            return 1;
        } else {
            return -1;
        }
    }

    public int sumAllResources(){
        return getBerries()+getPebbles()+getResin()+getTwigs();
    }

    @Override
    public String toString() {
        return MessageFormat.format("Berries: {0}, Twigs: {1}, Resin: {2}, Pebbles: {3},", berries, twigs, resin, pebbles);
    }
}
