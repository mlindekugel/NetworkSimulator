import java.awt.*;
import java.util.*;
import javax.swing.*;

public class MessagePopup extends JPanel
{
    private static int loremIdx = 0;
    private static final String[] LOREM = new String[]{
"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
"Scelerisque in dictum non consectetur a.",
"Semper feugiat nibh sed pulvinar. Convallis a cras semper auctor.",
"Potenti nullam ac tortor vitae.",
"Enim nulla aliquet porttitor lacus luctus accumsan tortor posuere ac.",
"Sagittis purus sit amet volutpat consequat mauris nunc.",
"Morbi tincidunt ornare massa eget egestas.",
"Tempor orci dapibus ultrices in iaculis nunc sed augue lacus.",
"Magna sit amet purus gravida.",
"Id ornare arcu odio ut sem nulla.",
"Sit amet volutpat consequat mauris nunc congue nisi.",
"Phasellus egestas tellus rutrum tellus pellentesque eu tincidunt tortor aliquam.",
"Aenean sed adipiscing diam donec adipiscing tristique risus nec feugiat.",
"Sed libero enim sed faucibus turpis in eu mi."
    };
    private Machine source, destination;
    private Map<String,Machine> lookupName;
    private SimulationArea parent;

    public MessagePopup( 
        SimulationArea parent, Machine source, Machine[] machines ) 
    {
        this.parent = parent;
        this.setLayout( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints();
        String[] choices = new String[ machines.length - 1 ];
        int idx = 0;
        this.lookupName = new HashMap<String, Machine>();
        for ( Machine each : machines )
        {
            if ( each.getName().equals( source.getName() ) ) continue;
            choices[ idx++ ] = each.getName();
            this.lookupName.put( each.getName(), each );
        }

        JTextField sourceName = new JTextField( source.getName() );
        JComboBox<String> destinationNames = new JComboBox<>( choices );
        JTextArea text = new JTextArea(3, 5);
        text.setText( LOREM[ ( loremIdx++ % LOREM.length ) ] );
        JScrollPane pane = new JScrollPane(text);
        text.setLineWrap( true );
        JButton save = new JButton( "Save" );
        JButton cancel = new JButton( "Cancel" );

        cancel.addActionListener( e -> this.parent.hidePopup() );
        save.addActionListener( e -> {
            this.destination = lookupName.get(destinationNames.getSelectedItem());
            Message m = new Message( text.getText() );
            m.setSource( this.source );
            m.setDestination( this.destination );
            this.source.addMessage( m );
            this.parent.hidePopup();
        });
        
        this.source = source;
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = gbc.BOTH;
        this.add( new JLabel( "Source: " ), gbc );
        gbc.gridx = 1; gbc.gridy = 0;
        this.add( sourceName, gbc );
        gbc.gridx = 2; gbc.gridy = 0;
        this.add( new JLabel( "Desination: " ), gbc );
        gbc.gridx = 3; gbc.gridy = 0;
        this.add( destinationNames, gbc );
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        this.add( pane, gbc );
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        this.add( cancel, gbc );
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        this.add( save, gbc );

        sourceName.setEditable( false );
        destinationNames.setVisible( true );

    }
}
