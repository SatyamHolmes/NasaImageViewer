import java.util.ArrayList;

public class trash
{
  public static void main(String[] args)
  {
   	Store<Integer> abc=new Store<Integer>();
   	abc.addFront(2);
   	abc.addFront(3);
   	abc.addFront(4);
   	abc.addFront(5);
   	abc.addFront(6);
   	abc.removeFront();
   	abc.addLast(7);
   	System.out.println(abc.get(5));
  }
}
