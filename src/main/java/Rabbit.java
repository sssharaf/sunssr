import java.util.ArrayList;

public class Rabbit {
    public static void main(String[] args) {

        var gardenTest = new int[][] {
                {5, 7, 8, 6, 3,1},
                {0, 0, 7, 0, 4,1},
                {4, 6, 3, 4, 9,1},
                {3, 1, 0, 5, 8,1}
        };
        // Test findCenterPoint with even number of rows and cols
        var testCenterPoint = findCenterPoint(gardenTest);
        System.out.println(testCenterPoint);


        var garden = new int[][] {
                {5, 7, 8, 6, 3},
                {0, 0, 7, 0, 4},
                {4, 6, 3, 4, 9},
                {3, 1, 0, 5, 8}
        };

        var centerPoint = findCenterPoint(garden);
        int c = carrotsEatable(garden,centerPoint.r,centerPoint.c);
        System.out.println(c);
    }

    record Point(int r, int c) {}

    /*
        Given a matrix find the center point. If the number of rows
        and/or columns even then the element having maximum value
        is returned
     */
    public static Point findCenterPoint(final int[][] garden) {
        // Number of rows and cols - probably can be externalized
        final int nrows = garden.length;
        final int ncols = garden[0].length;
        var rowsToSearch = new ArrayList<Integer>();
        rowsToSearch.add(nrows/2);
        if (nrows%2 ==0 ) {
            rowsToSearch.add( (nrows/2) -1 );
        }
        var colsToSearch = new ArrayList<Integer>();
        colsToSearch.add(ncols/2);
        if ( ncols%2 == 0) {
            colsToSearch.add(ncols/2-1);
        }
        int maxCarrot = -1;
        Point point = null;
        for ( int row : rowsToSearch) {
            for (int col : colsToSearch) {
                if ( garden[row][col]> maxCarrot ) {
                    maxCarrot = garden[row][col];
                    point = new Point(row,col);
                }
            }
        }
        return point;
    }

    // DIR_DELTAS denote the deltas to be added to the current row and column
    private static final int[][] DIR_DELTAS = new int[][]{{1,0},{0,1},{-1,0},{0,-1}};


    /*
        carrotsEatable finds the number of carrots that can be eaten in a greedy way
        starting from the given row and col. It uses a depth first search.

        Note - Side effects: The method temporarily marks a slot as visited by changing the variable
        to -1 ( capitalizing the fact that values cannot be negative). The changed item is replaced
        just before the method returns.
     */
    public static int carrotsEatable(int[][] garden, int r, int c) {
        int carrotsEaten = garden[r][c];
        // Mark it as used
        System.out.printf("r:%d,c:%d%n",r,c);
        // Mark the slot as visited by setting as -1 so that our DFS traversal does not get into an infinite loop
        final int tmp = garden[r][c];
        garden[r][c] = -1;
        int nextRWithMaxCarrots = -1; // this will hold the next row having maximum # of carrots
        int nextCWithMaxCarrrots = -1;// this will hold the next col having maximum # of carrots
        int maxCarrotsInNext = 0;
        for (var direction : DIR_DELTAS) {
            final int nextR = r + direction[0];
            final int nextC = c + direction[1];
            // check if nextR/C are valid
            if(nextR < 0 || nextC < 0 || nextR >=garden.length || nextC >= garden[0].length) continue;
            // At this point the nextR and nextC are valid co-ordinates
            if( garden[nextR][nextC] < 0  ) continue;
            if ( garden[nextR][nextC] > maxCarrotsInNext ) {
                maxCarrotsInNext = garden[nextR][nextC];
                nextRWithMaxCarrots = nextR;
                nextCWithMaxCarrrots = nextC;
            }
        }
        // If we find a next viable move then proceed
        if (nextRWithMaxCarrots >=0 && nextCWithMaxCarrrots >=0) {
            carrotsEaten +=  carrotsEatable(garden,nextRWithMaxCarrots,nextCWithMaxCarrrots);
        }
        // Replace the item so that the grid is unchanged
        garden[r][c] = tmp;
        return carrotsEaten;
    }
}
