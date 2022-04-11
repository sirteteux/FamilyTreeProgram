import java.io.Serializable;

/**
 * @author Andreas Lau, started on 31/05/2021
 * A class that manages and validates address data
 */
public class Address implements Serializable {

    private int streetNo;
    private String streetName;
    private String subUrb;
    private int postCode;

    /**
     * Default constructor
     */
    public Address() {
        streetNo = 0;
        streetName = "";
        postCode = 0;
        subUrb = "";
    }

    /**
     * Constructor
     */
    public Address(int streetno, String streetname, String suburb, int postcode) {
        streetNo = streetno;
        streetName = streetname;
        subUrb = suburb;
        postCode = postcode;
    }

    /**
     * Set address values with string parameters
     */
    public void SetAddress(String streetno, String streetname, String suburb, String postcode) {
        SetStreetNo(streetno);
        SetStreetName(streetname);
        SetSubUrb(suburb);
        SetPostCode(postcode);
    }

    /**
     * Set address
     */
    public void SetAddress(int streetno, String streetname, String suburb, int postcode) {
        streetNo = streetno;
        streetName = streetname;
        subUrb = suburb;
        postCode = postcode;
    }

    /**
     * Set streetno
     * 
     * @return Return boolean
     */
    public boolean SetStreetNo(String streetno) {
        if (ValidateInput(streetno, "[+]?\\d+(\\.\\d+)?")) {
            streetNo = Integer.parseInt(streetno);
            return true;
        }
        return false;
    }

    /**
     * Set streetno
     */
    public void SetStreetNo(int streetno) {
        streetNo = streetno;
    }

    /**
     * Set streetname
     * 
     * @return Return boolean
     */
    public boolean SetStreetName(String streetname) {
        if (ValidateInput(streetname, "^(?!\\s*$).+")) {
            streetName = streetname;
            return true;
        }
        return false;
    }

    /**
     * Set suburb
     * 
     * @return Return boolean
     */
    public boolean SetSubUrb(String suburb) {
        if (ValidateInput(suburb, "^(?!\\s*$).+")) {
            subUrb = suburb;
            return true;
        }
        return false;
    }

    /**
     * Set postcode
     * 
     * @return Return boolean
     */
    public boolean SetPostCode(String postcode) {
        if (ValidateInput(postcode, "[+]?\\d+(\\.\\d+)?")) {
            postCode = Integer.parseInt(postcode);
            return true;
        }
        return false;
    }

    /**
     * Set postcode
     */
    public void SetPostCode(int postcode) {
        postCode = postcode;
    }

    /**
     * @return Return streetNo
     */
    public int GetStreetNo() {
        return streetNo;
    }

    /**
     * @return Return streetName
     */
    public String GetStreetName() {
        return streetName;
    }

    /**
     * @return Return suburb
     */
    public String GetSubUrb() {
        return subUrb;
    }

    /**
     * Get postcode
     * 
     * @return postcode
     */
    public int GetPostCode() {
        return postCode;
    }

    /**
     * Determines if address is empty
     * 
     * @return Return boolean
     */
    public boolean IsEmpty() {
        if (streetNo == 0) {
            if (streetName.equals("")) {
                if (subUrb.equals("")) {
                    if (postCode == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Validate string against regular expression
     * 
     * @return Return boolean
     */
    private boolean ValidateInput(String input, String criteria) {
        return (input.matches(criteria));
    }
}