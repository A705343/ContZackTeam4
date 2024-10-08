import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.HashSet;
import java.util.Set;
import java.util.List; // <-- Import List class
import javafx.scene.image.Image;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Checkerboard {
    private final int cellSize;
    private final int borderSize;
    private final int checkerboardWidth;
    private final int checkerboardHeight;
    private final GridPane pane = new GridPane();
    private final Rectangle[][] blocks;
    private final Set<String> blockedCells = new HashSet<>(); // Set to store blocked cells
    private static final int BLOCK_SIZE = 80;

    public Checkerboard(int checkerboardWidth, int checkerboardHeight, int cellSize, int borderSize) {
        this.checkerboardWidth = checkerboardWidth;
        this.checkerboardHeight = checkerboardHeight;
        this.cellSize = cellSize;
        this.borderSize = borderSize;
        blocks = new Rectangle[checkerboardHeight][checkerboardWidth];
        initializeCheckerboard();
    }

    private void initializeCheckerboard() {
    boolean isWhite = true;
    for (int row = 0; row < checkerboardHeight; row++) {
        for (int col = 0; col < checkerboardWidth; col++) {
            Rectangle square = new Rectangle(cellSize, cellSize);
            
            square.setFill(isWhite ? Color.WHITE : Color.GRAY);
            pane.add(square, col, row);
            blocks[row][col] = square; // Store reference in the 2D array
            isWhite = !isWhite; // Alternate color for the next square
        }
        if (checkerboardWidth % 2 == 0) {
            isWhite = !isWhite; // Alternate color on new row if width is even
        }
    }
}

public void setLevel4Checkerboard() {
        for (int row = 0; row < checkerboardHeight; row++) {
            for (int col = 0; col < checkerboardWidth; col++) {
                blocks[row][col].setFill(Color.RED); // Set each block to red
                blocks[row][col].setStroke(Color.BLACK); // Optionally add a black stroke for the checkerboard effect
                blocks[row][col].setStrokeWidth(borderSize); // Set the stroke width
            }
        }
    }
    
    public void setLevel5Checkerboard() {
        for (int row = 0; row < checkerboardHeight; row++) {
            for (int col = 0; col < checkerboardWidth; col++) {
                blocks[row][col].setFill(Color.RED); // Set each block to red
                blocks[row][col].setStroke(Color.BLACK); // Optionally add a black stroke for the checkerboard effect
                blocks[row][col].setStrokeWidth(borderSize); // Set the stroke width
            }
        }
    }

public void setRowColor(int rowIndex, Color color) {
    if (rowIndex >= 0 && rowIndex < checkerboardHeight) {
        for (int col = 0; col < checkerboardWidth; col++) {
            blocks[rowIndex][col].setFill(color);
        }
    } else {
        throw new IndexOutOfBoundsException("Invalid row index.");
    }
}

public void positionRow(int rowIndex, double offset) {
    if (rowIndex >= 0 && rowIndex < checkerboardHeight) {
        for (int col = 0; col < checkerboardWidth; col++) {
            Rectangle block = blocks[rowIndex][col];
            block.setY((rowIndex * cellSize) + offset);
        }
    } else {
        throw new IndexOutOfBoundsException("Invalid row index.");
    }
}

    public Pane getPane() {
        return pane;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public int getCheckerboardWidth() {
        return checkerboardWidth;
    }

    public int getCheckerboardHeight() {
        return checkerboardHeight;
    }

    public double getFullWidth() {
        return checkerboardWidth * cellSize;
    }

    public double getFullHeight() {
        return checkerboardHeight * cellSize;
    }

    public Rectangle getBlock(int row, int col) {
        if (row >= 0 && row < checkerboardHeight && col >= 0 && col < checkerboardWidth) {
            return blocks[row][col];
        } else {
            throw new IndexOutOfBoundsException("Invalid block position.");
        }
    }

    public void setBlockColor(int row, int col, Color color) {
        Rectangle block = getBlock(row, col);
        block.setFill(color);
    }

    // Mark a block as blocked (non-interactable)
    public void blockCell(int row, int col) {
        blockedCells.add(row + "," + col);
        setBlockColor(row, col, Color.BLACK); // Optionally color the blocked cells differently
        addBoundaries(row, col);
    }

    // Add boundaries around blocked cells
    public void addBoundaries(int row, int col) {
    addBoundary(row - 1, col); // Top
    addBoundary(row + 1, col); // Bottom
    addBoundary(row, col - 1); // Left
    addBoundary(row, col + 1); // Right
}

    private void addBoundary(int row, int col) {
    if (row >= 0 && row < checkerboardHeight && col >= 0 && col < checkerboardWidth) {
        Rectangle block = getBlock(row, col);
        if (block.getFill() != Color.BLACK) { // Ensure it's not already blocked
            Rectangle boundary = new Rectangle(cellSize, cellSize);
            boundary.setFill(Color.TRANSPARENT); // Set to transparent to see underlying colors
            boundary.setStroke(Color.BLACK); // Use black for boundary color
            boundary.setStrokeWidth(borderSize);
            boundary.setX(col * cellSize);
            boundary.setY(row * cellSize);
            pane.getChildren().add(boundary); // Add the boundary to the pane
        }
    }
}

    // Check if a cell is blocked
    public boolean isCellBlocked(int row, int col) {
        return blockedCells.contains(row + "," + col);
    }

    // Add this method to draw the checkerboard on a Pane
    public void draw(Pane pane) {
        pane.getChildren().clear(); // Clear existing children
        pane.getChildren().add(this.pane); // Add the GridPane to the Pane
    }

  

    // Method to add a boundary to a specific block
public void addBlockBoundary(int blockX, int blockY) {
    double blockSize = cellSize; // Keep using the size of the blocks
    Rectangle boundary = new Rectangle(blockSize, blockSize); // Create a boundary that matches the block size
    boundary.setFill(Color.TRANSPARENT); // Transparent fill
    boundary.setStroke(Color.BLACK); // Black stroke for boundary
    boundary.setStrokeWidth(2); // Set the stroke width to 2 (or any value you want for the thickness)

    // Position the boundary at the exact position of the block
    boundary.setX(blockX * blockSize); // X position based on the block column
    boundary.setY(blockY * blockSize); // Y position based on the block row

    pane.getChildren().add(boundary); // Add the boundary to the pane
}

public void blockOutSpecificBlocks(List<int[]> blockedBlocks) {
    for (int[] block : blockedBlocks) {
        int row = block[1];
        int col = block[0];
        
        if (row >= 0 && row < checkerboardHeight && col >= 0 && col < checkerboardWidth) {
            // Set the fill color to black
            Rectangle square = blocks[row][col];
            square.setFill(Color.BLACK); // Fill black for all blocked cells
            
           
            
            // Mark the block as blocked
            blockedCells.add(row + "," + col); 
        }
    }
}


public void blockOutWallBlocks(List<int[]> wallBlocks) {
    for (int[] block : wallBlocks) {
        int col = block[0]; // Column
        int row = block[1]; // Row

        if (row >= 0 && row < checkerboardHeight && col >= 0 && col < checkerboardWidth) {
            // Set the corresponding block to gray
            Rectangle square = blocks[row][col];
            square.setFill(Color.DARKGRAY); // Set the fill color to gray
            
            blockedCells.add(row + "," + col); // Keep track of blocked cells
        }
    }
}

public void setAllBlocksColor(Color color) {
    for (int row = 0; row < checkerboardHeight; row++) {
        for (int col = 0; col < checkerboardWidth; col++) {
            blocks[row][col].setFill(color);
        }
    }
}


}