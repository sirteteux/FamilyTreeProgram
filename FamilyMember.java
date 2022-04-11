import java.io.Serializable;

/**
 * @author Andreas Lau, started on 31/05/2021
 * A class that manages the details of people
 */
public class FamilyMember implements Serializable {

    private String firstName;
    private String surnameAtBirth;
    private String surnameMarriage;
    private char gender;
    private Address streetAddress;
    private String lifeDescription;
    private FamilyRelation familyRelation;

    /**
     * Constructor
     */
    public FamilyMember() {
        firstName = "";
        surnameAtBirth = "";
        surnameMarriage = "";
        gender = '\0';
        streetAddress = new Address();
        lifeDescription = "";
    }

    /**
     * Constructor
     * 
     * @param fn  as firstName
     * @param snb as surnameAtBirth
     * @param snm as surnameMarriage
     * @param gen as gender
     * @param sa  as streetAddress
     * @param ld  as lifeDescription
     * @param famR as familyRelation
     */
    public FamilyMember(String fn, String snb, String snm, char gen, String ld, Address sa, FamilyRelation famR) {
        firstName = fn;
        surnameAtBirth = snb;
        surnameMarriage = snm;
        gender = gen;
        streetAddress = sa;
        lifeDescription = ld;
        famR.SetRootPerson(this);
        familyRelation = famR;
    }

    /**
     * Set firstName and validates if empty string
     * 
     * @param fn
     * @return Return boolean
     */
    public boolean SetFirstName(String fn) {
        if (ValidateInput(fn, "^(?!\\s*$).+")) {
            firstName = fn;
            return true;
        }
        return false;
    }

    /**
     * Set surnameAtBirth and validates if empty string
     * 
     * @param snb
     * @return Return boolean
     */
    public boolean SetSurnameAtBirth(String snb) {
        if (ValidateInput(snb, "^(?!\\s*$).+")) {
            surnameAtBirth = snb;
            return true;
        }
        return false;
    }

    /**
     * Set surnameMarriage and validates if empty string
     * 
     * @param snm
     * @return Return boolean
     */
    public boolean SetSurnameMarriage(String snm) {
        if (ValidateInput(snm, "^(?!\\s*$).+")) {
            surnameMarriage = snm;
            return true;
        }
        return false;
    }

    /**
     * Set gender if validates for m, M, f, F values
     * 
     * @param gen
     * @return Return boolean
     */
    public boolean SetGender(String gen) {
        if (ValidateInput(gen, "[m|M|f|F]")) {
            // Remove whitespace
            String tempGender = gen.replaceAll("\\W", "");
            gender = tempGender.charAt(0);
            return true;
        }
        return false;
    }

    /**
     * Set gender if validates for m, M, f, F values
     * 
     * @param gen
     * @return Return boolean
     */
    public boolean SetGender(char gen) {
        if (gen == 'm' || gen == 'M' || gen == 'f' || gen == 'F') {
            gender = gen;
            return true;
        }
        return false;
    }

    /**
     * Set address with string parameters for validation in address class
     * 
     * @param no
     * @param nam
     * @param sub
     * @param pc
     */
    public void SetStreetAddress(String no, String nam, String sub, String pc) {
        streetAddress.SetAddress(no, nam, sub, pc);
    }

    /**
     * Set address
     * 
     * @param no
     * @param nam
     * @param sub
     * @param pc
     */
    public void SetStreetAddress(int no, String nam, String sub, int pc) {
        streetAddress.SetAddress(no, nam, sub, pc);
    }

    /**
     * Set life description
     * 
     * @param ld
     */
    public void SetLifeDescription(String ld) {
        lifeDescription = ld;
    }

    /**
     * Get the firstName
     * 
     * @return Return firstName
     */
    public String GetFirstName() {
        return firstName;
    }

    /**
     * Get the surnameAtBirth
     * 
     * @return Return surnameAtBirth
     */
    public String GetSurnameAtBirth() {
        return surnameAtBirth;
    }

    /**
     * Get the surnameMarriage
     * 
     * @return Return surnameMarriage
     */
    public String GetSurnameMarriage() {
        return surnameMarriage;
    }

    /**
     * Get the gender
     * 
     * @return Return gender
     */
    public char GetGender() {
        return gender;
    }

    /**
     * Get the streetAddress
     * 
     * @return Return streetAddress
     */
    public Address GetStreetAddress() {
        return streetAddress;
    }

    /**
     * Get the lifeDescription
     * 
     * @return Return lifeDescription
     */
    public String GetLifeDescription() {
        return lifeDescription;
    }

    /**
     * Get the familyRelation and return new familyRelation if null
     * 
     * @return Return familyRelation or new familyRelation
     */
    public FamilyRelation GetFamilyRelation() {
        return ((familyRelation == null) ? new FamilyRelation() : familyRelation);
    }

    /**
     * Determines if person is empty
     * 
     * @return Return boolean
     */
    public boolean IsEmpty() {

        if (firstName.equals("")) {
            if (surnameAtBirth.equals("")) {
                if (surnameMarriage.equals("")) {
                    if (gender == '\0') {
                        if (streetAddress.IsEmpty()) {
                            if (lifeDescription.equals("")) {
                                return true;
                            }
                        }
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