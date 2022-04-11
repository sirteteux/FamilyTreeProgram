/**  
* @author Andreas Lau, started on 28/06/2021
* 1. A person can have no more than one spouse
* 2. Spouse is of the opposite gender
* 3. Incestuous relationships cannot be added
* 4. Family beyond the root persons mother and father can be added
* 5. Gender cannot be changed on person who currently has a spouse
* 6. Invalid inputs will not be added
*/ 
public class Main {
    
    public static void main(String[] args) {
                
        //Test Data (Andreas family tree)

        //Andreas Family (Not real)
        FamilyMember p1 = new FamilyMember("Andreas","Lau","Lau",'m',"Andreas Lau loves to exercise to keep fit and healthy. He also programs this java project for ICT373 Assignment 2",new Address(50,"Rivervale Avenue 50","Rivervale",530050),new FamilyRelation());
        FamilyMember p2 = new FamilyMember("Jennifer","Lawrence","Lau",'f',"Supportive and loving spouse.",new Address(),new FamilyRelation());
        FamilyMember p3 = new FamilyMember("Sean","Lau","Lau",'m',"Sean Lau  is a Hong Kong actor who has acted in both films and television series. He won Best Actor in the 2007 and 2015 Hong Kong Film Awards and in the 2000 and 2007 Golden Bauhinia Awards.",new Address(238,"The Orchard Residences","238 Orchard Blvd",248652),new FamilyRelation());
        FamilyMember p4 = new FamilyMember("Carol","Chu","Lau",'f',"Carol, wife of Andy Lau, has 1 children with him. They are married since 2008.",new Address(10,"Marina Bay Sands","10 Bayfront Ave",18956),new FamilyRelation());
        FamilyMember p5 = new FamilyMember("Andy","Lau","Lau",'m',"Andy Lau loves to sing and he is well known world-wide\n" +"\n",new Address(10,"Marina Bay Sands","10 Bayfront Ave",18956),new FamilyRelation());
        FamilyMember p6 = new FamilyMember("Hanna","Lau","Bieber",'f',"Hanna Lau is a daughter of Andy and Carol.",new Address(58,"Centro Residences","59 Ang Mo Kio Ave 8",567752),new FamilyRelation());
        FamilyMember p7 = new FamilyMember("Justin","Bieber","Bieber",'m',"Justin Bieber, a famous singer. Had a son with hannah in 2020.",new Address(58,"Centro Residences","59 Ang Mo Kio Ave 8",567752),new FamilyRelation());
        FamilyMember p8 = new FamilyMember("John","Bieber","Bieber",'m',"Son of Justin Bieber and Hanna Bieber.",new Address(58,"Centro Residences","59 Ang Mo Kio Ave 8",567752),new FamilyRelation());
        
        //Bieber Family
        FamilyMember p9 = new FamilyMember("George","Bieber","Bieber",'m',"Father of Jeremy Bieber and Grandfather of Justin Bieber",new Address(18,"Gardens By the Bay","18 Marina Bay Dr",18953),new FamilyRelation());
        FamilyMember p10 = new FamilyMember("Jeremy","Bieber","Bieber",'m',"Father of Justin Bieber", new Address(12,"Bishan View","Bishan Avenue 100",570111), new FamilyRelation());
        FamilyMember p11 = new FamilyMember("Janelle","Bieber","Levine",'f',"Justin's Bieber's older sister.",new Address(5,"Yishun Avenue 5","Happy Place",530555),new FamilyRelation());
        FamilyMember p16 = new FamilyMember("Adam","Levine","Levine",'m',"Brother-in-law of Justin Bieber and married to Janelle Bieber.",new Address(5,"Yishun Avenue 5","Happy Place",530555),new FamilyRelation());
        FamilyMember p17 = new FamilyMember("John","Levine","Levine",'m',"Son of Adam Levine and Janelle Bieber.",new Address(5,"Yishun Avenue 5","Happy Place",530555),new FamilyRelation());

        //Lau Family
        p1.GetFamilyRelation().UpdateSpouse(p2);
        p1.GetFamilyRelation().AddChild(p3);
        p1.GetFamilyRelation().AddChild(p5);
        p5.GetFamilyRelation().UpdateSpouse(p4);
        p5.GetFamilyRelation().AddChild(p6);

        
        p6.GetFamilyRelation().UpdateSpouse(p7);
        p6.GetFamilyRelation().AddChild(p8);
        
        //Bieber Family
        p9.GetFamilyRelation().AddChild(p10);

        p10.GetFamilyRelation().AddChild(p7);
        p10.GetFamilyRelation().AddChild(p11);
        p10.GetFamilyRelation().AddChild(p11);

        p11.GetFamilyRelation().UpdateSpouse(p16);
        p11.GetFamilyRelation().AddChild(p17);
        
        //Initiate GUI
        FamilyTree FT = new FamilyTree(p1);
        
        FT.SaveFile("Lau Family");
        
        Interface GUI = new Interface("Family Tree");
        GUI.setSize(1000, 600);
        GUI.setVisible(true);
    }
}