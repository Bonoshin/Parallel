package serialAbelianSandpile;
import java.util.concurrent.RecursiveAction;

import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import java.util.concurrent.ForkJoinPool;

public class ParallelGrid extends RecursiveAction{
    
    private static final int THRESHOLD = 12;
    private final Grid grid;
    private final int startRow, endRow;

    public ParallelGrid(Grid grid, int startRow, int endRow) {
        this.grid = grid;
        this.startRow = startRow;
        this.endRow = endRow;
    }


    @Override
    protected void compute() {
        if (endRow - startRow <= THRESHOLD) {
            updateStepless();
        } else {
            int mid = (startRow + endRow) / 2;
            ParallelGrid upperTask = new ParallelGrid(grid, startRow, mid);
            ParallelGrid lowerTask = new ParallelGrid(grid, mid, endRow);
            invokeAll(upperTask, lowerTask);
        }
    }

	private void updateStepless() {
        boolean change = false;
        for (int i = startRow; i < endRow; i++) {
            for (int j = 1; j < grid.getColumns() + 1; j++) {
                int newValue = (grid.get(i, j) % 4)
                        + (grid.get(i - 1, j) / 4)
                        + (grid.get(i + 1, j) / 4)
                        + (grid.get(i, j - 1) / 4)
                        + (grid.get(i, j + 1) / 4);
                if (grid.get(i, j) != newValue) {
                    change = true;
                }
                grid.updateGrid[i][j] = newValue;
            }
        }
    }
}
