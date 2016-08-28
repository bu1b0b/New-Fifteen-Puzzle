package game.fifteen.ru.matrix;

import java.util.ArrayList;

/**
 * Created by Lokki on 14.09.2015.
 */

public class Matrix {

    private int playerMoves;
    private int[][] gameMatrix;
    private int zeroPointX;
    private int zeroPointY;

    public Matrix() {

    }

    public int[][] getMatrix() {
        return gameMatrix;
    }

    public int[][] setUserMove(int x, int y) {
        if (x == zeroPointX) {
            moveX(x, y);
        } else if (y == zeroPointY) {
            moveY(x, y);
        }

        return gameMatrix;
    }

    private void moveX(int posi, int posj) {
        ArrayList<Integer> tempArray = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tempArray.add(gameMatrix[posi][i]);
        }
        tempArray.remove(tempArray.indexOf(0));
        tempArray.add(posj, 0);
        for (int i = 0; i < 4; i++) {
            gameMatrix[posi][i] = tempArray.get(i);
            if (gameMatrix[posi][i] == 0) {
                zeroPointX = posi;
                zeroPointY = i;
            }
        }
        playerMoves++;
    }

    private void moveY(int posi, int posj) {
        ArrayList<Integer> tempArray = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tempArray.add(gameMatrix[i][posj]);
        }
        tempArray.remove(tempArray.indexOf(0));
        tempArray.add(posi, 0);
        for (int i = 0; i < 4; i++) {
            gameMatrix[i][posj] = tempArray.get(i);
            if (gameMatrix[i][posj] == 0) {
                zeroPointX = i;
                zeroPointY = posj;
            }
        }
        playerMoves++;
    }

    public int getPlayerMoves() {
        return playerMoves;
    }

    /////////////////////////////////////////
    public void initMatrix() {
        playerMoves = 0;
        gameMatrix = new int[4][4];
        ArrayList<Integer> matrix = new ArrayList<>();
        do {
            // от 0 до 15 включительно
            int rand = 0 + (int) (Math.random() * (15 - 0 + 1));
            if (!matrix.contains(rand)) {
                matrix.add(rand);
            }
        } while (matrix.size() != 16);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gameMatrix[i][j] = matrix.get(0);
                if (gameMatrix[i][j] == 0) {
                    zeroPointX = i;
                    zeroPointY = j;
                }
                matrix.remove(0);
            }
        }
    }
}
