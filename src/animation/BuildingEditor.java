package animation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Buildings.Building;
import agents.Person;

public class BuildingEditor extends JPanel implements ActionListener{
	
	private JComboBox<Building> buildings = new JComboBox<Building>();
	
	private List<JButton> dataButtons = new ArrayList<JButton>();
	private List<JTextField> dataFields = new ArrayList<JTextField>();
	
	JPanel listPanels;
	
	Building selectedBuilding = null;
	private JButton selectBuilding = new JButton();
	private GenericListPanel bankEditor = new GenericListPanel();
	
	ControlPanel control;
	
	public BuildingEditor(ControlPanel control)
	{
		this.control = control;
		this.setLayout(new BorderLayout());
		selectBuilding.addActionListener(this);
		
		listPanels = new JPanel();
		listPanels.setLayout(new GridLayout(1, 0));
		
		this.add(selectBuilding, BorderLayout.CENTER);
		this.add(listPanels, BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}
}
