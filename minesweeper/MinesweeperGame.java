package com.codegym.games.minesweeper;

import com.codegym.engine.cell.Color;
import com.codegym.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField = 0;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags = 0;
    private boolean isGameStopped = false;
    private int countClosedTiles = SIDE * SIDE;
    private int score = 0;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped){
            restart();
        } else {
            openTile(x, y);














        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.BEIGE, "Game over", Color.BLACK, 20);
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.BEIGE, "Congrats, you win!", Color.BLACK, 20);
    }

    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        setScore(score);
        countMinesOnField = 0;
        createGame();


    }

    private void markTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        if (!gameObject.isOpen && (countFlags != 0 && !gameObject.isFlag) && !isGameStopped) {
            gameObject.isFlag = true;
            countFlags--;
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.YELLOW);
        } else if (!gameObject.isOpen && (countFlags != 0 && gameObject.isFlag) && !isGameStopped){
            gameObject.isFlag = false;
            countFlags++;
            setCellValue(x, y, "");
            setCellColor(x, y, Color.ORANGE);
        }

    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
                setCellValue(x, y ,"");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }


    private void openTile(int x, int y){
            GameObject gameObject = gameField[y][x];
        if (!isGameStopped && !gameObject.isFlag && !gameObject.isOpen) {
            gameObject.isOpen = true;
            countClosedTiles--;
            setCellColor(x, y, Color.GREEN);
            if (countClosedTiles == countMinesOnField && !gameObject.isMine) {
                win();
            }else if (gameObject.isMine) {
                setCellValueEx(gameObject.x, gameObject.y, Color.RED, MINE);
                gameOver();
            } else if (gameObject.countMineNeighbors == 0) {
                score += 5;
                setScore(score);
                setCellValue(gameObject.x, gameObject.y, "");
                List<GameObject> neighbors = getNeighbors(gameObject);
                for (GameObject neighbor : neighbors) {
                    if (!neighbor.isOpen) {
                        openTile(neighbor.x, neighbor.y);
                    }
                }

            } else {
                setCellNumber(x, y, gameObject.countMineNeighbors);
                score += 5;
                setScore(score);
            }
        }


}


    private void countMineNeighbors(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject gameObject = gameField[y][x];

                if (!gameObject.isMine) {
                    gameObject.countMineNeighbors = Math.toIntExact(getNeighbors(gameObject).stream().filter(neighbor -> neighbor.isMine).count());
                }
            }
        }
    }
    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }
}