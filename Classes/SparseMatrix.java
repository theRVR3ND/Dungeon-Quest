import java.util.ArrayList;

//Cell class can be found below

public class SparseMatrix<T> implements Matrixable<T>{
   
   private final byte numR, numC;						//Number of rows and number of columns
   private ArrayList<Cell<T>> list;						//Arraylist of all grid cells
   
   //-----Initialize-----//
   
   public SparseMatrix(byte rIn, byte cIn){
      list = new ArrayList<Cell<T>>();
      numR = rIn;
      numC = cIn;
   }
   
   //-----Mutate-----//
   
   //pre: addThis != null
   //post: Adds new cell or replaces previous cell at position (r, c) with 
   //      contents addThis and returns previous value at (r, c)
   public T add(T addThis, byte r, byte c){
      if(r < 0 || r >= numR || c < 0 || c >= numC)
         return null;
         
      for(byte i = 0; i < list.size(); i++)
         if(list.get(i).getKey() == r * numC + c){
            T prevThere = list.get(i).getContents();
            list.get(i).setContents(addThis);
            return prevThere;
         }
      //If Cell at (r, c) does not already exist
      list.add(new Cell(addThis, r * numC + c));
      return null;
   }
   
   //pre: 
   //post: Removes/deletes cell at (r, c) and returns contents, if present
   public T remove(byte r, byte c){
      if(r < 0 || r >= numR || c < 0 || c >= numC)
         return null;
         
      for(byte i = 0; i < list.size(); i++){
         if(list.get(i).getKey() == r * numC + c)
            return list.remove(i).getContents();
      }
      return null;
   }
   
   //pre: 
   //post: Deletes entirety of matrix
   public void clear(){
      list.clear();
   }
   
   //-----Access-----//
   
   //pre: 
   //post: Returns contents of matrix at (r, c)
   public T get(byte r, byte c){
      if(r < 0 || r >= numR || c < 0 || c >= numC)
         return null;
      
      for(byte i = 0; i < list.size(); i++)
         if(list.get(i).getKey() == r * numC + c)
            return list.get(i).getContents();
      //If no cell at (r, c) exists
      return null;
   }
   
   //pre: 0 <= i < list.size()
   //post: Returns contents of cell at index i in list
   public T get(byte i){
      return list.get(i).getContents();
   }
   
   //pre: 
   //post: Returns number of elements in matrix
   public byte size(){
      return (byte)list.size();
   }
   
   //pre: 
   //post: Returns true if matrix is empty, false otherwise
   public boolean empty(){
      return list.size() == 0;
   }
   
   //pre: 
   //post: Returns row dimension of matirx
   public byte numRow(){
      return numR;
   }
   
   //pre: 
   //post: Returns column dimension of matrix
   public byte numCol(){
      return numC;
   }
   
   //pre: 
   //post: Returns true if matrix contains searchFor
   public boolean contains(T searchFor){
      for(Cell c : list)
         if(c.getContents().equals(searchFor))
            return true;
      return false;
   }
   
   //pre: 
   //post: Returns number of occurences of searchFor in matrix
   public byte numberOf(T searchFor){
      byte numOf = 0;
      for(Cell c : list)
         if(c.getContents().equals(searchFor))
            numOf++;
      return numOf;
   }
   
   //pre: 
   //post: Returns int[] with index 0 being the row location of find
   //      and index 1 being the column value find
   public byte[] locationOf(T find){
      byte[] coord = new byte[2];
      for(byte i = 0; i < list.size(); i++)
         if(list.get(i).getContents().equals(find)){
            coord[0] = (byte)(list.get(i).getKey() / numC);//Row coordinate
            coord[1] = (byte)(list.get(i).getKey() % numC);//Column coordinate
            return coord;
         }
      return null;
   }
   
   //pre: 
   //post: Returns largest value in this matrix
   public T getLargest(){
      if(list.size() == 0)
         return null;
      Comparable largest = (Comparable)(list.get(0).getContents());
      for(Cell c : list)
         if(largest.compareTo((Comparable)(c.getContents())) < 0)
            largest = (Comparable)c.getContents();
      return (T)largest;
   }
   
   //pre: 
   //post: Returns conventional array form of this matrix
   public Object[][] toArray(){
      Object[][] arr = new Object[numR][numC];
      for(byte r = 0; r < numR; r++)
         for(byte c = 0; c < numC; c++)
            arr[r][c] = get(r, c);
      return arr;
   }
   
   //-----toString-----//

   //pre: 
   //post: Returns String representation of this matrix, with '-' representing an
   //      empty space.
   public String toString(){
      String ret = "";
      for(byte r = 0; r < numR; r++){
         for(byte c = 0; c < numC; c++){
            if(get(r, c) != null)
               ret += get(r, c).toString() + " ";
            else
               ret += "- ";
         }
         ret += "\n";
      }
      return ret;
   }
   
   //-----Cell Class (for item storage)-----//
   
   public class Cell<T>{
   
      private final int key;                    //Key of cell in grid
      private T holdThis;                       //Object which this cell is holding
      
      //-----Initialize-----//
      
      public Cell(T holdThisIn, int keyIn){
         key = keyIn;
         holdThis = holdThisIn;
      }
      
      //-----Mutate-----//
      
      //pre: 
      //post: Returns key of this cell (relative to matrix)
      public int getKey(){
         return key;
      }
      
      //pre: holdThisIn != null
      //post: Sets contents of this cell to holdThisIn
      public void setContents(T holdThisIn){
         holdThis = holdThisIn;
      }
      
      //-----Access-----//
      
      //pre: 
      //post: Returns current contents of this cell
      public T getContents(){
         return holdThis;
      }
   }
}