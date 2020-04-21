import java.awt.*;
import javax.swing.*;

public class NetworkSimulator
{
    public static void main( String[] args )
    {
        JFrame window = new JFrame();
        window.setSize( 800, 600);
        SimulationArea area = new SimulationArea();
        window.add( new ControlPanel( area ), BorderLayout.NORTH );
        window.add( area );
        window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        window.setVisible( true );
    }
}
