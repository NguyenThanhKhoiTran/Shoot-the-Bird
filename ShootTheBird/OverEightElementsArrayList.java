/************************************************************************
 * Create an exception class (OverEightElementsArrayList) to finish the 
 * game as there are no birds exist now
 * 
 * @author Nguyen Thanh Khoi Tran
 * @author Sagar Kaithavayalil Jaison
 * @date Feb 29, 2024
 * @version proj_v01 
 ************************************************************************/
public class OverEightElementsArrayList extends Exception
{
    public OverEightElementsArrayList (String message)
    {
        super (message);
    }
    
    public OverEightElementsArrayList ()
    {
        super ("Ohhhh.... There are no birds now, PLEASE click \" LEAVE \" button below to receive reward");
    }
}