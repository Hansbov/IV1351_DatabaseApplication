package model;

/**
 * Specifies a read only view of Instrument
 */
public interface InstrumentDTO {

    public Boolean getIs_available();

    public Double getFee();

    public int getId();

    public String getBrand();

    public String getName();

}
