package org.baljinder;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Application {

	public static void main(String[] a) {
		setLookAndFeel();
		ApplicationWindow application = new ApplicationWindow();
		application.validate();
		application.show();
	}

	public static class ApplicationWindow extends JFrame {

		private static final long serialVersionUID = 1L;

		Dimension fullScreen = Toolkit.getDefaultToolkit().getScreenSize();

		public ApplicationWindow() {
			setTitle("Formatter");
			setSize(fullScreen);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLayout(new GridLayout());
			buildApplicationComponents();
		}

		private void buildApplicationComponents() {

			final JTextPane inputTextArea = new JTextPane();
			final JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

			final JTextPane formattedTextArea = new JTextPane();
			final JScrollPane formattedScrollPane = new JScrollPane(
					formattedTextArea);

			final JSplitPane textAreaSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT, inputScrollPane,
					formattedScrollPane);
			textAreaSplitPane.setDividerLocation(fullScreen.width / 2);

			JButton button = new JButton();
			button.setText("Format(CTRL+F)");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					formattedTextArea.setText(formatText(inputTextArea
							.getText()));
				}
			});

			JSplitPane mainSplitPane = new JSplitPane(
					JSplitPane.VERTICAL_SPLIT, button, textAreaSplitPane);
			textAreaSplitPane.setDividerLocation(fullScreen.width / 2);
			add(mainSplitPane);
			addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if ((e.getKeyCode() == KeyEvent.VK_F)
							&& ((e.getModifiers() & Toolkit.getDefaultToolkit()
									.getMenuShortcutKeyMask()) != 0)) {
						formattedTextArea.setText(formatText(inputTextArea
								.getText()));
					}

				}

				public void keyReleased(KeyEvent arg0) {
				}

				public void keyTyped(KeyEvent arg0) {
				}
			});
		}

		private String formatText(String input) {
			try {
				String textToFormat = input;
				return XmlUtils.formatXml(textToFormat);
			} catch (Throwable th) {
				System.out.println(th);
				return "Invald input";
			}
		}
	}
	
	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
