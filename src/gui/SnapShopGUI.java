/*
 * TCSS 305 - Assignment 4: SnapShop
 */
package gui;


//import statements
import filters.EdgeDetectFilter; //filters imports
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;
import java.awt.BorderLayout; //java.awt imports
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon; //swing imports
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



/**
 * This program will be able to edit selected photos through a variety of filters.
 * This program implements the use of JFrame, JPanel, and other swing components.
 * @author Andrew Joshua Loria
 * @version 11/1/2017
 */
public class SnapShopGUI {
    
    /**Number of filters in this program.*/
    public static final int NUM_FILTERS = 7;

    /**Screen size.*/
    public static final Dimension MIN_SIZE = new Dimension(500, 500);
    
    /**Get screen size of device.*/
    private static final Dimension DIM = Toolkit.getDefaultToolkit().getScreenSize();

    /**Get screen width.*/
    public static final int WIDTH = DIM.width;
    
    /**Get screen height.*/
    private static final int HEIGHT = DIM.height;

    /**Creates new JFrame.*/
    private JFrame myJframe;   

    /**South panel.*/
    private JPanel mySouthPan;
    
    /**West panel.*/
    private JPanel myWestPan;
    
    /**Center label.*/
    private JLabel myCenter;
    
    /**Pixel image.*/
    private PixelImage myPixelImage;
    
    /**File chooser.*/
    private final JFileChooser myFileChooser;
    
    /**File that wants to be loaded.*/
    private File myFile;
    
    /**List of Buttons.*/
    private final List<JButton> myButtons;
    
    /**List of Filters.*/
    private final List<Filter> myFilters;

    /**Open button.*/
    private JButton myOpenButton;

    /**Save button.*/
    private JButton mySaveButton;

    /**Close button.*/
    private JButton myCloseButton;
  
    /**JFileChooser represented through an integer.*/
    private int myFileChooserInt;

    
    /**
     * Constructor for SnapShopGUI.
     * This is where file chooser and the array lists are initialized.
     */
    public SnapShopGUI() {
        myFileChooser = new JFileChooser(".");
        //initialize the array lists
        //array list of filters
        myFilters = new ArrayList<Filter>();
        //array list of buttons
        myButtons = new ArrayList<JButton>();
         
    }
    
    /**
     * This start method will set up the components required for the GUI,
     * make the buttons, make the JFrame visible, cause 
     * the window to be fit to its preferred size, and close when
     * the user clicks the X button.
     */
    public void start() {        
        
        setupComponents();
        makeFilterButtons();
        makeOptButtons();        
    
        //sets default size and center JFrame
        adjust();
        
        myJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myJframe.setVisible(true);
        
    }
    
    /**
     * A helper method where the size is set to default and the 
     * location of the window is right in the center. This method
     * is called multiple times instead of having to type it out every time.
     */
    public void adjust() {
        myJframe.setMinimumSize(MIN_SIZE);
        myJframe.setLocation(WIDTH / 2 - myJframe.getWidth() / 2, 
                            HEIGHT / 2 - myJframe.getHeight() / 2);
        myJframe.pack();
        
    }
    
    /**
     * This creates a new label, creates the panels, and adds them to
     * the frame.
     */
    private void setupComponents() {

        myJframe = new JFrame("TCSS 305 SnapShop");
        myJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myJframe.setLayout(new BorderLayout());
        
        myCenter = new JLabel();
        //create placeholder for image
        final JPanel centerPan = new JPanel(new FlowLayout());
        //add it to the center panel
        centerPan.add(myCenter, BorderLayout.CENTER);
        myJframe.add(centerPan, BorderLayout.CENTER);

        mySouthPan = new JPanel(new FlowLayout());
        myJframe.add(mySouthPan, BorderLayout.SOUTH);
        
        final JPanel northPan = new JPanel(new FlowLayout());
        myJframe.add(northPan, BorderLayout.NORTH);
       
        myWestPan = new JPanel(new GridLayout(0, 1));
        myJframe.add(myWestPan, BorderLayout.WEST);
        
        myJframe.pack();
        myJframe.setLocationRelativeTo(null);
        myJframe.setVisible(true);
    }
    
    
    /**
     * This method creates filter button and adds it to the west panel.
     */
    private void makeFilterButtons() {
        
        addToArray(); //call helper method
        for (int i = 0; i < NUM_FILTERS; i++) {
            //for each filter, add it to myButtons and the west panel
            myButtons.add(i, makeFilterButton(myFilters.get(i)));
            myWestPan.add(myButtons.get(i)); //add to west panel
        }
    }
    
    /**
     * Helper method for makeFilterButtons.
     * This adds each filter to the array list called myFilters.
     */
    private void addToArray() {
        myFilters.add(new EdgeDetectFilter());
        myFilters.add(new EdgeHighlightFilter());
        myFilters.add(new FlipHorizontalFilter());
        myFilters.add(new FlipVerticalFilter());
        myFilters.add(new GrayscaleFilter());
        myFilters.add(new SharpenFilter());
        myFilters.add(new SoftenFilter());
        
    }

    /**
     * This method contains a FiltersActionListener class which creates an ActionListener.
     * Also, specific filters are assigned for each button.
     * 
     * @param theFilter The filter that is called from the createFilterButtons method.
     * @return Button with assigned filter.
     */
    private JButton makeFilterButton(final Filter theFilter) {
        
        // A button that receive a name from description of the file.
        final JButton filterButton = new JButton(theFilter.getDescription());
        
        /**
         * This class is an inner class. In this class, an ActionListener 
         * for each filter button is made. This is also the class where 
         * the actionPerformed method is overriden from the ActionListener 
         * class.
         * 
         * @author Andrew Joshua Loria
         * @version 11/3/17
         */
        class FiltersActionListener implements ActionListener {
            
            /**
             * 
             * In this method, the filter is applied to the image.
             * If a filter is already applied to the image and another filter wants
             * to be applied, it will remember the last filter and build on top 
             * @param theEvent The current filter for an image.
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                theFilter.filter(myPixelImage);
                //filter the image and add it to JLabel myCenter
                myCenter.setIcon(new ImageIcon(myPixelImage));
                myJframe.pack();
            }
        }
        
        filterButton.addActionListener(new FiltersActionListener());
        //when program is initially run, filter is disabled
        filterButton.setEnabled(false); 
        
        return filterButton;
    }
 
    /**
     * This method creates the buttons Open, Save, and Close 
     * and adds them to the South Panel.
     */
    private void makeOptButtons() {
        
        //Open
        myOpenButton = new JButton("Open...");
        myOpenButton.setEnabled(true);
        mySouthPan.add(myOpenButton);
        myOpenButton.addActionListener(new SPanelButtonsActionListener());

        //Save
        mySaveButton = new JButton("Save As...");
        mySaveButton.setEnabled(false); 
        mySouthPan.add(mySaveButton);
        mySaveButton.addActionListener(new SPanelButtonsActionListener());

        //Close
        myCloseButton = new JButton("Close Image");
        myCloseButton.setEnabled(false);
        mySouthPan.add(myCloseButton);
        myCloseButton.addActionListener(new SPanelButtonsActionListener());
        
    }
    
    
    /**
     * This inner class creates an ActionListener
     * for the buttons in the South panel: open, save, and close.
     * Also, in this class, specific actions (open, save, close) 
     * are assigned for each button.
     * 
     * @author Andrew Joshua Loria
     * @version 11/3/17
     */
    class SPanelButtonsActionListener implements ActionListener {
        
        /**
         * This method overrides ActionListener's actionPerformed.
         * This is where the action method is called for each of the option buttons
         * (open, save, close).
         * 
         * @param theEvent A button that handles this event.
         */
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            
            myFile = myFileChooser.getSelectedFile();
            
            if (theEvent.getSource() == myOpenButton) {
                openAction(); 
            } else if (theEvent.getSource() == mySaveButton) {
                saveAction();
            }  else if (theEvent.getSource() == myCloseButton) {
                closeAction();
            }
        }
        
        /**
         * This method will cause the GUI to respond to when the open button is clicked.
         */
        private void openAction() {
            myFileChooserInt = myFileChooser.showOpenDialog(myJframe);
            
            if (myFileChooserInt == JFileChooser.APPROVE_OPTION) {
                myFile = myFileChooser.getSelectedFile();
                
                try {
                    myPixelImage = PixelImage.load(myFile);
                    myCenter.setIcon(new ImageIcon(myPixelImage));
                    myJframe.pack();
                    // an image file was opened successfully so enable option buttons
                    mySaveButton.setEnabled(true);
                    myCloseButton.setEnabled(true);
                    
                    for (int i = 0; i < NUM_FILTERS; i++) {
                        //enabling the filter buttons
                        myButtons.get(i).setEnabled(true);
                    }
                
                } catch (final IOException e) {
                    //creates the error message if file not valid
                    JOptionPane.showMessageDialog(null,
                                                  "The selected file"
                                                  + " did not contain an image!",
                                                  "Error!",
                                                  JOptionPane.ERROR_MESSAGE);
                }
                adjust();
                myJframe.setMinimumSize(new Dimension(myJframe.getWidth(), 
                                                      myJframe.getHeight()));
                
                myJframe.setLocation(WIDTH / 2 - myJframe.getWidth() / 2, 
                                    HEIGHT / 2 - myJframe.getHeight() / 2);
                
            }
            
        }
        
        /**
         * This method will cause the GUI to respond to when the save button is clicked.
         */
        private void saveAction() {
            myFileChooserInt  = myFileChooser.showSaveDialog(null);
           
            if (myFileChooserInt == JFileChooser.APPROVE_OPTION && checkVal()) {
                try {
                    //saves file successfully
                    myPixelImage.save(myFileChooser.getSelectedFile());                    
                
                } catch (final IOException e) {
                    JOptionPane.showMessageDialog(null, "The file can't be saved!");
                }
            }
                        
        }
        
        /**
         * This method checks whether or not it is okay 
         * to save the file with the given name. This also warns the user
         * if they are about to overwrite the file and asks them if they want to
         * overwrite the file.
         * @return boolean True if it is okay to save the file with intended name.
         */
        private boolean checkVal() {
            boolean fileIsValid = false;
            
            final File outputFile = myFileChooser.getSelectedFile();
            if (outputFile.exists()) {
                //file name has same name as an existing file
                final int result = JOptionPane.showConfirmDialog(
                        mySaveButton.getParent(),
                        "File already exists. Do you want to overwrite it?",
                        "File Already Exists",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    fileIsValid = true;
                } else { 
                    fileIsValid = false;
                }
            } else { //file name is different from any of the existing files
                fileIsValid = true;
            }
            return fileIsValid;
        }

        /**
         * This method will cause the GUI to respond to when the close button is clicked.
         */
        private void closeAction() {
            for (int i = 0; i < NUM_FILTERS; i++) {
                myButtons.get(i).setEnabled(false);
            }
            
            mySaveButton.setEnabled(false); //disable save
            myCloseButton.setEnabled(false); //disable close
            myCenter.setIcon(null); //remove image
            adjust();            
            
        }       
        
    }
}