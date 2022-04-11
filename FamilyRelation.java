import java.util.*;
import java.io.Serializable;

/**  
* @author Andreas Lau, started on 31/05/2021
* A class that manages the relationships between Person objects
*/ 
public class FamilyRelation implements Serializable{
   
    private FamilyMember rootPerson;
    private FamilyMember Spouse;
    private FamilyMember Mother;
    private FamilyMember Father;
    private ArrayList<FamilyMember> Children = new ArrayList<>();
    
    /**
     * Default constructor
    */
    public FamilyRelation() {
        rootPerson = new FamilyMember();
        Spouse = new FamilyMember();
        Mother = new FamilyMember();
        Father = new FamilyMember();
    }
    
    /**
    * Constructor
    * @param rootp as rootPerson
    * @param spouse as Spouse
    * @param mother as Mother
    * @param father as Father
    */
    public FamilyRelation(FamilyMember rootp, FamilyMember spouse, FamilyMember mother, FamilyMember father) {
        rootPerson = rootp;
        Spouse = spouse;
        Mother = mother;
        Father = father;
    }
    
    /**
    * Get the rootPerson.
    * @return Returns rootPerson
    */
    public FamilyMember GetRootPerson() {
        return rootPerson;
    }
    
    /**
    * Get the spouse of rootPerson
    * @return Returns spouse
    */
    public FamilyMember GetSpouse() {
        return Spouse;
    }
    
    /**
    * Get the mother of rootPerson
    * @return Returns Mother
    */
    public FamilyMember GetMother() {
        return Mother;
    }
    
    /**
    * Get the father of rootPerson
    * @return Returns Father
    */
    public FamilyMember GetFather() {
        return Father;
    }
    
    /**
    * Get children of rootPerson
    * @return Retuns Children
    */
    public ArrayList<FamilyMember> GetChildren() {
        return Children;
    }
    
    /**
    * Add person to Children if they are not added yet
    * @param person child
    * @return Returns boolean
    */
    public boolean AddChild(FamilyMember person) {
        boolean exists = false;
        for(int i = 0; i < Children.size(); i++) {
            if(Children.get(i) == person) {
                exists = true;
            }
        }
        //if not exists add child
        if(!exists) {
            Children.add(person);
            UpdateRelations();
            return true;
        } else {
            return false;
        }
    }
    
    /**
    * Add Spouse if they are the opposite gender and merges their Children lists
    * @param person Spouse
    * @return Returns boolean
    */
    public boolean UpdateSpouse(FamilyMember person) {
        if(Character.toLowerCase(rootPerson.GetGender()) != Character.toLowerCase(person.GetGender())) {
            SetSpouse(person);
            person.GetFamilyRelation().SetSpouse(rootPerson);
            person.GetFamilyRelation().SetChildren(Children);
            UpdateRelations();
            return true;
        }
        return false;
    }
    
    /**
    * Add parent by calling the method appropriate to their gender
    * @param person parent
    * @return Returns boolean
    */
    public boolean UpdateParent(FamilyMember person) {
        if(Character.toLowerCase(person.GetGender()) == 'm') {
            UpdateFather(person);
            return true;
        } else if(Character.toLowerCase(person.GetGender()) == 'f') {
            UpdateMother(person);
            return true;
        }
        return false;
    }
    
    /**
    * Add father and updates rootPerson to their children list
    * @param person Father
    * @return Returns boolean
    */
    public boolean UpdateFather(FamilyMember person) {
        if(Character.toLowerCase(person.GetGender()) == 'm') {
            SetFather(person);
            person.GetFamilyRelation().AddChild(rootPerson);
            UpdateRelations();
            return true;
        } else if(person.IsEmpty()) {
            SetFather(person);
            UpdateRelations();
            return true;
        }
        return false;
    }
    
    /**
    * Add Mother and updates rootPerson to their Children list
    * @param person Mother
    * @return Returns boolean
    */
    public boolean UpdateMother(FamilyMember person) {
        if(Character.toLowerCase(person.GetGender()) == 'f') {
            SetMother(person);
            person.GetFamilyRelation().AddChild(rootPerson);
            UpdateRelations();
            return true;
        } else if(person.IsEmpty()) {
            SetMother(person);
            UpdateRelations();
            return true;
        }
        return false;
    }
    
    /**
    * Updates relations of rootPerson to make sure that all links are consistent between entities
    */
    public void UpdateRelations() {
        
        for(int i = 0; i < Children.size(); i++) {
            if(Character.toLowerCase(rootPerson.GetGender()) == 'm') {
                if(!Spouse.IsEmpty()) {
                    Children.get(i).GetFamilyRelation().SetMother(Spouse);
                }
                Children.get(i).GetFamilyRelation().SetFather(rootPerson);
            } else if(Character.toLowerCase(rootPerson.GetGender()) == 'f') {
                if(!Spouse.IsEmpty()) {
                    Children.get(i).GetFamilyRelation().SetFather(Spouse);
                }
                Children.get(i).GetFamilyRelation().SetMother(rootPerson);
            }
        }
        
    }
    
    /**
    * Set the rootPerson
    * @param person rootPerson
    */
    public void SetRootPerson(FamilyMember person) {
        rootPerson = person;
    }
    
    /**
    * Set the children
    * @param list Children (ArrayList)
    */
    public void SetChildren(ArrayList<FamilyMember> list) {
        Children = list;
    }
    
    /**
    * Set the spouse
    * @param person Spouse
    */
    private void SetSpouse(FamilyMember person) {
        Spouse = person;
    }
    
    /**
    * Set the mother
    * @param person mother
    */
    private void SetMother(FamilyMember person) {
        Mother = person;
    }
    
    /**
    * Set the father
    * @param person Father
    */
    private void SetFather(FamilyMember person) {
        Father = person;
    }
    
}
