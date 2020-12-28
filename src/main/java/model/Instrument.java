package model;

/**
 * An instrument
 */
public class Instrument implements InstrumentDTO{
private int id;
private String name;
private String brand;
private String type;
private Double fee;
private Boolean is_available;

    /**
     * Creates an Instrument with specified parameters.
     * @param id The database id for the Instrument
     * @param name The name of the instrument
     * @param brand The brand of the instrument
     * @param fee The fee to rent the instrument
     * @param is_available BOOLEAN true if it is not currently rented
     */
    public Instrument(int id, String name,String type, String brand, Double fee, Boolean is_available) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.brand = brand;
        this.fee = fee;
        this.is_available = is_available;
    }


    public Boolean getIs_available() {
        return is_available;
    }

    public Double getFee() {
        return fee;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    /**
     * @return A string representation of all relevant fields in this object.
     */
    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Instrument: [");
        stringRepresentation.append("name: ");
        stringRepresentation.append(name);
        stringRepresentation.append(", brand: ");
        stringRepresentation.append(brand);
        stringRepresentation.append(", type: ");
        stringRepresentation.append(type);
        stringRepresentation.append(", fee: ");
        stringRepresentation.append(fee);
        stringRepresentation.append(", Available: ");
        stringRepresentation.append(is_available);
        stringRepresentation.append("]");
        return stringRepresentation.toString();
    }

}
