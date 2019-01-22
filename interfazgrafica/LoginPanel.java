package interfazgrafica;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginPanel extends JFrame{
	private InterfazGrafica interfaz;
	private JLabel fotoETSE;
	private ArrayList<JTextField> fields = new ArrayList<JTextField>();
	private JLabel n1_label;
	private JLabel n2_label;
	private JLabel n3_label;
	private JLabel n4_label;
	private JLabel n5_label;
	private JLabel n6_label;
	private JLabel introduzca;
	private ButtonGroup grupo;
	private ArrayList<JRadioButton> botones = new ArrayList<JRadioButton>();
	private JButton comenzar;
	
	private static ArrayList<String> nombres = new ArrayList<String>();
	

	public LoginPanel(InterfazGrafica interfaz) throws IOException {
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(300, 500));
		this.interfaz = interfaz;
		this.setUp();
	}
	
	private void setUp() throws IOException {
		Image img = ImageIO.read(getClass().getResource("/resources/etse.jpg"));
		img = img.getScaledInstance(40, 50, Image.SCALE_SMOOTH);
		fotoETSE = new JLabel();
		fotoETSE.setIcon(new ImageIcon(img));
		fotoETSE.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(fotoETSE);
		
		introduzca = new JLabel("Introduzca el número de jugadores");
		this.add(introduzca);
		for(Integer j=2; j<7; j++) {
			botones.add(new JRadioButton(j.toString(),false));
		}
		botones.get(0).setSelected(true);
		for(JRadioButton b: botones) {
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					b.setSelected(true);
				}
			});
		}
		for(JRadioButton b: botones) {
			this.add(b);
		}
		grupo = new ButtonGroup();
		for(JRadioButton b: botones) {
			grupo.add(b);
		}

		n1_label = new JLabel("Nombre jugador 1:");
		n2_label = new JLabel("Nombre jugador 2:");
		n3_label = new JLabel("Nombre jugador 3:");
		n4_label = new JLabel("Nombre jugador 4:");
		n5_label = new JLabel("Nombre jugador 5:");
		n6_label = new JLabel("Nombre jugador 6:");
		for(int i=0; i<6; i++) {
			fields.add(new JTextField());
		}
		this.add(n1_label);
		this.add(fields.get(0));
		this.add(n2_label);
		this.add(fields.get(1));
		this.add(n3_label);
		this.add(fields.get(2));
		this.add(n4_label);
		this.add(fields.get(3));
		this.add(n5_label);
		this.add(fields.get(4));
		this.add(n6_label);
		this.add(fields.get(5));
		
		comenzar = new JButton("Comenzar el juego");
		comenzar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer nJugadores = 2;
				for(JRadioButton b: botones) {
					if(b.isSelected()) {
						nJugadores = Integer.parseInt(b.getText());
					}
				}
				for(int i=0; i<nJugadores; i++) {
					nombres.add(fields.get(i).getText());
				}
				((JButton) e.getSource()).getParent().setVisible(false);
				try {
					interfaz.cargarJugadoresIniciales(nombres,nombres);
					interfaz.cargarFichasIniciales();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				interfaz.setVisible(true);
			}
		});
		this.add(comenzar);
	}
	
	public static ArrayList<String> getNombres(){
		if(nombres.isEmpty()) {
			return null;
		}
		return nombres;
	}
	
	public static Integer getNJugadores() {
		return nombres.size();
	}

}