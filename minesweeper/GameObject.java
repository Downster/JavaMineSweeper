package com.codegym.games.minesweeper;

public class GameObject {
    public int x;
    public int y;
    public boolean isMine;
    public int countMineNeighbors;
    public boolean isOpen;
    public boolean isFlag;

    GameObject(int x, int y, boolean mine){
        this.x = x;
        this.y = y;
        this.isMine = mine;
    }
}
