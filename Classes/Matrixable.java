public interface Matrixable<T>{
   public T add(T addThis, int r, int c);					//Returns previous value at (r, c) before replacing it with addThis
   public T get(int r, int c);								//Returns value at (r, c)
   public boolean empty();										//Returns whether or not the matrix is empty
   public int size();											//Number or values stored
   public int numRow();											//Number or rows
   public int numCol();											//Number of columns
   public boolean contains(T searchFor);					//Returns whether or not the matrix contains searchFor
   public Object[][] toArray();								//Returns a 2D array which represents the matrix
   public String toString();									//Returns a representation of the current sparse matrix
}