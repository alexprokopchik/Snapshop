/*
 * Alex Prokopchik
 * May 4, 2013
 * SnapShopGUI.java
 */

package snapshop.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import snapshop.filters.EdgeDetectFilter;
import snapshop.filters.EdgeHighlightFilter;
import snapshop.filters.Filter;
import snapshop.filters.FlipHorizontalFilter;
import snapshop.filters.FlipVerticalFilter;
import snapshop.filters.GrayscaleFilter;
import snapshop.filters.SharpenFilter;
import snapshop.filters.SoftenFilter;
import snapshop.image.PixelImage;

/**
 * This program displays and manipulates images using a GUI.
 * 
 * @author alexpro
 * @version 1.0
 */

public class SnapShopGUI
{
  /**
   * A list of the filter buttons.
   */
  private static final List<JButton> FILTER_BUTTONS = new ArrayList<JButton>();
  
  /**
   * The number of buttons on bottom part of GUI.
   */
  private static final int BOTTOM_BUTTON_AMOUNT = 3;
  
  /**
   * The frame for this application's GUI.
   */
  private final JFrame my_frame;
  
  /**
   * The label for this application's GUI that stores an image.
   */
  private final JLabel my_label = new JLabel("", JLabel.CENTER);
  
  /**
   * The file chooser this GUI uses.
   */
  private final JFileChooser my_file_chooser = new JFileChooser("./images");
  
  /**
   * An array of the buttons on the bottom part of the GUI.
   */
  private final JButton[] my_bottom_buttons = new JButton[BOTTOM_BUTTON_AMOUNT]; 

  /**
   * North part of the GUI.
   */
  private final JPanel my_panel1 = new JPanel();
  
  /**
   * South part of the GUI.
   */
  private final JPanel my_panel2 = new JPanel();
  
  /**
   * The image that is selected and edited by the GUI.
   */
  private PixelImage my_image;
                  
  /**
   * Constructor to initialize fields.
   */
  public SnapShopGUI() 
  {
    my_frame = new JFrame();
  }

  /**
   * Sets up and displays the GUI for this application.
   */
  public void start() 
  {
    my_frame.setTitle("TCSS 305 SnapShop - Spring 2013");
    my_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    my_frame.setVisible(true);
    
    addButtonsTop();
    addButtonsBottom();
    openButton();
    createButtonClose();
    createButtonSave();
    
    my_frame.pack(); 
  }
  
  /**
   * This method creates the Filter buttons that go on top of the GUI.
   */
  private void addButtonsTop()
  {
    createButtonFilter(new EdgeDetectFilter());
    createButtonFilter(new EdgeHighlightFilter());
    createButtonFilter(new FlipHorizontalFilter());
    createButtonFilter(new FlipVerticalFilter());
    createButtonFilter(new GrayscaleFilter());
    createButtonFilter(new SharpenFilter());
    createButtonFilter(new SoftenFilter());
   
    for (int i = 0; i < FILTER_BUTTONS.size(); i++) 
    {
      my_panel1.add(FILTER_BUTTONS.get(i));
    }
    my_frame.add(my_panel1, BorderLayout.NORTH);
    my_frame.pack();
  }
  
  /**
   * This method creates the bottom buttons that go on the GUI.
   */
  private void addButtonsBottom()
  {
    final String[] names = {"Open...", "Save As...", "Close Image"}; 
    for (int i = 0; i < my_bottom_buttons.length; i++) 
    {  
      my_bottom_buttons[i] = new JButton(names[i]); 
      my_panel2.add(my_bottom_buttons[i]);      
    }
    my_bottom_buttons[1].setEnabled(false);
    my_bottom_buttons[2].setEnabled(false);
    my_frame.add(my_panel2, BorderLayout.SOUTH);
    my_frame.pack();
  }
  
/**
 * This method creates the filter buttons for the program.
 * 
 * @param the_object the filter object.
 */
  private void createButtonFilter(final Filter the_object)
  {
    final JButton button = new JButton(the_object.getDescription());
    FILTER_BUTTONS.add(button);
    button.addActionListener(new ActionListener() 
    {
      /**
       * Handles an ActionEvent for the filter buttons.
       * 
       * @param the_event The event.
       */
      public void actionPerformed(final ActionEvent the_event) 
      {
        the_object.filter(my_image);
        my_label.repaint();
      }
    });
    button.setEnabled(false);   
  }
  
  /**
   * This method gives an action listener for the close button in the program.
   */
  private void createButtonClose()
  {
    my_bottom_buttons[2].addActionListener(new ActionListener() 
      {
        /**
         * Handles an ActionEvent for the close button.
         * 
         * @param the_event The event.
         */
        public void actionPerformed(final ActionEvent the_event) 
        {
          my_label.setIcon(null);
          my_frame.pack();
          
          for (int i = 0; i < FILTER_BUTTONS.size(); i++) 
          {
            FILTER_BUTTONS.get(i).setEnabled(false);
            
          }
          my_bottom_buttons[1].setEnabled(false);
          my_bottom_buttons[2].setEnabled(false);        
        }
      });  
  }
  
  /**
   * This method gives an action listener for the close button in the program.
   */
  private void createButtonSave()
  {
    my_bottom_buttons[1].addActionListener(new ActionListener() 
      {
        /**
         * Handles an ActionEvent for the save as button.
         * 
         * @param the_event The event.
         */
        public void actionPerformed(final ActionEvent the_event) 
        {
          my_file_chooser.showSaveDialog(null);
          try
          {
            my_image.save(my_file_chooser.getSelectedFile());
          }
          catch (final IOException e)
          {
            e.getMessage();
          }    
        }
      });  
  }
  
  /**
   * The method that calls the action listener created for the open button.
   */
  private void openButton()
  {
    final ActionListener listener = new MyActionListenerOpen();

    my_bottom_buttons[0].addActionListener(listener);

  }
   
  /**
   * This method creates an action listener for the open button in the program.
   */
  class MyActionListenerOpen implements ActionListener 
  {
    /**
     * Handles an ActionEvent for the open button.
     * 
     * @param the_event The event.
     */
    public void actionPerformed(final ActionEvent the_event) 
    {
      
      my_file_chooser.showOpenDialog(null);

      try
      {
        my_image  = PixelImage.load(new File(my_file_chooser.getSelectedFile().toString()));
      }
      catch (final IOException e)
      {
        e.getMessage();
      }

      my_label.setIcon(new ImageIcon(my_image));
      my_frame.add(my_label, BorderLayout.CENTER);
      my_frame.pack();
      my_label.setEnabled(true);
      
      
      for (int i = 0; i < FILTER_BUTTONS.size(); i++) 
      {
        FILTER_BUTTONS.get(i).setEnabled(true);
      }
      for (int i = 0; i < my_bottom_buttons.length; i++) 
      {
        my_bottom_buttons[i].setEnabled(true);
      } 
    } 
  }
}
