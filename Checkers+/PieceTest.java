import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PieceTest {

    private Board bEmpty;
    private Board b;

    @Before
    public void setUp() {
        bEmpty = new Board(true);
        b = new Board(false);


    }
    
    //movetesting JUNITs are in BOARD.TEST because LOTS OF TESTS DEPEND ON IT

    @Test
    public void pieceConstructorTest() {
        Piece test = new Piece(true, b, 1, 1, "pawn");      
        assertEquals(true, test.isFire());
        assertEquals(0, test.side());
        assertEquals(false, test.isKing());
        assertEquals(false, test.isBomb());
        assertEquals(false, test.isShield());
        assertEquals(false, test.hasCaptured());
        
        Piece test2 = new Piece(false, b, 3, 4, "bomb");
        assertEquals(false, test2.isFire());
        assertEquals(1, test2.side());
        assertEquals(false, test2.isKing());
        assertEquals(true, test2.isBomb());
        assertEquals(false, test2.isShield());
        assertEquals(false, test2.hasCaptured());
    }
    
    @Test
    public void hasCapturedAndDoneCapturingTest() {
    Piece b1 = new Piece(true, bEmpty, 1, 1, "pawn"); 
    bEmpty.place(b1, 1, 1);
    
    Piece b2 = new Piece(false, bEmpty, 2, 2, "pawn");
    bEmpty.place(b2, 2, 2);
    
    bEmpty.select(1, 1);
    bEmpty.select(3, 3);        
    assertEquals(true, b1.hasCaptured());
    
    b1.doneCapturing();
    assertEquals(false, b1.hasCaptured());
   }
    
    
    
    
    /* USED TO TEST VALIDMOVE WHEN I TEMPORARILY MADE IT PUBLIC
    @Test
    public void pieceMoveTest() { //check BoardTest: canSelectTest2 for further extensive testing
        Piece testBomb = new Piece(false, bEmpty, 2, 2, "bomb");
        Piece testPawn = new Piece(true, bEmpty, 3, 3, "pawn");
        Piece testPiece = new Piece(false, bEmpty, 4, 4, "bomb");
        
        bEmpty.place(testBomb, 2, 2); 
        bEmpty.place(testPawn, 3, 3); 
        bEmpty.place(testPiece, 4, 4); 
        assertEquals(false, bEmpty.validMove(2, 2, 4, 4));
        
        
        
        assertEquals(true, bEmpty.canSelect(3, 3));
        if (bEmpty.canSelect(3, 3)) {
            bEmpty.select(3, 3);
        }
        
        
        bEmpty.select(5, 5);
        
        assertEquals(null, bEmpty.pieceAt(4, 4));
        assertEquals(testPawn, bEmpty.pieceAt(5, 5));
        assertEquals(null, bEmpty.pieceAt(3, 3));
        
        
    }
    
    */
    
    



    public static void main(String... args) {
        jh61b.junit.textui.runClasses(PieceTest.class);
    }       
}