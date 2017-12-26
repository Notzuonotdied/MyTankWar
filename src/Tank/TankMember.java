package Tank;

import UIElement.BlockWall;
import Util.Audio;
import Util.CommonUtil;
import Util.Direction;

import java.awt.*;
import java.util.Random;
import java.util.Vector;

import static Util.CommonUtil.*;

/**
 * 主类-坦克类
 */
public abstract class TankMember {
    // 初始化坦克图片
    private static Image[] tankImages;

    static {
        tankImages = new Image[]{
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/monsterUp.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/monsterRight.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/monsterDown.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/monsterLeft.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/heroUp.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/heroRight.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/heroDown.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/heroLeft.gif")),};
    }

    // 定义我的坦克的生命状态
    public boolean isLive = true;
    // 定义坐标（x,y）
    public int x;
    public int y;
    // 定义坦克的方向
    public Direction direct = Direction.UP;
    // 定义坦克的速度
    int speed = 5;
    Vector<Bullet> bullets = new Vector<>();
    private Random random;
    // 定义子弹集合
    private Bullet bullet = null;
    private int oldX, oldY;

    TankMember() {

    }

    // 以下为成员变量的构造函数
    TankMember(int x, int y) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.random = new Random();
    }

    public BlockWall getBlockWall() {
        return BlockWall.getInstance();
    }

    // 开火的能力
    public void ShotEnemy() {
        if (MyTank.getInstance().isLive) {
            // 启动声音
            new Audio("Shot.wav").start();
        }
        switch (this.direct) {
            case UP:
                bullet = new Bullet(x + (size - 8) / 2, y, Direction.UP);
                break;
            case RIGHT:
                bullet = new Bullet(x + size / 2, y + (size - 6) / 2, Direction.RIGHT);
                break;
            case DOWN:
                bullet = new Bullet(x + (size - 10) / 2, y + size / 2, Direction.DOWN);
                break;
            case LEFT:
                bullet = new Bullet(x, y + (size - 6) / 2, Direction.LEFT);
                break;
        }
        bullets.add(bullet);
        // 启动子弹线程
        CommonUtil.getInstance().startCachedThread(bullet);
    }

    /**
     * 画出坦克的函数
     *
     * @param x    坦克的左上角的x
     * @param y    坦克左上角的y
     * @param g    画笔
     * @param type 坦克的类型，1是Monster，0是MyTank
     */
    public void drawTank(int x, int y, Graphics g, int type) {
        switch (direct) {
            case UP:
                drawATank(0, x, y, type, g);
                break;
            case RIGHT:
                drawATank(1, x, y, type, g);
                break;
            case DOWN:
                drawATank(2, x, y, type, g);
                break;
            case LEFT:
                drawATank(3, x, y, type, g);
                break;
        }
    }

    /**
     * 绘制一个坦克
     *
     * @param index    图片的索引
     * @param x        坦克的左上角的x
     * @param y        坦克左上角的y
     * @param type     坦克的类型，1是Monster，0是MyTank
     * @param graphics 画笔
     */
    private void drawATank(int index, int x, int y, int type, Graphics graphics) {
        if (type == 1) {
            graphics.drawImage(tankImages[index], x, y, null);
        } else {
            graphics.drawImage(tankImages[index + 4], x - 7, y - 7, null);
        }
    }

    /**
     * 定义我的坦克的移动函数
     */
    public abstract void move();

    /**
     * 一旦与其他坦克相遇就转向
     */
    public Direction DictionDirection(Direction direct) {
        while (direct == Direction.UP) {
            this.direct = getRandomDirection();
        }
        return direct;
    }

    Direction getRandomDirection() {
        switch (random.nextInt(3)) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.DOWN;
            case 3:
                return Direction.LEFT;
            default:
                return Direction.UP;
        }
    }

    // 判断是否越界
    private boolean isTouchBorder() {
        if (x < BlockWall.BlockWallWidth) {
            x = BlockWall.BlockWallWidth;
            return true;
        }
        if (y < BlockWall.BlockWallHeight) {
            y = BlockWall.BlockWallHeight;
            return true;
        }
        if (x + size > rangX) {
            x = rangX - size;
            return true;
        }
        if (y + size > rangY) {
            y = rangY - size;
            return true;
        }
        return false;
    }

    /**
     * 判断是否与普通墙相撞
     */
    public abstract boolean isTouchCWall();

    /**
     * 判断是否与石墙相撞
     */
    public abstract boolean isTouchBWall();

    /**
     * 判断是否接触
     */
    public abstract boolean isTouchOtherTank();

    /**
     * 调用Rectangle函数
     */
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, size, size);
    }

    /**
     * 保存旧的位置,保证出现错误的移动可以恢复到原先的位置
     */
    void set2OldDirect() {
        this.oldX = x;
        this.oldY = y;
    }

    /**
     * 在出现错误的移动后，改变现有位置为保存的旧的位置
     */
    private void chang2OldDirect() {
        x = oldX;
        y = oldY;
    }

    boolean canMove() {
        return !isTouchOtherTank() && !isTouchBorder() && !isTouchCWall() && !isTouchBWall();
    }

    boolean afterMoveIsOkay() {
        if (isTouchOtherTank() || isTouchBorder() || isTouchCWall() || isTouchBWall()) {
            chang2OldDirect();
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirect(Direction direct) {
        this.direct = direct;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    void sleepForMoment(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
