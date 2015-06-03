import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BoardTest  {

    private Board bEmpty;
    private Board b;


    @Before
    public void setUp() {
        bEmpty = new Board(true);             
        b = new Board(false);
        
        

    }
    

    @Test
    public void pieceAtTest() {
        assertEquals(true, b.pieceAt(0, 0).isFire());
        assertEquals(true, b.pieceAt(0, 2).isBomb());
        assertEquals(null, b.pieceAt(123, 123));      
    }
    
    
    /*
    @Test 
    public void validMoveTest() {
    assertEquals(true, b.validMove(0, 2, 1, 3));
    assertEquals(true, b.validMove(2, 2, 1, 3));
    assertEquals(true, b.validMove(2, 2, 3, 3));
    assertEquals(false, b.validMove(2, 2, 3, 2));
    
    Piece bEmptyPiece = new Piece(true, bEmpty, 0, 0, "shield");
    bEmpty.place(bEmptyPiece, 0, 0);
    Piece bEmptyPiece2 = new Piece(true, bEmpty, 1, 1, "pawn");
    bEmpty.place(bEmptyPiece2, 1, 1);
    assertEquals(false, bEmpty.validMove(0, 0, 1, 1));
    assertEquals(false, bEmpty.validMove(0, 0, 2, 2));
    
    }
    */
    
    
    
    
    //implement canSelect
    
    @Test
    public void canSelectTest1() {
        assertEquals(true, b.canSelect(0, 0));
        assertEquals(true, b.canSelect(3, 1));              
        assertEquals(false, b.canSelect(1, 5));
        assertEquals(false, bEmpty.canSelect(0, 0));
        assertEquals(false, b.canSelect(3, 2));
        assertEquals(false, b.canSelect(3, 0));
    }
    
    @Test
    public void canSelectTest2() { //ALSO TESTS FOR WORKING MOVE (includes bomb move method) , REMOVE, and PLACE METHOD
        Piece bEmptyPiece = new Piece(true, bEmpty, 0, 0, "shield");
        bEmpty.place(bEmptyPiece, 0, 0);
        bEmpty.select(0, 0);
        
        Piece bEmptyPiece2 = new Piece(false, bEmpty, 1, 1, "pawn");
        bEmpty.place(bEmptyPiece2, 1, 1);
        
        assertEquals(false, bEmpty.canSelect(1, 1));
        bEmptyPiece.move(2, 2);
        assertEquals(true, bEmptyPiece.hasCaptured());
        assertEquals(null, bEmpty.pieceAt(1, 1));
        assertEquals(bEmptyPiece, bEmpty.pieceAt(2, 2)); //fire shield at (2, 2)
        bEmpty.remove(2, 2);
        assertEquals(null, bEmpty.pieceAt(2, 2));
        
        
        Piece bomb = new Piece(false, bEmpty, 0 , 0, "bomb");
        Piece sacrifice = new Piece(true, bEmpty, 1, 1, "pawn");
        Piece victim = new Piece(true, bEmpty, 2, 1, "pawn");
        Piece victim2 = new Piece(false, bEmpty, 1, 2, "pawn");
        
        bEmpty.place(bomb, 0, 0);
        bEmpty.place(sacrifice, 1, 1);
        bEmpty.place(victim, 2, 1);
        bEmpty.place(victim2, 1, 2);
        
        bEmpty.select(0, 0);
        bEmpty.select(2, 2);
       
        assertEquals(null, bEmpty.pieceAt(1, 2));
        assertEquals(null, bEmpty.pieceAt(2, 1));
        assertEquals(null, bEmpty.pieceAt(1, 1));
        assertEquals(null, bEmpty.pieceAt(2, 2));
        assertEquals(null, bEmpty.pieceAt(0, 0));
              
    }
    /*
    @Test
    public void canSelectTest3() {
        
        Piece bEmptyPiece = new Piece(true, bEmpty, 5, 5, "bomb");
        bEmpty.place(bEmptyPiece, 5, 5);
        
        bEmpty.select(5, 5);
        System.out.println(bEmpty.hasMoved);
        assertEquals(true, bEmpty.canSelect(6, 6));
        
    }
    */
    
    @Test //crown jewel test
    public void canSelectTest4() {
        Piece b0 = new Piece(true, bEmpty, 3, 3, "pawn");
        Piece b1 = new Piece(false, bEmpty, 4, 4, "pawn");
        Piece b2 = new Piece(false, bEmpty, 6, 6, "shield");
        
        bEmpty.place(b0, 3, 3);
        bEmpty.place(b1, 4, 4);
        bEmpty.place(b2, 6, 6);
        
        
        assertEquals(true, bEmpty.canSelect(3, 3));
        bEmpty.select(3,  3);
        
        assertEquals(true, bEmpty.canSelect(5, 5));
        bEmpty.select(5, 5);
        
        assertEquals(true, bEmpty.canSelect(7, 7));
        assertEquals(false, bEmpty.canSelect(5, 5));
        
        bEmpty.select(7,  7);
        
        assertEquals(true, b0.isKing());
        
        
    }
    


    @Test
    public void placeTest() {  
        Piece bEmptyPiece = new Piece(true, bEmpty, 1, 1, "pawn"); //placing normally
        bEmpty.place(bEmptyPiece, 1, 1);
        assertEquals(bEmptyPiece, bEmpty.pieceAt(1, 1));
        
        bEmpty.place(bEmptyPiece, -123, 4); //placing out of bounds
        assertEquals(bEmptyPiece, bEmpty.pieceAt(1, 1)); 
        
        Piece bEmptyPiece2 = new Piece(false, bEmpty, 2, 2, "pawn");
        bEmpty.place(bEmptyPiece2, 2, 2);
        
        bEmpty.place(bEmptyPiece, 2, 2);
        assertEquals(bEmptyPiece, bEmpty.pieceAt(2, 2));
      
    }
    
    @Test
    public void winnerTest() {
        assertEquals("No one", bEmpty.winner());
        
        Piece b1 = new Piece(true, bEmpty, 1, 1, "bomb"); //placing normally
        bEmpty.place(b1, 1, 1);
        
        Piece b2 = new Piece(false, bEmpty, 2, 2, "pawn");
        bEmpty.place(b2, 2, 2);
        
        b1.move(3, 3);
        
        assertEquals("No one", bEmpty.winner());
        
        Piece b3 = new Piece(false, bEmpty, 5, 5, "pawn");
        bEmpty.place(b3, 5, 5);
        
        assertEquals("Water", bEmpty.winner());
               
    }
    
    @Test
    public void winnerTest2() {
    
        Piece b1 = new Piece(true, bEmpty, 1, 1, "bomb"); //placing normally
        bEmpty.place(b1, 1, 1);
        
        Piece b2 = new Piece(false, bEmpty, 2, 2, "pawn");
        bEmpty.place(b2, 2, 2);
        
        Piece b3 = new Piece(false, bEmpty, 3, 2, "pawn");
        
        b1.move(3, 3);
        assertEquals("No one", bEmpty.winner());

               
    }
    
    @Test
   public void winnerTest3() {
 
        Piece b1 = new Piece(true, bEmpty, 1, 1, "bomb"); //placing normally
        bEmpty.place(b1, 1, 1);
        
        Piece b2 = new Piece(false, bEmpty, 2, 2, "pawn");
        bEmpty.place(b2, 2, 2);
        
        Piece b3 = new Piece(false, bEmpty, 3, 2, "shield");
        bEmpty.place(b3, 3, 2);
        
        b1.move(3, 3);
        assertEquals("Water", bEmpty.winner());
    
    }
   @Test
   public void selectTest() {
       Piece b1 = new Piece(true, bEmpty, 1, 1, "bomb"); //placing normally
       bEmpty.place(b1, 1, 1);
       
       Piece b2 = new Piece(false, bEmpty, 2, 2, "pawn");
       bEmpty.place(b2, 2, 2);
       
       Piece b3 = new Piece(false, bEmpty, 3, 2, "shield");
       bEmpty.place(b3, 3, 2);
       
       bEmpty.select(1, 1);      
       bEmpty.select(3, 3);
       
       assertEquals(null, bEmpty.pieceAt(1, 1));
       assertEquals(null, bEmpty.pieceAt(2, 2));
       assertEquals(b3, bEmpty.pieceAt(3, 2));
       
   }
   
   @Test
   public void selectTest2() {
       b.select(4, 2);
       b.select(5, 3);
       b.endTurn();
       
       b.select(3, 5);
       b.select(4, 4);
       b.endTurn();
       
       b.select(5, 3);
       b.select(3, 5);
       
       assertEquals(null, b.pieceAt(3, 5));
       assertEquals(null, b.pieceAt(4, 4));
      
   }
   
   @Test
   public void selectTest3() {
       
       assertEquals(true, b.canSelect(2, 2));
       if (b.canSelect(2, 2)) {
           b.select(2, 2);
       }
       
       assertEquals(true, b.canSelect(3, 3));
       if (b.canSelect(3, 3)) {
           b.select(3, 3);
       }
       
       assertEquals(true, b.canEndTurn());
       if(b.canEndTurn()) {
           b.endTurn();
       }
       
       assertEquals(true, b.canSelect(1, 5));
       if (b.canSelect(1, 5)) {
           b.select(1, 5);
       }
       
       assertEquals(true, b.canSelect(2, 4));
       if (b.canSelect(2, 4)) {
           b.select(2, 4);
       }
       
       assertEquals(true, b.canEndTurn());
       if(b.canEndTurn()) {
           b.endTurn();
       }
       
       assertEquals(true, b.canSelect(3, 3));
       if (b.canSelect(3, 3)) {
           b.select(3, 3);
       }
       
       assertEquals(true, b.canSelect(1, 5));
       if (b.canSelect(1, 5)) {
           b.select(1, 5);
       }
       
       assertEquals(true, b.canEndTurn());
       if(b.canEndTurn()) {
           b.endTurn();
       }
       
       assertEquals(null, b.pieceAt(3, 3));
       assertEquals(null, b.pieceAt(2, 4));
   }

    
   @Test
   public void endTurnTest() {
 
        Piece b1 = new Piece(true, bEmpty, 1, 1, "pawn"); //placing normally
        bEmpty.place(b1, 1, 1);
        
        Piece b2 = new Piece(false, bEmpty, 2, 2, "pawn");
        bEmpty.place(b2, 2, 2);
        
        bEmpty.select(1, 1);
        bEmpty.select(3, 3);        
        
        bEmpty.endTurn();
        assertEquals(false, b1.hasCaptured());
        
       
    
    }


    public static void main(String... args) {
        jh61b.junit.textui.runClasses(PieceTest.class);
    }       
}