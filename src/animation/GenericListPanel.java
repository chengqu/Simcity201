package animation;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import agents.Person;

public class GenericListPanel extends JPanel implements ActionListener{
	
  public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JTextField textField = new JTextField(20);
    
    public Person p;
    
    public GenericListPanel(Person p)
    {
    	this.p = p;
    	this.setPreferredSize(new Dimension(200, 200));
		this.setMinimumSize(new Dimension(200, 200));
		this.setMaximumSize(new Dimension(200, 200));
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        
        add(new JLabel("<html><pre> <u>" + p.getName() + "</u><br></pre></html>"));
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }
	
	public void actionPerformed(ActionEvent arg0) {
	}
}
