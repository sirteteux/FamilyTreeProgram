import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;

/**
 * @author Andreas Lau, started on 31/05/2021 A class that manages the GUI, elements and user-side interactions with family tree data.
 */
public class Interface extends JFrame {

    private final FamilyTree famtree;
    private FamilyMember currentPerson;

    private final FamilyTreePanel famtreepanel;
    private final PersonDetailsPanel persondetailspanel;

    private boolean editMode;
    private JMenuItem menuItemViewMode;
    private JMenuItem menuItemEditMode;

    private String filePath;
    private final String emptyText = "";

    public class ExitEvent implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    public class NewFileEvent implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            famtree.SetRootPerson(new FamilyMember(emptyText, emptyText, emptyText, 'm', emptyText,
                    new Address(0, emptyText, emptyText, 0), new FamilyRelation()));
            RefreshFrame();
        }
    }

    public class OpenFileEvent implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            filePath = "";
            try {
                FileBrowserOpen application = new FileBrowserOpen();
            } catch (IOException er) {
                // error
            }
            famtree.OpenFile(filePath);
            RefreshFrame();
        }
    }

    public class SaveFileEvent implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            CommitChanges();
            filePath = "";
            try {
                FileBrowserSave application = new FileBrowserSave();
            } catch (IOException er) {
                // error
            }
            famtree.SaveFile(filePath);
        }
    }

    public class ViewModeEvent implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            editMode = false;
            persondetailspanel.ChangeMode();
            menuItemViewMode.setEnabled(false);
            menuItemEditMode.setEnabled(true);
        }
    }

    public class EditModeEvent implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            editMode = true;
            persondetailspanel.ChangeMode();
            menuItemViewMode.setEnabled(true);
            menuItemEditMode.setEnabled(false);
        }
    }

    /**
     * Constructor
     */
    public Interface(String frameTitle) {

        super(frameTitle);

        // Variable initialisation
        famtree = new FamilyTree();
        // Check relation
        famtree.GetRootPerson().GetFamilyRelation();
        currentPerson = famtree.GetRootPerson();
        editMode = false;

        // Window Layout
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        GridLayout layout = new GridLayout(0, 2);
        layout.setHgap(5);
        frame.setLayout(layout);
        getContentPane().setBackground(Color.white);

        // Menu
        JMenuBar menuBar = MenuBar();
        menuBar.setBackground(Color.gray);
        frame.setJMenuBar(menuBar);

        // Person Details
        persondetailspanel = new PersonDetailsPanel();
        JPanel personDetailsPanel = persondetailspanel;
        persondetailspanel.ChangeMode();

        // Family
        famtreepanel = new FamilyTreePanel();
        JPanel familyTreePanel = famtreepanel;

        frame.add(familyTreePanel);
        frame.add(personDetailsPanel);

        // Exit
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });

    }

    /**
     * Creates menu bar for main frame.
     * 
     * @return Returns menuBar
     */
    private JMenuBar MenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuMode = new JMenu("Mode");

        JMenuItem menuItemNewFile = new JMenuItem("New");
        menuItemNewFile.addActionListener(new NewFileEvent());
        JMenuItem menuItemOpenFile = new JMenuItem("Open");
        menuItemOpenFile.addActionListener(new OpenFileEvent());
        JMenuItem menuItemSaveFile = new JMenuItem("Save");
        menuItemSaveFile.addActionListener(new SaveFileEvent());
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(new ExitEvent());

        menuItemViewMode = new JMenuItem("View Mode");
        menuItemViewMode.addActionListener(new ViewModeEvent());
        menuItemEditMode = new JMenuItem("Edit Mode");
        menuItemEditMode.addActionListener(new EditModeEvent());
        menuItemViewMode.setEnabled(false);
        menuItemEditMode.setEnabled(true);

        menuBar.add(menuFile);
        menuFile.add(menuItemNewFile);
        menuFile.add(menuItemOpenFile);
        menuFile.add(menuItemSaveFile);
        menuFile.add(menuItemExit);

        menuBar.add(menuMode);
        menuMode.add(menuItemViewMode);
        menuMode.add(menuItemEditMode);

        return menuBar;

    }

    public class FileBrowserOpen extends JFrame {

        private final JTextArea outputArea;

        /**
         * Constructor
         */
        public FileBrowserOpen() throws IOException {
            super("File Browser");
            outputArea = new JTextArea();
            add(new JScrollPane(outputArea));
            analyzePath();
        }

        public void analyzePath() throws IOException {
            Path path = getFileOrDirectoryPath();
        }

        private Path getFileOrDirectoryPath() {

            // Selection of a file or directory
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Family Tree File", "ftree");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = fileChooser.showOpenDialog(this);

            // Return if user clicked cancel
            if (result == JFileChooser.CANCEL_OPTION) {
                dispose();
            }

            filePath = fileChooser.getSelectedFile().toString();

            // Return selected file
            return fileChooser.getSelectedFile().toPath();

        }
    }

    public class FileBrowserSave extends JFrame {

        private final JTextArea outputArea;

        /**
         * Default constructor.
         */
        public FileBrowserSave() throws IOException {
            super("File Browser");
            outputArea = new JTextArea();
            add(new JScrollPane(outputArea));
            analyzePath();
        }

        public void analyzePath() throws IOException {
            Path path = getFileOrDirectoryPath();
        }

        private Path getFileOrDirectoryPath() {

            // Configure dialog allowing selection of a file or directory
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Family Tree File", "ftree");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(filter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setCurrentDirectory(new File("."));
            int result = fileChooser.showSaveDialog(this);

            // Return if user clicks cancel button
            if (result == JFileChooser.CANCEL_OPTION) {
                dispose();
            }

            filePath = fileChooser.getSelectedFile().toString();

            // Return selected file
            return fileChooser.getSelectedFile().toPath();

        }
    }

    public class PersonDetailsPanel extends JPanel {

        private final ArrayList<JTextField> memberTextList = new ArrayList<>();
        private final JComboBox dropDownGender;
        private final JTextArea txtLifeDescription;
        private final JTextArea txtChildren;

        private final JButton addButtonFather;
        private final JButton addButtonMother;
        private final JButton addButtonSpouse;
        private final JButton addButtonChildren;

        private final JButton deleteButtonFather;
        private final JButton deleteButtonMother;
        private final JButton deleteButtonSpouse;

        private final JButton deleteCurrentPerson;

        public class DeletePersonEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                DeletePerson(currentPerson);
            }
        }

        public class DeleteFatherEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                DeletePerson(currentPerson.GetFamilyRelation().GetFather());
            }
        }

        public class DeleteMotherEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                DeletePerson(currentPerson.GetFamilyRelation().GetMother());
            }
        }

        public class DeleteSpouseEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                DeletePerson(currentPerson.GetFamilyRelation().GetSpouse());
            }
        }

        public class AddFatherEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                if (currentPerson.GetFamilyRelation().GetFather().IsEmpty()) {
                    FamilyMember tempPerson = new FamilyMember(emptyText, emptyText, emptyText, 'm', emptyText,
                            new Address(0, emptyText, emptyText, 0), new FamilyRelation());
                    currentPerson.GetFamilyRelation().UpdateFather(tempPerson);
                    if (currentPerson.GetFamilyRelation().GetMother() != null
                            && !currentPerson.GetFamilyRelation().GetMother().IsEmpty()) {
                        currentPerson.GetFamilyRelation().GetMother().GetFamilyRelation().UpdateSpouse(tempPerson);
                    }
                    RefreshFrame(tempPerson);
                }
            }
        }

        public class AddMotherEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                if (currentPerson.GetFamilyRelation().GetMother().IsEmpty()) {
                    FamilyMember tempPerson = new FamilyMember(emptyText, emptyText, emptyText, 'f', emptyText,
                            new Address(0, emptyText, emptyText, 0), new FamilyRelation());
                    currentPerson.GetFamilyRelation().UpdateMother(tempPerson);
                    if (currentPerson.GetFamilyRelation().GetFather() != null
                            && !currentPerson.GetFamilyRelation().GetFather().IsEmpty()) {
                        currentPerson.GetFamilyRelation().GetFather().GetFamilyRelation().UpdateSpouse(tempPerson);
                    }
                    RefreshFrame(tempPerson);
                }
            }
        }

        public class AddSpouseEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                char tempGender = (Character.toLowerCase(currentPerson.GetGender()) == 'm' ? 'f' : 'm');

                if (currentPerson.GetFamilyRelation().GetSpouse().IsEmpty()) {
                    FamilyMember tempPerson = new FamilyMember(emptyText, emptyText, emptyText, tempGender, emptyText,
                            new Address(0, emptyText, emptyText, 0), new FamilyRelation());
                    currentPerson.GetFamilyRelation().UpdateSpouse(tempPerson);
                    tempPerson.GetFamilyRelation().SetChildren(currentPerson.GetFamilyRelation().GetChildren());
                    RefreshFrame(tempPerson);
                }
            }
        }

        public class AddChildEvent implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                FamilyMember tempPerson = new FamilyMember(emptyText, emptyText, emptyText, 'm', emptyText,
                        new Address(0, emptyText, emptyText, 0), new FamilyRelation());
                currentPerson.GetFamilyRelation().AddChild(tempPerson);
                tempPerson.GetFamilyRelation().UpdateParent(currentPerson);
                RefreshFrame(tempPerson);
            }
        }

        /**
         * Constructor
         */
        public PersonDetailsPanel() {

            Border border = BorderFactory.createLineBorder(Color.green, 3);
            TitledBorder titleBorder = BorderFactory.createTitledBorder(border, "Person Details");
            setBorder(titleBorder);
            setBackground(Color.white);
            setLayout(new GridBagLayout());
            GridBagConstraints constraint = new GridBagConstraints();

            // Set up scroll panel
            JPanel scrollPanel = new JPanel();
            scrollPanel.setLayout(new GridBagLayout());
            JScrollPane scrollBar = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollBar.getVerticalScrollBar().setUnitIncrement(10);
            constraint.weightx = 1;
            constraint.weighty = 1;
            constraint.fill = GridBagConstraints.BOTH;
            add(scrollBar, constraint);

            // Set up labels and text fields
            constraint = new GridBagConstraints();

            // Padding
            constraint.gridx = 0;
            constraint.gridy = 0;
            scrollPanel.add(new JLabel(" "), constraint);

            // Person
            JLabel personPanelLabel = new JLabel("Person :");
            personPanelLabel.setFont(new Font("Arial", Font.BOLD, 18));
            constraint.fill = GridBagConstraints.HORIZONTAL;
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(personPanelLabel, constraint);

            // Blank
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel(" "), constraint);

            // First Name
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("First Name :"), constraint);

            JTextField textFirstName = new JTextField();
            textFirstName.setColumns(10);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(textFirstName, constraint);
            memberTextList.add(textFirstName);

            // Surname (birth)
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Surname (birth) :"), constraint);

            JTextField textSurnameBirth = new JTextField();
            textSurnameBirth.setColumns(10);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(textSurnameBirth, constraint);
            memberTextList.add(textSurnameBirth);

            // Surname (marriage)
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Surname (marriage) :"), constraint);

            JTextField textSurnameMarriage = new JTextField();
            textSurnameMarriage.setColumns(10);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(textSurnameMarriage, constraint);
            memberTextList.add(textSurnameMarriage);

            // Gender
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Gender :"), constraint);

            String[] genderOptions = { "Male", "Female" };
            dropDownGender = new JComboBox(genderOptions);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(dropDownGender, constraint);

            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Life Description :"), constraint);

            txtLifeDescription = new JTextArea();
            txtLifeDescription.setBorder(textFirstName.getBorder());
            JScrollPane scrollPane = new JScrollPane(txtLifeDescription, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            txtLifeDescription.setRows(3);
            txtLifeDescription.setLineWrap(true);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(scrollPane, constraint);

            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel(" "), constraint);

            // Street Address
            JLabel streetAddressPanelLabel = new JLabel("Address :");
            streetAddressPanelLabel.setFont(new Font("Arial", Font.BOLD, 18));
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(streetAddressPanelLabel, constraint);

            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel(" "), constraint);

            // Street Number
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Street No :"), constraint);

            JTextField textStreetNo = new JTextField();
            textStreetNo.setColumns(10);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(textStreetNo, constraint);
            memberTextList.add(textStreetNo);

            // Street Name
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Street Name :"), constraint);

            JTextField textStreetName = new JTextField();
            textStreetName.setColumns(10);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(textStreetName, constraint);
            memberTextList.add(textStreetName);

            // Suburb
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Suburb :"), constraint);

            JTextField textSuburb = new JTextField();
            textSuburb.setColumns(10);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(textSuburb, constraint);
            memberTextList.add(textSuburb);

            // Post Code
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.insets = new Insets(0, 0, 0, 10);
            scrollPanel.add(new JLabel("Postcode :"), constraint);

            JTextField textPostcode = new JTextField();
            textPostcode.setColumns(10);
            constraint.gridx = 1;
            constraint.insets = new Insets(0, 0, 0, 0);
            scrollPanel.add(textPostcode, constraint);
            memberTextList.add(textPostcode);

            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel(" "), constraint);

            // Family
            String emptyFamField = ".........";

            JLabel familyPanelLabel = new JLabel("Family:");
            familyPanelLabel.setFont(new Font("Arial", Font.BOLD, 18));
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(familyPanelLabel, constraint);

            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel(" "), constraint);

            // Father
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel("Father:"), constraint);

            JTextField txtFather = new JTextField(emptyFamField);
            txtFather.setPreferredSize(new Dimension(180, 20));
            txtFather.setEditable(false);
            constraint.gridx = 1;
            scrollPanel.add(txtFather, constraint);
            memberTextList.add(txtFather);

            addButtonFather = new JButton("Add");
            addButtonFather.setBorder(null);
            addButtonFather.addActionListener(new AddFatherEvent());

            constraint.gridx = 2;
            constraint.insets = new Insets(0, (10), 0, 0);
            scrollPanel.add(addButtonFather, constraint);

            deleteButtonFather = new JButton("Delete");
            deleteButtonFather.setBorder(null);
            deleteButtonFather.addActionListener(new DeleteFatherEvent());

            constraint.gridx = 3;
            scrollPanel.add(deleteButtonFather, constraint);

            // Mother
            constraint.insets = new Insets(0, 0, 0, 0);
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel("Mother:"), constraint);

            JTextField textMother = new JTextField(emptyFamField);
            textMother.setPreferredSize(new Dimension(180, 20));
            textMother.setEditable(false);
            constraint.gridx = 1;
            scrollPanel.add(textMother, constraint);
            memberTextList.add(textMother);

            addButtonMother = new JButton("Add");
            addButtonMother.setBorder(null);
            addButtonMother.addActionListener(new AddMotherEvent());

            constraint.gridx = 2;
            constraint.insets = new Insets(0, (10), 0, 0);
            scrollPanel.add(addButtonMother, constraint);

            deleteButtonMother = new JButton("Delete");
            deleteButtonMother.setBorder(null);
            deleteButtonMother.addActionListener(new DeleteMotherEvent());

            constraint.gridx = 3;
            scrollPanel.add(deleteButtonMother, constraint);

            // Spouse
            constraint.insets = new Insets(0, 0, 0, 0);
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel("Spouse:"), constraint);

            JTextField textSpouse = new JTextField(emptyFamField);
            textSpouse.setPreferredSize(new Dimension(180, 20));
            textSpouse.setEditable(false);
            constraint.gridx = 1;
            scrollPanel.add(textSpouse, constraint);
            memberTextList.add(textSpouse);

            addButtonSpouse = new JButton("Add");
            addButtonSpouse.setBorder(null);
            addButtonSpouse.addActionListener(new AddSpouseEvent());

            constraint.gridx = 2;
            constraint.insets = new Insets(0, (10), 0, 0);
            scrollPanel.add(addButtonSpouse, constraint);

            deleteButtonSpouse = new JButton("Delete");
            deleteButtonSpouse.setBorder(null);
            deleteButtonSpouse.addActionListener(new DeleteSpouseEvent());

            constraint.gridx = 3;
            scrollPanel.add(deleteButtonSpouse, constraint);

            // Children
            constraint.insets = new Insets(0, 0, 0, 0);
            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel("Children:"), constraint);

            txtChildren = new JTextArea(emptyFamField);
            txtChildren.setLineWrap(true);
            txtChildren.setBorder(textFirstName.getBorder()); // adds border like text fields
            txtChildren.setBackground(txtFather.getBackground()); // to get non-enable background colour
            txtChildren.setEditable(false);
            constraint.gridx = 1;
            scrollPanel.add(txtChildren, constraint);

            addButtonChildren = new JButton("Add");
            addButtonChildren.setBorder(null);
            addButtonChildren.addActionListener(new AddChildEvent());

            constraint.gridx = 2;
            constraint.insets = new Insets(0, (10), 0, 0);
            scrollPanel.add(addButtonChildren, constraint);

            // Delete current person

            deleteCurrentPerson = new JButton("Delete Current Person");
            deleteCurrentPerson.addActionListener(new DeletePersonEvent());

            constraint.insets = new Insets(10, 0, 0, 0);
            constraint.gridx = 0;
            constraint.gridy++;
            constraint.gridwidth = 4;
            scrollPanel.add(deleteCurrentPerson, constraint);

            constraint.gridx = 0;
            constraint.gridy++;
            scrollPanel.add(new JLabel(" "), constraint);

        }

        /**
         * Enables/disables buttons and text fields based on current mode.
         */
        private void ChangeMode() {
            dropDownGender.setEnabled(editMode);
            txtLifeDescription.setEnabled(editMode);
            for (int i = 0; i < memberTextList.size() - 3; i++) {
                memberTextList.get(i).setEditable(editMode);
            }
            addButtonFather.setEnabled(editMode);
            addButtonMother.setEnabled(editMode);
            addButtonSpouse.setEnabled(editMode);
            addButtonChildren.setEnabled(editMode);
            deleteButtonFather.setEnabled(editMode);
            deleteButtonMother.setEnabled(editMode);
            deleteButtonSpouse.setEnabled(editMode);
            deleteCurrentPerson.setEnabled(editMode);
        }

        /**
         * Removes person and refreshes panels.
         * @param person
         */
        private void DeletePerson(FamilyMember person) {
            famtree.RemovePerson(person);
            famtreepanel.PrintFamilyTree();

            currentPerson = famtree.GetRootPerson();
            persondetailspanel.PrintMember(currentPerson);

            UpdateBorders(Color.BLACK);
        }

        /**
         * Prints family member information in text fields
         * @param person
         */
        private void PrintMember(FamilyMember person) {

            if (Character.toLowerCase(person.GetGender()) == 'm') {
                dropDownGender.setSelectedItem("Male");
            } else if (Character.toLowerCase(person.GetGender()) == 'f') {
                dropDownGender.setSelectedItem("Female");
            }

            memberTextList.get(0).setText(person.GetFirstName());
            memberTextList.get(1).setText(person.GetSurnameAtBirth());
            memberTextList.get(2).setText(person.GetSurnameMarriage());
            
            // Gender drop down box placement
            txtLifeDescription.setText(person.GetLifeDescription());
            memberTextList.get(3).setText(String.valueOf(person.GetStreetAddress().GetStreetNo()));
            memberTextList.get(4).setText(person.GetStreetAddress().GetStreetName());
            memberTextList.get(5).setText(person.GetStreetAddress().GetSubUrb());
            memberTextList.get(6).setText(String.valueOf(person.GetStreetAddress().GetPostCode()));

            FamilyMember tempFather = person.GetFamilyRelation().GetFather();
            FamilyMember tempMother = person.GetFamilyRelation().GetMother();
            FamilyMember tempSpouse = person.GetFamilyRelation().GetSpouse();
            ArrayList<FamilyMember> tempChildren = person.GetFamilyRelation().GetChildren();

            String txtFather = ".........";
            String txtMother = ".........";
            String txtSpouse = ".........";
            String txtChildren = "";

            if (!tempFather.IsEmpty()) {
                txtFather = tempFather.GetFirstName() + " " + tempFather.GetSurnameMarriage();
            }
            if (!tempMother.IsEmpty()) {
                txtMother = tempMother.GetFirstName() + " " + tempMother.GetSurnameMarriage();
            }
            if (!tempSpouse.IsEmpty()) {
                txtSpouse = tempSpouse.GetFirstName() + " " + tempSpouse.GetSurnameMarriage();
            }

            for (int i = 0; i < tempChildren.size(); i++) {
                if (!tempChildren.get(i).IsEmpty()) {
                    if (i != 0) {
                        txtChildren += "\n";
                    }
                    txtChildren += tempChildren.get(i).GetFirstName() + " " + tempChildren.get(i).GetSurnameMarriage();
                }
            }
            if (tempChildren.isEmpty()) {
                txtChildren = ".........";
            }

            memberTextList.get(7).setText(txtFather);
            memberTextList.get(8).setText(txtMother);
            memberTextList.get(9).setText(txtSpouse);
            this.txtChildren.setText(txtChildren);

        }

    }

    public class FamilyTreePanel extends JPanel {

        private JPanel scrollPanel;

        public class memberButtonEvent implements ActionListener {
            FamilyMember person;
            Color color;

            public memberButtonEvent(FamilyMember person, Color color) {
                this.person = person;
                this.color = color;
            }

            public void actionPerformed(ActionEvent event) {
                CommitChanges();
                famtreepanel.PrintFamilyTree();
                currentPerson = person;
                persondetailspanel.PrintMember(person);
                UpdateBorders(color);
            }
        }

        /**
         * Constructor
         */
        FamilyTreePanel() {
            // Set layout
            TitledBorder titleBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green, 3),
                    "Family Tree");
            setBorder(titleBorder);
            setBackground(Color.white);
            setLayout(new GridBagLayout());
            CreateScrollPanel();
        }

        /**
         * Sets up the scroll panel for Family Tree Panel to be used with button diagram
         */
        private void CreateScrollPanel() {

            scrollPanel = new JPanel();
            scrollPanel.setLayout(new GridBagLayout());
            JScrollPane scrollBar = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollBar.getVerticalScrollBar().setUnitIncrement(10);
            GridBagConstraints constraint = new GridBagConstraints();
            constraint.weightx = 1;
            constraint.weighty = 1;
            constraint.fill = GridBagConstraints.BOTH;
            add(scrollBar, constraint);

        }

        /**
         * Print family tree. This method starts at the root person and traverses through all attached nodes.
         */
        public void PrintFamilyTree() {

            removeAll();
            CreateScrollPanel();

            FamilyMember person = famtree.GetRootPerson();

            if (!person.IsEmpty()) {

                if (!person.GetFamilyRelation().GetFather().IsEmpty()) {
                    PrintFamilyMember(person.GetFamilyRelation().GetFather(), "Father", 1);
                }
                if (!person.GetFamilyRelation().GetMother().IsEmpty()) {
                    PrintFamilyMember(person.GetFamilyRelation().GetMother(), "Mother", 1);
                }

                PrintFamilyMember(person, "Self", 2);

                if (!person.GetFamilyRelation().GetSpouse().IsEmpty()) {
                    PrintFamilyMember(person.GetFamilyRelation().GetSpouse(), "Spouse", 2);
                }

                for (int i = 0; i < person.GetFamilyRelation().GetChildren().size(); i++) {
                    PrintFamilyMembers(person.GetFamilyRelation().GetChildren().get(i), 3);
                }

            }

            revalidate();
            repaint();

        }

        /**
         * Print the family tree hierarchy
         * 
         * @param person current person being printed
         * @param depth depth in family tree
         */
        private void PrintFamilyMembers(FamilyMember person, int depth) {

            String text = "";
            if (!person.IsEmpty()) {

                PrintFamilyMember(person, "Child", depth);

                if (!person.GetFamilyRelation().GetSpouse().IsEmpty()) {

                    if (!person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetFather().IsEmpty()) {
                        PrintFamilyMember(person.GetFamilyRelation().GetSpouse(), "🡓" + "Spouse", depth);
                        PrintFamilyMember("\u2937", depth);
                        PrintFamilyMembers(person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetFather(), person.GetFamilyRelation().GetSpouse(),
                                depth + 5);
                        // Set spacing of char relative to child if exists
                        if (person.GetFamilyRelation().GetChildren().isEmpty()) {
                            PrintFamilyMember("🡓", depth);
                        } else {
                            PrintFamilyMember("🡓", depth + 1);
                        }
                    } else if (!person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetMother().IsEmpty()) {
                        PrintFamilyMember(person.GetFamilyRelation().GetSpouse(), "🡓" + "Spouse", depth);
                        PrintFamilyMember("\u2937", depth);
                        PrintFamilyMembers(person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetMother(), person.GetFamilyRelation().GetSpouse(),
                                depth + 5);
                        // sets spacing of char relative to child if exists
                        if (person.GetFamilyRelation().GetChildren().isEmpty()) {
                            PrintFamilyMember("🡓", depth);
                        } else {
                            PrintFamilyMember("🡓", depth + 1);
                        }
                    } else {
                        PrintFamilyMember(person.GetFamilyRelation().GetSpouse(), "Spouse", depth); // original
                    }
                }

                for (int i = 0; i < person.GetFamilyRelation().GetChildren().size(); i++) {
                    PrintFamilyMembers(person.GetFamilyRelation().GetChildren().get(i), depth + 1);
                }

            }

        }

        /**
        * Print family tree hierarchy
        * 
        * @param person current person being printed
        * @param child  current persons child from previous print (will not be reprinted)
        * @param depth depth in family tree
        */
        private void PrintFamilyMembers(FamilyMember person, FamilyMember child, int depth) {

            if (!person.GetFamilyRelation().GetFather().IsEmpty()) {
                PrintFamilyMember(person, "🡓" + ((Character.toLowerCase(person.GetGender()) == 'm') ? "Father" : "Mother"),
                        depth);
                PrintFamilyMember("\u2937", depth);
                PrintFamilyMembers(person.GetFamilyRelation().GetFather(), person, depth + 5);
                // Sets spacing of char relative to child if exists
                if (person.GetFamilyRelation().GetSpouse().IsEmpty()) {
                    if (person.GetFamilyRelation().GetChildren().size() == 1) {
                        PrintFamilyMember("🡓", depth);
                    } else {
                        PrintFamilyMember("🡓", depth + 1);
                    }
                } else {
                    PrintFamilyMember("🡓", depth);
                }
            } else if (!person.GetFamilyRelation().GetMother().IsEmpty()) {
                PrintFamilyMember(person, "🡓" + ((Character.toLowerCase(person.GetGender()) == 'm') ? "Father" : "Mother"),
                        depth);
                PrintFamilyMember("\u2937", depth);
                PrintFamilyMembers(person.GetFamilyRelation().GetMother(), person, depth + 5);
                // Set spacing of char relative to child if exists
                if (person.GetFamilyRelation().GetSpouse().IsEmpty()) {
                    if (person.GetFamilyRelation().GetChildren().size() == 1) {
                        PrintFamilyMember("🡓", depth);
                    } else {
                        PrintFamilyMember("🡓", depth + 1);
                    }
                } else {
                    PrintFamilyMember("🡓", depth);
                }
            } else {
                PrintFamilyMember(person, ((Character.toLowerCase(person.GetGender()) == 'm') ? "Father" : "Mother"), depth);
            }

            if (!person.GetFamilyRelation().GetSpouse().IsEmpty()) {

                if (!person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetFather().IsEmpty()) {
                    PrintFamilyMember(person.GetFamilyRelation().GetSpouse(),
                            "🡓" + ((Character.toLowerCase(person.GetFamilyRelation().GetSpouse().GetGender()) == 'm') ? "Father"
                                    : "Mother"),
                            depth);
                    PrintFamilyMember("\u2937", depth);
                    PrintFamilyMembers(person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetFather(), person.GetFamilyRelation().GetSpouse(),
                            depth + 5);
                    // sets spacing of char relative to child if exists
                    if (person.GetFamilyRelation().GetChildren().isEmpty()) {
                        PrintFamilyMember("🡓", depth);
                    } else {
                        PrintFamilyMember("🡓", depth + 1);
                    }
                } else if (!person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetMother().IsEmpty()) {
                    PrintFamilyMember(person.GetFamilyRelation().GetSpouse(),
                            "🡓" + ((Character.toLowerCase(person.GetFamilyRelation().GetSpouse().GetGender()) == 'm') ? "Father"
                                    : "Mother"),
                            depth);
                    PrintFamilyMember("\u2937", depth);
                    PrintFamilyMembers(person.GetFamilyRelation().GetSpouse().GetFamilyRelation().GetMother(), person.GetFamilyRelation().GetSpouse(),
                            depth + 5);
                    // Sets spacing of char relative to child if exists
                    if (person.GetFamilyRelation().GetChildren().isEmpty()) {
                        PrintFamilyMember("🡓", depth);
                    } else {
                        PrintFamilyMember("🡓", depth + 1);
                    }
                } else {
                    PrintFamilyMember(person.GetFamilyRelation().GetSpouse(),
                            ((Character.toLowerCase(person.GetFamilyRelation().GetSpouse().GetGender()) == 'm') ? "Father"
                                    : "Mother"),
                            depth);
                }

            }

            for (int i = 0; i < person.GetFamilyRelation().GetChildren().size(); i++) {
                if (person.GetFamilyRelation().GetChildren().get(i) != child) {
                    PrintFamilyMembers(person.GetFamilyRelation().GetChildren().get(i), depth + 1);
                }
            }

        }

        /**
         * Prints empty instance of family members and calls when family tree traverses
         * up hierarchy.
         * 
         * @param text  text for formatting
         * @param depth depth in family tree
         */
        private void PrintFamilyMember(String text, int depth) {
            JLabel hierarchyBreak = new JLabel(text, JLabel.CENTER);
            hierarchyBreak.setBackground(Color.DARK_GRAY);
            hierarchyBreak.setPreferredSize(new Dimension(180, 10));

            GridBagConstraints constraint = new GridBagConstraints();
            constraint.gridx = 0;
            constraint.anchor = GridBagConstraints.LINE_START;
            constraint.insets = new Insets(5, (20 * depth), 0, 0);
            scrollPanel.add(hierarchyBreak, constraint);
        }

        /**
         * Prints a family members button, including setting up events and formatting indentation depending on its depth in the family tree.
         * @param person family member, family member's relevant title, depth in family tree
         */
        private void PrintFamilyMember(FamilyMember person, String title, int depth) {

            String tempGender = "\u26a7";
            if (Character.toLowerCase(person.GetGender()) == 'm') {
                tempGender = "\u2642";
            } else if (Character.toLowerCase(person.GetGender()) == 'f') {
                tempGender = "\u2640";
            }
            String labelText = " " + tempGender + "\uFF5c" + title + ": " + person.GetFirstName() + " "
                    + person.GetSurnameMarriage();

            Color randColor = GetColourFromSeed(depth);
            Border border = BorderFactory.createLineBorder(randColor);

            JButton memberButton = new JButton(labelText);
            memberButton.setPreferredSize(new Dimension(180, 20));
            memberButton.setBorder(border);
            memberButton.setHorizontalAlignment(SwingConstants.LEFT);
            memberButton.addActionListener(new memberButtonEvent(person, randColor));

            GridBagConstraints constraint = new GridBagConstraints();
            constraint.gridx = 0;
            constraint.anchor = GridBagConstraints.LINE_START;
            constraint.insets = new Insets(5, (20 * depth), 0, 0);

            scrollPanel.add(memberButton, constraint);

        }

    }

    /**
     * Updates person's data in text fields
     */
    private void CommitChanges() {

        if (currentPerson.GetFamilyRelation().GetSpouse().IsEmpty()) {
            currentPerson.SetGender(
                    (persondetailspanel.dropDownGender.getSelectedItem().toString().equals("Male") ? 'm' : 'f'));

            // change mother/father role to children
            ArrayList<FamilyMember> children = currentPerson.GetFamilyRelation().GetChildren();
            if (Character.toLowerCase(currentPerson.GetGender()) == 'm') {
                for (int i = 0; i < children.size(); i++) {
                    children.get(i).GetFamilyRelation().UpdateMother(new FamilyMember());
                    children.get(i).GetFamilyRelation().UpdateFather(currentPerson);
                }
            } else if (Character.toLowerCase(currentPerson.GetGender()) == 'f') {
                for (int i = 0; i < children.size(); i++) {
                    children.get(i).GetFamilyRelation().UpdateFather(new FamilyMember());
                    children.get(i).GetFamilyRelation().UpdateMother(currentPerson);
                }
            }

        }

        currentPerson.SetFirstName(persondetailspanel.memberTextList.get(0).getText());
        currentPerson.SetSurnameAtBirth(persondetailspanel.memberTextList.get(1).getText());
        currentPerson.SetSurnameMarriage(persondetailspanel.memberTextList.get(2).getText());
        currentPerson.SetLifeDescription(persondetailspanel.txtLifeDescription.getText());

        currentPerson.SetStreetAddress(persondetailspanel.memberTextList.get(3).getText(),
                persondetailspanel.memberTextList.get(4).getText(), persondetailspanel.memberTextList.get(5).getText(),
                persondetailspanel.memberTextList.get(6).getText());
    }

    /**
     * Reprints frames with current family tree data.
     */
    private void RefreshFrame() {
        currentPerson = famtree.GetRootPerson();
        famtreepanel.PrintFamilyTree();
        persondetailspanel.PrintMember(currentPerson);
        UpdateBorders(Color.BLACK);
    }

    /**
     * Reprints frames with current family tree data.
     * 
     * @param person person to set to current person
     */
    private void RefreshFrame(FamilyMember person) {
        currentPerson = person;
        famtreepanel.PrintFamilyTree();
        persondetailspanel.PrintMember(currentPerson);
        UpdateBorders(Color.BLACK);
    }

    /**
     * Updates panel borders with new color.
     * @param color color to add to borders
     */
    private void UpdateBorders(Color color) {
        CompoundBorder compoundBorder = new CompoundBorder(BorderFactory.createLineBorder(color),
                BorderFactory.createLineBorder(Color.BLACK, 3));
        TitledBorder titleBorder = BorderFactory.createTitledBorder(compoundBorder, "Family Tree");
        famtreepanel.setBorder(titleBorder);
        titleBorder = BorderFactory.createTitledBorder(compoundBorder, "Person Details");
        persondetailspanel.setBorder(titleBorder);
    }

    /**
     * Produces a consistent, seemingly random color based on number passed in.
     * @param number number (as seed for color)
     * @return Returns Color
     */
    private Color GetColourFromSeed(int number) {

        int red = 0;
        int green = 0;
        int blue = 0;

        for (int i = 0; i < (number % 6); i++) {
            if ((number % 6) == 0) {
                if (blue < 2) {
                    blue++;
                } else {
                    if (green < 2) {
                        green++;
                    } else {
                        if (red < 2) {
                            red++;
                        }
                    }
                }
            } else if ((number % 5) == 0) {
                if (green < 2) {
                    green++;
                } else {
                    if (red < 2) {
                        red++;
                    } else {
                        if (blue < 2) {
                            blue++;
                        }
                    }
                }
            } else if ((number % 4) == 0) {
                if (red < 2) {
                    red++;
                } else {
                    if (blue < 2) {
                        blue++;
                    } else {
                        if (green < 2) {
                            green++;
                        }
                    }
                }
            } else if ((number % 3) == 0) {
                if (green < 2) {
                    green++;
                } else {
                    if (blue < 2) {
                        blue++;
                    } else {
                        if (red < 2) {
                            red++;
                        }
                    }
                }
            } else if ((number % 2) == 0) {
                if (blue < 2) {
                    blue++;
                } else {
                    if (red < 2) {
                        red++;
                    } else {
                        if (green < 2) {
                            green++;
                        }
                    }
                }
            } else {
                if (red < 2) {
                    red++;
                }
                if (green < 2) {
                    green++;
                }
                if (blue < 2) {
                    blue++;
                }
                if (red < 2) {
                    red++;
                } else {
                    if (green < 2) {
                        green++;
                    } else {
                        if (blue < 2) {
                            blue++;
                        }
                    }
                }
            }
        }

        red *= 128;
        green *= 128;
        blue *= 128;

        if (red == 256) {
            red--;
        }
        if (green == 256) {
            green--;
        }
        if (blue == 256) {
            blue--;
        }

        return new Color(red, green, blue);
    }
}
