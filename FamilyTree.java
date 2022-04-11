import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 * @author Andreas Lau, started on 31/05/2021
 * A class that manages an instance of a family tree
 */
public class FamilyTree {

    private FamilyMember rootPerson;

    /**
     * Default Constructor
     */
    public FamilyTree() {
        rootPerson = new FamilyMember();
    }

    /**
     * Constructor
     * 
     * @param person rootPerson
     */
    public FamilyTree(FamilyMember person) {
        rootPerson = person;
    }

    /**
     * Constructor
     * 
     * @param relationship rootPerson's Relationship
     */
    public FamilyTree(FamilyRelation relationship) {
        rootPerson = relationship.GetRootPerson();
    }

    /**
     * Set rootPerson
     * 
     * @param person rootPerson
     */
    public void SetRootPerson(FamilyMember person) {
        rootPerson = person;
    }

    /**
     * @return Return rootPerson
     */
    public FamilyMember GetRootPerson() {
        return rootPerson;
    }

    /**
     * Removes a person from the family tree.
     * Validates that they are not the root person
     * Updates all their relations data to ensure they are no longer linked.
     * Does not delete a person so much as disassociates them.
     * Linked nodes will also get deleted if they linked to the family tree through current person
     * @param person as rootPerson
     */
    public void RemovePerson(FamilyMember person) {

        person.GetFamilyRelation();
        if (person != rootPerson) {

            if (!person.GetFamilyRelation().GetFather().GetFamilyRelation().GetChildren().isEmpty()) {
                RemoveChild(person.GetFamilyRelation().GetFather(), person);
            }
            if (!person.GetFamilyRelation().GetMother().GetFamilyRelation().GetChildren().isEmpty()) {
                RemoveChild(person.GetFamilyRelation().GetMother(), person);
            }
            if (!person.GetFamilyRelation().GetSpouse().IsEmpty()) {
                person.GetFamilyRelation().GetSpouse().GetFamilyRelation().UpdateSpouse(new FamilyMember());
            }
            for (int i = 0; i < person.GetFamilyRelation().GetChildren().size(); i++) {
                if (Character.toLowerCase(person.GetGender()) == 'm') {
                    person.GetFamilyRelation().GetChildren().get(i).GetFamilyRelation().UpdateFather(new FamilyMember());
                } else if (Character.toLowerCase(person.GetGender()) == 'f') {
                    person.GetFamilyRelation().GetChildren().get(i).GetFamilyRelation().UpdateMother(new FamilyMember());
                }

            }

        }

    }

    /**
     * Remove a child
     * 
     * @param parent as parent
     * @param child  as child
     */
    private void RemoveChild(FamilyMember parent, FamilyMember child) {
        for (int i = 0; i < parent.GetFamilyRelation().GetChildren().size(); i++) {
            if (parent.GetFamilyRelation().GetChildren().get(i) == child) {
                parent.GetFamilyRelation().GetChildren().remove(i);
            }
        }
    }

    /**
     * Save a family tree in file as .ftree format
     * 
     * @param fileName filename or path
     */
    public void SaveFile(String fileName) {

        fileName += ".ftree";

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

            outputStream.writeObject(rootPerson);

            outputStream.close();

        } catch (FileNotFoundException e) {
            // Do Nothing
        } catch (IOException e) {
            // Do Nothing
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Unable to save " + fileName + ". Please try again",
                    "Save File Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Open a family tree file of .ftree
     * 
     * @param fileName filename or path and filename of file to be opened
     */
    public void OpenFile(String fileName) {

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName));

            Object obj = inputStream.readObject();

            rootPerson = (FamilyMember) obj;

            inputStream.close();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "File was not found at " + fileName + ". Please try again.",
                    "File Not Found", JOptionPane.ERROR_MESSAGE);
        } catch (EOFException e) {
            // Do Nothing
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Unable to open " + fileName + ". Please open a file with the .ftree extension.",
                    "Incompatible File Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Do Nothing
        }

    }
}