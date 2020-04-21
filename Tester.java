public class Tester
{
    public static void main( String[] args )
    {
        Message msg1 = new Message("This is a test to see how things work.");

        Machine m1 = new Machine( "One" );
        Machine m2 = new Machine( "Two" );
        Machine m3 = new Machine( "Three" );

        m1.connectTo( m2 );
        m2.connectTo( m3 );

        msg1.setSource( m1 );
        msg1.setDestination( m3 );

        msg1.send();

        for ( int i = 0; i < 20; i++ )
        {
            System.out.println( m1 );
            System.out.println( m2 );
            System.out.println( m3 );

            Packet.routeAll();
        }
    }
}
