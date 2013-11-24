//package ApartmentGui;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
///**
// * Main GUI class.
// * Contains the main frame and subsequent panels
// */
//public class ApartmentPanelGui extends JFrame implements ActionListener {
//    /* The GUI has two frames, the control frame (in variable gui) 
//     * and the animation frame, (in variable animationFrame within gui)
//     */
//        JFrame animationFrame = new JFrame("Apartment");
//        ApartmentAnimationPanel animationPanel = new ApartmentAnimationPanel();
//        
//    /* restPanel holds 2 panels
//     * 1) the staff listing, menu, and lists of current customers all constructed
//     *    in RestaurantPanel()
//     * 2) the infoPanel about the clicked Customer (created just below)
//     */    
////    private ApartmentAnimationPanel restPanel = new ApartmentPerson(this);
//    
//    /* infoPanel holds information about the clicked customer, if there is one*/
//    private JPanel infoPanel;
//    private JLabel infoLabel; //part of infoPanel
//    private JCheckBox stateCB;//part of infoLabel
//
//    private Object currentPerson;/* Holds the agent that the info is about.
//                                                                    Seems like a hack */
//
//    /**
//     * Constructor for RestaurantGui class.
//     * Sets up all the gui components.
//     */
//    public ApartmentPanelGui() {
//        int WINDOWX = 450;
//        int WINDOWY = 350;
//
//        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
//        animationFrame.setVisible(true);
//            animationFrame.add(animationPanel); 
//            
//            
//    }
//    
//    public void actionPerformed(ActionEvent e) {
//       
//    }
//    /**
//     * Message sent from a customer gui to enable that customer's
//     * "I'm hungry" checkbox.
//     *
//     * @param c reference to the customer
//     */
//    
//    public static void main(String[] args) {
//        ApartmentPanelGui gui = new ApartmentPanelGui();
//        
//    }
//}