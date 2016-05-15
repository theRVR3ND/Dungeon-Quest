public interface Matrixable<T>{
   public T add(T addThis, byte r, byte c);				//Returns previous value at (r, c) before replacing it with addThis
   public T get(byte r, byte c);								//Returns value at (r, c)
   public boolean empty();										//Returns whether or not the matrix is empty
   public byte size();											//Number or values stored
   public byte numRow();										//Number or rows
   public byte numCol();										//Number of columns
   public boolean contains(T searchFor);					//Returns whether or not the matrix contains searchFor
   public Object[][] toArray();								//Returns a 2D array which represents the matrix
   public String toString();									//Returns a representation of the current sparse matrix
}