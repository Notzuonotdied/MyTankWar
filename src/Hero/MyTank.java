package Hero;

import UIElement.BlockWall;
import UIElement.CommonWall;
import Util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;

// 我的坦克类
public class MyTank extends TankMember {
    private static final int MaxBulletNum = 2;
    // 定义坦克的速度
    private static final int speed = 7;
    // 定义坦克的生命值
    public static int blood = 200;
    // 定义我的坦克类
    private static MyTank myTank;

    private MyTank() {
        super(600, 500);
        this.direct = Direction.UP;
    }

    MyTank(int x, int y) {
        super(x, y);

    }

    public static MyTank getInstance() {
        if (myTank == null) {
            synchronized (MyTank.class) {
                if (myTank == null) {
                    myTank = new MyTank();
                }
            }
        }
        return myTank;
    }

    public static void setInstance(MyTank tank) {
        myTank = tank;
    }

    /**
     * 画出我的坦克
     *
     * @param g 画笔
     */
    public void drawMyTank(Graphics g) {
        // 画出我自己的坦克
        if (myTank.isLive) {
            myTank.drawTank(myTank.x, myTank.y, g, 0);
        }

        // 画出子弹
        for (int i = 0; i < myTank.bullets.size(); i++) {
            if (!Bullet.getInstance().ifDrawBullet(myTank, g)) {
                // 如果子弹存在状态为假就移除子弹
                myTank.bullets.remove(myTank.bullets.get(i));
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            myTank.setDirect(Direction.DOWN);
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            myTank.setDirect(Direction.UP);
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            myTank.setDirect(Direction.LEFT);
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            myTank.setDirect(Direction.RIGHT);
            myTank.move();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (myTank.bullets.size() < MaxBulletNum) {
                myTank.ShotEnemy();
            }
        }
    }

    /**
     * 创建我的坦克,并恢复所有的Monster
     * */
    public void createMyTank() {
        myTank = new MyTank(600, 500);
        myTank.setDirect(Direction.UP);
        Monster.getInstance().restoreMonsters();
    }

    /**
     * 防止重叠
     */
    @Override
    public boolean isTouchOtherTank() {
        // 取出所有的敌人坦克
        for (int i = 0; i < Monster.getInstance().getMonsterSize(); i++) {
            // 取出第一个坦克
            Monster mon = Monster.getInstance().getMonsterAt(i);
            if (mon.getRect().intersects(myTank.getRect())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 定义我的坦克的移动函数
     */
    public void move() {
        // 保存旧的位置
        set2OldDirect();
        // 选择移动方向
        switch (direct) {
            case UP:
                if (canMove()) {
                    this.y -= speed;
                    this.afterMoveIsOkay();
                }
                break;
            case RIGHT:
                if (canMove()) {
                    this.x += speed;
                    this.afterMoveIsOkay();
                }
                break;
            case DOWN:
                if (canMove()) {
                    this.y += speed;
                    this.afterMoveIsOkay();
                }
                break;
            case LEFT:
                if (canMove()) {
                    x -= speed;
                    this.afterMoveIsOkay();
                }
                break;
        }
    }

    @Override
    public boolean isTouchCWall() {
        for (int i = 0; i < CommonWall.getInstance().getCWallsSize(); i++) {
            if (myTank.getRect().intersects(
                    CommonWall.getInstance().getCWallRectAt(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTouchBWall() {
        for (int i = 0; i < BlockWall.getInstance().getBWalls_1Size(); i++) {
            if (myTank.getRect().intersects(
                    BlockWall.getInstance().getBWallRectAt(i))) {
                return true;
            }
        }
        return false;
    }
}

