

public class Piece {
    private boolean isFire;
    private Board b;
    private int x;
    private int y;
    private String type;
    private boolean isKing;
    private boolean hasCaptured;


    public Piece(boolean isFire, Board b, int x, int y, String type) {
        this.isFire = isFire;
        this.b = b;
        this.x = x;
        this.y = y;
        this.type = type;
        this.isKing = false;
          
        
        
        
    }

    public boolean isFire() {
        return this.isFire;
    }

    public int side() {
        if (this.isFire) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean isKing() {
        return this.isKing;
    }

    public boolean isBomb() {
        if (type == "bomb") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isShield() {
        if (type == "shield") {
            return true;
        } else {
            return false;
        }
    }
    
    private void crowning(int y) {
        if (this.isFire() && y == 7) {
            this.isKing = true;
        } else if (!this.isFire() && y == 0) {
            this.isKing = true;
        }
    }
    

 
    //handles valid move-checking 
    public void move(int x, int y) {

        
        if (b.pieceAt(this.x + 1, this.y + 1) != null && (x == this.x + 2 && y == this.y + 2)) { //capture to the front right
                b.remove(this.x + 1, this.y + 1);
                this.hasCaptured = true;
        } 
        
        else if (b.pieceAt(this.x - 1, this.y + 1) != null && (x == this.x - 2 && y == this.y + 2)) { //capture to the front left
                b.remove(this.x - 1, this.y + 1);
                this.hasCaptured = true;
        }
        
                   
        if (b.pieceAt(this.x + 1, this.y - 1) != null && (x == this.x + 2 && y == this.y - 2)) { //capture to the back right
                b.remove(this.x + 1, this.y - 1);
                this.hasCaptured = true;
        }
        else if (b.pieceAt(this.x - 1, this.y - 1) != null && (x == this.x - 2 && y == this.y - 2)) { //capture to the back left
                b.remove(this.x - 1, this.y - 1);
                this.hasCaptured = true;
        }
        b.remove(this.x, this.y);
        b.place(this, x , y);

        
        this.x = x;
        this.y = y;
        
        if (this.isBomb() && this.hasCaptured) { //handles bomb moves
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    Piece surroundingSquare = b.pieceAt(i, j);
                    if (surroundingSquare != null) {
                       if (!surroundingSquare.isShield()) {
                           b.remove(i, j);
                       }
                    }
                }
            }
           
        } else {
            this.crowning(y);
        }
        
        
        
        
        
        
       
    }


    public boolean hasCaptured() {
        return this.hasCaptured;
    }

    public void doneCapturing() {
        this.hasCaptured = false;
    }



}
