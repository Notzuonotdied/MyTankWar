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
    // 定义为全局静态变量
    private static Image[] tankImages;

    static {
        tankImages = new Image[]{
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/1.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/2.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/3.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/4.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m0.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m1.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m2.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m3.gif")),};
    }

    // 定义我的坦克的生命状态
    public boolean isLive = true;
    // 定义坐标（x,y）
    public int x;
    public int y;
    // 定义坦克的方向
    // 说明：0为方向上，1为方向右，2为方向下，3为方向左
    public Direction direct = Direction.UP;
    // 定义坦克的速度
    int speed = 5;
    Vector<Bullet> bullets = new Vector<>();
    private Random random;
    // 定义一个类型：0.为我的坦克，1.为怪物
    private int type;
    // 定义子弹集合
    private Bullet bullet = null;
    private int oldX, oldY;
    protected TankMember() {

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
        if (GamePanel.myTank.isLive) {
            // 启动声音
            new Audio("Shot.wav").start();
        }
        switch (this.direct) {
            case UP:
                bullet = new Bullet(x + (size - 8) / 2, y, Direction.UP);
                bullets.add(bullet);
                break;
            case RIGHT:
                bullet = new Bullet(x + size / 2, y + (size - 6) / 2, Direction.RIGHT);
                bullets.add(bullet);
                break;
            case DOWN:
                bullet = new Bullet(x + (size - 10) / 2, y + size / 2, Direction.DOWN);
                bullets.add(bullet);
                break;
            case LEFT:
                bullet = new Bullet(x, y + (size - 6) / 2, Direction.LEFT);
                bullets.add(bullet);
                break;
        }
        // 启动子弹线程
        CommonUtil.getInstance().startCachedThread(bullet);
    }

    // 画出坦克的函数
    public void drawTank(int x, int y, Graphics g, int type) {
        switch (direct) {
            case UP:
                if (type == 1) {
                    g.drawImage(tankImages[0], x, y, null);
                } else {
                    g.drawImage(tankImages[4], x - 7, y - 7, null);
                }
                break;
            case RIGHT:
                if (type == 1) {
                    g.drawImage(tankImages[1], x, y, null);
                } else {
                    g.drawImage(tankImages[5], x - 7, y - 7, null);
                }
                break;
            case DOWN:
                if (type == 1) {
                    g.drawImage(tankImages[2], x, y, null);
                } else {
                    g.drawImage(tankImages[6], x - 7, y - 7, null);
                }
                break;
            case LEFT:
                if (type == 1) {
                    g.drawImage(tankImages[3], x, y, null);
                } else {
                    g.drawImage(tankImages[7], x - 7, y - 7, null);
                }
                break;
        }
    }

    // 定义我的坦克的移动函数
    public abstract void move();

    // 一旦与其他坦克相遇就转向
    public Direction DictionDirection(Direction direct) {
        while (direct == Direction.UP) {
            this.direct = getRandomDirection();
        }
        return direct;
    }

    protected Direction getRandomDirection() {
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
    boolean isTouchBorder() {
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


    // 判断是否与普通墙相撞
    public abstract boolean isTouchCWall();

    // 判断是否与石墙相撞
    public abstract boolean isTouchBWall();

    // 判断是否接触
    public abstract boolean isTouchOtherTank(Direction direct);

    // 调用Rectangle函数
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, size, size);
    }

    // 保存旧的位置,保证出现错误的移动可以恢复到原先的位置
    public void set2OldDirect() {
        this.oldX = x;
        this.oldY = y;
    }

    // 在出现错误的移动后，改变现有位置为保存的旧的位置
    public void chang2OldDirect() {
        x = oldX;
        y = oldY;
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

}
