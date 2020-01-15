package main_parser;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;


public class ParserWindow {

	private JFrame frmJsonParser;
	private JTextField textField;
	private static JTextArea textArea;
	private static String Message;
	private File location;

	/**
	 * Launch the application....
	 */
	public static void main(String[] args) {
		try {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ParserWindow window = new ParserWindow();
					window.frmJsonParser.setVisible(true);
				} catch (Exception e1){
					Message = e1.toString();
					textArea.setDisabledTextColor(Color.BLACK);
					}
			}
		});
		}
		catch (Exception e1){
			Message = e1.getLocalizedMessage().toString();
			textArea.setText(Message);
			textArea.setDisabledTextColor(Color.BLACK);
			}
		
	}

	/**
	 * Create the application.
	 */
	public ParserWindow() throws Exception {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws Exception{
		
		
		//  initialize the main frame of Window 
		
		frmJsonParser = new JFrame();
		frmJsonParser.setResizable(false);
		frmJsonParser.setTitle("JSON Parser");
		frmJsonParser.setBounds(100, 100, 451, 219);
		frmJsonParser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJsonParser.getContentPane().setLayout(null);
		
		
		// Button for choose file from Directory -----------------
		
		JButton btnNewButton = new JButton("OPEN");
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File("C:\\"));
				chooser.setDialogTitle("Choose File");
				chooser.setFileFilter(new FileTypeFilter(".json","JSON FILE"));
				chooser.setAcceptAllFileFilterUsed(false);
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					if((getExtension(f.toString()).equalsIgnoreCase("json"))){ 
						textField.setText(f.toString());
						Border border = BorderFactory.createLineBorder(Color.green,1);
						textField.setBorder(border);
						location = f;
					}
					else {
						Border border = BorderFactory.createLineBorder(Color.RED,2);
						textField.setBorder(border);
						textArea.setText("Acceptable file with extension (*.json)");
						textArea.setDisabledTextColor(Color.RED);
					}
				}
			}

			
			
			private String getExtension(String string){
				String extension = "";
				int i = string.lastIndexOf('.');
				int p = Math.max(string.lastIndexOf('/'), string.lastIndexOf('\\'));
				if (i > p) extension = string.substring(i+1);
				return extension;
			}
		});
		btnNewButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		btnNewButton.setBounds(10, 33, 88, 28);
		frmJsonParser.getContentPane().add(btnNewButton);
		
		
		
		//Button for Upload and parse data ----------------
		
		JButton btnNewButton_1 = new JButton("UPLOAD");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				JSONParse parse = new JSONParse(location);
				Message = parse.parse();
				textArea.setText(Message);
				textArea.setDisabledTextColor(Color.BLACK);
			}
		});
		btnNewButton_1.setFont(new Font("Verdana", Font.PLAIN, 11));
		btnNewButton_1.setBounds(336, 33, 88, 28);
		frmJsonParser.getContentPane().add(btnNewButton_1);
		
		
		//textField for show input location
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(97, 34, 213, 28);
		frmJsonParser.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		//Text area for show output result
		
		textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		textArea.setTabSize(100);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Verdana", Font.PLAIN, 11));
		textArea.setRows(10);
		textArea.setBounds(10, 83, 414, 84);
		frmJsonParser.getContentPane().add(textArea);
		
	}
}