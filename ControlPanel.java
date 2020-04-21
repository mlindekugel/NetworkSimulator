import javax.swing.*;

public class ControlPanel extends JPanel
{
    private SimulationArea simulation;
    public ControlPanel( SimulationArea simulation )
    {
        this.simulation = simulation;
        JButton begin = new JButton( "Create Packets" );
        begin.addActionListener( e -> simulation.send() );
        JButton step = new JButton( "Route Packets" );
        step.addActionListener( e -> simulation.routePackets() );
        JButton clear = new JButton( "Clear Packets" );
        clear.addActionListener( e -> simulation.clear() );

        JTextArea ttl = new JTextArea( String.valueOf( Packet.getTTL() ) );
        JButton ttlBtn = new JButton( "TTL" );
        ttlBtn.addActionListener( e -> {
            try { Packet.setTTL( Integer.parseInt(ttl.getText()) ); }
            catch( Exception ee ) 
            { ttl.setText(String.valueOf(Packet.getTTL())); };
        });

        JTextArea buff = new JTextArea( 
            String.valueOf( Machine.getBufferSize() ) 
        );
        JButton buffBtn = new JButton( "Buffer" );
        buffBtn.addActionListener( e -> {
            try { Machine.setBufferSize( Integer.parseInt(buff.getText()) ); }
            catch( Exception ee ) 
            { buff.setText(String.valueOf(Machine.getBufferSize())); };
        });

        this.add( begin );
        this.add( step );
        this.add( clear );
        this.add( new JLabel( "TTL: " ) );
        this.add( ttl );
        this.add( ttlBtn );
        this.add( new JLabel( "Buffer: " ) );
        this.add( buff );
        this.add( buffBtn );
    }
}
