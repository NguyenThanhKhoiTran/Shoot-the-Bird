/************************************************************************
 * Create an exception class (NoNameInputException) to force user to type
 * a name into a provided TextField
 * 
 * @author Nguyen Thanh Khoi Tran
 * @author Sagar Kaithavayalil Jaison
 * @date Feb 29, 2024
 * @version proj_v01 
 ************************************************************************/
public class NoNameInputException extends Exception
{
    public NoNameInputException (String message)
    {
        super (message);
    }
}