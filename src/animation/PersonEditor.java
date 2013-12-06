package animation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import agents.Person;
import simcity201.gui.GlobalMap;

public class PersonEditor extends JPanel implements ActionListener{

	private JComboBox<Person> persons = new JComboBox<Person>();
	private List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
	private List<JButton> buttons = new ArrayList<JButton>();
	JPanel listPanels;
	private JButton selectPerson;
	
	private GenericListPanel listPanel;
	
	Person selectedPerson = null;
	
	ControlPanel control;
	
	int previousCount;
	
	public PersonEditor(ControlPanel control)
	{
		selectPerson = new JButton();
		selectPerson.setText("Select");
		this.control = control;
		listPanels = new JPanel();
		listPanels.setLayout(new GridLayout(1, 0));
		
		JPanel temp = new JPanel();
		
		setLayout(new BorderLayout());
        
        Dimension addDim = new Dimension(200, 30);
        persons.setPreferredSize(addDim);
        persons.setMinimumSize(addDim);
        persons.setMaximumSize(addDim);
        
        temp.add(selectPerson);
        temp.add(persons);
		
        this.add(temp, BorderLayout.NORTH);
        
        listPanel = new GenericListPanel();
        
        listPanels.add(listPanel);
        
        this.add(listPanels, BorderLayout.CENTER);
        
		for(Person p: GlobalMap.getGlobalMap().getListOfPeople())
		{
			persons.addItem(p);
		}
		
		persons.setVisible(true);
		selectPerson.setVisible(true);
		selectPerson.addActionListener(this);
	}
	
	public void addPerson(Person p)
	{
		persons.addItem(p);
		this.repaint();
	}
	
	public void updatePerson(Person p)
	{
		if(persons.getSelectedItem() != null && p.equals((Person)persons.getSelectedItem()))
		{
			for(JCheckBox box : checkBoxes)
			{
				if(box.getName().equalsIgnoreCase("Deposit Groceries"))
				{
					box.setSelected(p.depositGroceries);
				}
				if(box.getName().equalsIgnoreCase("Create Account"))
				{
					box.setSelected(p.createAccount);
				}
				if(box.getName().equalsIgnoreCase("Get Money"))
				{
					box.setSelected(p.getMoneyFromBank);
				}
				if(box.getName().equalsIgnoreCase("Deposit Money"))
				{
					box.setSelected(p.depositMoney);
				}
				if(box.getName().equalsIgnoreCase("Buy Groceries"))
				{
					box.setSelected(p.buyGroceries);
				}
				if(box.getName().equalsIgnoreCase("Eat food"))
				{
					box.setSelected(p.eatFood);
				}
				if(box.getName().equalsIgnoreCase("Pay bills"))
				{
					box.setSelected(p.payBills);
				}
				if(box.getName().equalsIgnoreCase("Go to sleep"))
				{
					box.setSelected(p.goToSleep);
				}
			}
		}
	}
	
	public boolean show = true;
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == selectPerson)
		{
			if(persons.getSelectedItem() != null)
			{
				checkBoxes.clear();
				buttons.clear();
				listPanel.clearPane();
				
				List<JComponent> components;
				JCheckBox box;
				JButton button;
				Person p = (Person)persons.getSelectedItem();
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setName("Deposit Groceries");
				box.setSelected(p.depositGroceries);
				button = new JButton();
				button.setText("Deposit Groceries");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				listPanel.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setName("Create Account");
				box.setSelected(p.createAccount);
				button = new JButton();
				button.setText("Create Account");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				
				listPanel.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.getMoneyFromBank);
				button = new JButton();
				box.setName("Get Money");
				button.setText("Get Money");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				listPanel.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.depositMoney);
				button = new JButton();
				box.setName("Deposit Money");
				button.setText("Deposit Money");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				listPanel.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.buyGroceries);
				button = new JButton();
				box.setName("Buy Groceries");
				button.setText("Buy Groceries");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				listPanel.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.eatFood);
				button = new JButton();
				box.setName("Eat food");
				button.setText("Eat food");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				listPanel.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.payBills);
				button = new JButton();
				box.setName("Pay bills");
				button.setText("Pay bills");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				listPanel.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.goToSleep);
				button = new JButton();
				box.setName("Go to sleep");
				button.setText("Go to sleep");
				button.setBackground(Color.white);
				checkBoxes.add(box);
				buttons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				listPanel.addParams(components);
			}
		}
		else if(arg0.getSource().getClass() == JCheckBox.class)
		{
			JCheckBox temp = (JCheckBox)arg0.getSource();
			Person p = (Person)persons.getSelectedItem();
			synchronized(p.commandLock)
			{
				if(temp.getName().equalsIgnoreCase("Deposit Groceries"))
				{
					p.depositGroceries = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Create Account"))
				{
					p.createAccount = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Get Money"))
				{
					p.getMoneyFromBank = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Deposit Money"))
				{
					p.depositMoney = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Buy Groceries"))
				{
					p.buyGroceries = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Eat food"))
				{
					p.eatFood = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Pay bills"))
				{
					p.payBills = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Go to sleep"))
				{
					p.goToSleep = temp.isSelected();
				}
			}
		}
	}
}
