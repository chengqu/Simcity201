package animation;

import javax.swing.*;

import agents.Person;



import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimcityGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Simcity Animation");
	JFrame controlFrame = new JFrame("Control Panel");
	
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
	
	public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();

    private Simcity simCity = new Simcity(this);
    public SimcityPanel animationPanel = new SimcityPanel(simCity);
    public ControlPanel controlPanel=new ControlPanel(simCity, this);
    //public PersonListPanel personPanel=new PersonListPanel(simCity);
    
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JTextField textField;
    private JCheckBox onBreak;
    
    private JButton pauseButton;

    private Object currentPerson;/* Holds the agent that the info is about.
                           Seems like a hack */

    
    private  int controlFrameX=550;
    private  int controlFrameY=450;

    
    private static int guiX=700;
    private static int guiY=800;
    static Dimension GUI_DIM = new Dimension(guiX, guiY);

    //private ListPanel listpanel = new ListPanel(restPanel, "");
    private Image my;


    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public SimcityGui() {

        int WINDOWX = 1200;
        int WINDOWY = 850;

       
        setLayout(new BorderLayout(5,10));
        setBounds(20, 50, controlFrameX, controlFrameY);

       
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(50, 0 , WINDOWX, WINDOWY);
        Dimension PANEL_DIM = new Dimension(WINDOWX, WINDOWY);
        animationFrame.setPreferredSize(PANEL_DIM);
        animationFrame.setMaximumSize(PANEL_DIM);
        animationFrame.setMinimumSize(PANEL_DIM);
        animationFrame.setVisible(true);
        animationFrame.setResizable(false);
        animationFrame.add(animationPanel);
        
        controlPanel.setBounds(50, 0 , controlFrameX, controlFrameY);
        Dimension CONTROL_DIM = new Dimension(controlFrameX, controlFrameY);
        controlPanel.setPreferredSize(CONTROL_DIM);
        controlPanel.setMaximumSize(CONTROL_DIM);
        controlPanel.setMinimumSize(CONTROL_DIM);
        controlPanel.setVisible(true);
      
        add(controlPanel,BorderLayout.CENTER);
        
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        onBreak = new JCheckBox();
        onBreak.setVisible(false);
        onBreak.addActionListener(this);
        
        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        add(infoPanel,BorderLayout.SOUTH);
    }
    public void updateInfoPanel(Person p) {
   } 
    
    public void actionPerformed(ActionEvent e) {
        
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        SimcityGui gui = new SimcityGui();
        gui.setSize(GUI_DIM);
        gui.setPreferredSize(GUI_DIM);
        gui.setMaximumSize(GUI_DIM);
        gui.setMinimumSize(GUI_DIM);
        gui.setTitle("Control Panel");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
