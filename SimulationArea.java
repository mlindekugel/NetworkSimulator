import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class SimulationArea extends JPanel 
    implements MouseListener, MouseMotionListener
{
    private static PopupFactory pf = new PopupFactory();

    private int tempX, tempY;
    private boolean simulationRunning;
    private java.util.List<MachineWrapper> machines ; 
    private java.util.Map<Machine,MachineWrapper> machineLookup ; 
    private MachineWrapper target1, target2;
    private Popup messagePopup ;

    public SimulationArea()
    {
        this.machines = new ArrayList<MachineWrapper>();
        this.machineLookup = new HashMap<Machine,MachineWrapper>();
        this.addMouseListener( this );
        this.addMouseMotionListener( this );
        this.simulationRunning = false;
    }

    public void routePackets()
    {
        Packet.routeAll();
        Packet.deleteDropped();
        this.repaint();
    }

    public void send()
    {
        this.simulationRunning = true;
        for ( Machine each : this.machineLookup.keySet() )
        {
            if ( each.hasMessage() )
            {
                each.getMessage().send();
                each.addMessage( null );
            }
        }
        this.repaint();
    }

    public void clear()
    {
        for ( Machine each : this.machineLookup.keySet() )
        {
            if ( each.hasMessage() ) each.addMessage( null );
            this.machineLookup.get( each ).showDetails( false );
            each.clear();
        }
        Packet.clear();
        this.repaint();
        this.simulationRunning = false;
    }

    public MachineWrapper getMachineAt( int x, int y )
    {
        MachineWrapper machine = null;
        for ( MachineWrapper each : this.machines )
        {
            if ( each.getX() < x && x < each.getX() + each.getWidth() &&
                 each.getY() < y && y < each.getY() + each.getHeight())
            {
                machine = each;
                break;
            }
        }

        return machine;
    }

    @Override
    public void paintComponent( Graphics g )
    {
        g.setColor( Color.WHITE );
        g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
        for ( MachineWrapper each : this.machines )
            each.paint( g, true );
        for ( MachineWrapper each : this.machines )
            drawNetwork( each, g );
        for ( MachineWrapper each : this.machines )
            each.paint( g, false );
        if ( this.target1 != null && this.tempX > 0 && this.tempY > 0 )
        {
            int startX = this.target1.getX() + this.target1.getWidth() / 2;
            int startY = this.target1.getY() + this.target1.getHeight() / 2;
            g.setColor( Color.BLACK );
            g.drawLine( startX, startY, this.tempX, this.tempY );
        }
    }

    private void drawNetwork( MachineWrapper node, Graphics g )
    {
        ConnectionWrapper.drawAll( g );
    }

    public void hidePopup()
    {
        if ( this.messagePopup != null )
            this.messagePopup.hide();
        this.messagePopup = null;
        this.repaint();
    }

    private void moveTarget( int x, int y )
    {
        if ( this.target1 == null ) return;
        this.target1.setX( x - this.target1.getWidth() / 2 );
        this.target1.setY( y - this.target1.getHeight() / 2 );
    }

    private void updateNetworkConnectionLine( int x, int y )
    {
        this.tempX = x; 
        this.tempY = y;
    }

    private void finalizeNetworkConnection( int x, int y )
    {
        this.target2 = getMachineAt( x, y );
        if ( this.target1 != null && this.target2 != null && 
             !this.target1.equals( this.target2 ) )
            ConnectionWrapper.get( this.target1, this.target2 );
        this.tempX = 0; this.tempY = 0;
    }

    private void addMachine( int x, int y )
    {
        MachineWrapper w = new MachineWrapper( null, x, y );
        this.machines.add( w );
        this.machineLookup.put( w.getMachine(), w );
    }

    private void removeMachine( MachineWrapper w )
    {
        ConnectionWrapper.breakConnectionsWith( w.getMachine() );
        this.machines.remove( w );
        this.repaint();
    }

    private void getMessage( int x, int y )
    {
        MachineWrapper source = getMachineAt( x, y );
        if ( source == null ) return;
        Machine[] machines = new Machine[ this.machines.size() ] ;
        for ( int i = 0; i < machines.length; i++ )
            machines[i] = this.machines.get(i).getMachine();
        JPanel p2 = new MessagePopup( this, source.getMachine(), machines );
        this.hidePopup();
        this.messagePopup = pf.getPopup( this, p2, x, y );
        this.messagePopup.show( );
    }

    private void showMachineState( int x, int y )
    {
        MachineWrapper source = getMachineAt( x, y );
        if ( source == null ) return;
        source.toggleDetails();
        this.repaint();
    }

    @Override
    public void mouseDragged( MouseEvent e ) 
    {
        if ( SwingUtilities.isLeftMouseButton(e) )
            updateNetworkConnectionLine( e.getX(), e.getY() );
        else if ( SwingUtilities.isRightMouseButton( e ) )
            moveTarget( e.getX(), e.getY() );
        this.repaint();
    }

    @Override
    public void mouseMoved( MouseEvent e ) 
    {
        ConnectionWrapper.highlight( e.getX(), e.getY() ) ;
        this.repaint();
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
        if ( this.messagePopup != null ) 
            this.hidePopup();
        else if ( SwingUtilities.isLeftMouseButton( e ) )
            if ( ConnectionWrapper.isOnConnection() )
                ConnectionWrapper.breakHighlightedConnection();
            else if ( simulationRunning )
                showMachineState( e.getX(), e.getY() );
            else 
            {
                MachineWrapper machine = getMachineAt( e.getX(), e.getY() );
                if ( machine == null )
                    addMachine( e.getX(), e.getY() );
                else removeMachine( machine );
            }
        else if ( SwingUtilities.isRightMouseButton( e ) )
            getMessage( e.getX(), e.getY() );
        this.repaint();
    }

    @Override
    public void mouseEntered( MouseEvent e ) {}

    @Override
    public void mousePressed( MouseEvent e ) 
    {
        this.target1 = getMachineAt( e.getX(), e.getY() );
    }

    @Override
    public void mouseReleased( MouseEvent e ) 
    {
        if ( SwingUtilities.isLeftMouseButton( e ) )
            finalizeNetworkConnection( e.getX(), e.getY() );
        this.repaint();
    }

    @Override
    public void mouseExited( MouseEvent e ) {}
}

