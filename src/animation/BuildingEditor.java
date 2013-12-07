package animation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import simcity201.gui.Bank;
import simcity201.gui.GlobalMap;
import Buildings.Building;
import agents.Person;

public class BuildingEditor extends JPanel implements ActionListener{
	
	private JComboBox<Building> buildings = new JComboBox<Building>();
	private List<JButton> dataButtons = new ArrayList<JButton>();
	private List<JTextField> dataFields = new ArrayList<JTextField>();
	
	JPanel listPanels;
	
	Building selectedBuilding = null;
	private JButton selectBuilding = new JButton();
	private GenericListPanel editor = new GenericListPanel();
	
	ControlPanel control;
	
	public BuildingEditor(ControlPanel control)
	{
		this.control = control;
		this.setLayout(new BorderLayout());
		selectBuilding.addActionListener(this);
		
		listPanels = new JPanel();
		listPanels.setLayout(new GridLayout(1, 0));
		listPanels.add(editor);
		
		JPanel temp = new JPanel();
		temp.add(selectBuilding);
		temp.add(buildings);
		
		Dimension addDim = new Dimension(200, 30);
        buildings.setPreferredSize(addDim);
        buildings.setMinimumSize(addDim);
        buildings.setMaximumSize(addDim);
		
		this.add(temp, BorderLayout.NORTH);
		this.add(listPanels, BorderLayout.SOUTH);
		
		for(Building b: GlobalMap.getGlobalMap().getBuildings())
		{
			buildings.addItem(b);
		}
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(buildings.getSelectedItem().getClass() == Bank.class)
		{
			editor.clearPane();
			dataButtons.clear();
			dataFields.clear();
			
			List<JComponent> components;
			JButton button;
			JTextField field;
			
			Bank b = (Bank)buildings.getSelectedItem();
			
			components = new ArrayList<JComponent>();
			button = new JButton();
			field = new JTextField();
			
			button.setText(Float.toString(b.db.budget));
			field.setName("Money_Bank");
			field.addActionListener(this);
		}
	}
}
